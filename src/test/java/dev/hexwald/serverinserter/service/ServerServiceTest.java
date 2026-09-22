package dev.hexwald.serverinserter.service;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import dev.hexwald.serverinserter.model.ServerEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void createsServersDatWhenMissing() throws Exception {
        File datFile = tempDir.resolve("servers.dat").toFile();

        ImportResult result = ServerService.insert(datFile, List.of(
                new ServerEntry("Hypixel", "mc.hypixel.net"),
                new ServerEntry("Localhost", "127.0.0.1")
        ));

        assertEquals(2, result.inserted());
        assertEquals(0, result.skippedDuplicates());
        assertNull(result.backupFile());
        assertTrue(datFile.isFile());

        ListTag servers = readServers(datFile);
        assertEquals(2, servers.size());
        assertServer(servers, 0, "Hypixel", "mc.hypixel.net");
        assertServer(servers, 1, "Localhost", "127.0.0.1");
    }

    @Test
    void appendsServersAndCreatesBackup() throws Exception {
        File datFile = tempDir.resolve("servers.dat").toFile();

        ServerService.insert(datFile, List.of(new ServerEntry("First", "first.example.org")));
        ImportResult result = ServerService.insert(datFile, List.of(new ServerEntry("Second", "second.example.org")));

        assertEquals(1, result.inserted());
        assertEquals(0, result.skippedDuplicates());
        assertNotNull(result.backupFile());
        assertTrue(result.backupFile().isFile());
        assertTrue(result.backupFile().getName().startsWith("servers.dat."));
        assertTrue(result.backupFile().getName().endsWith(".bak"));

        ListTag backupServers = readServers(result.backupFile());
        assertEquals(1, backupServers.size());
        assertServer(backupServers, 0, "First", "first.example.org");

        ListTag servers = readServers(datFile);
        assertEquals(2, servers.size());
        assertServer(servers, 0, "First", "first.example.org");
        assertServer(servers, 1, "Second", "second.example.org");
    }

    @Test
    void skipsDuplicateIpsWhileInsertingNewOnes() throws Exception {
        File datFile = tempDir.resolve("servers.dat").toFile();

        ServerService.insert(datFile, List.of(new ServerEntry("First", "first.example.org")));
        ImportResult result = ServerService.insert(datFile, List.of(
                new ServerEntry("Same existing", " FIRST.EXAMPLE.ORG "),
                new ServerEntry("Second", "second.example.org"),
                new ServerEntry("Same batch", "second.example.org")
        ));

        assertEquals(1, result.inserted());
        assertEquals(2, result.skippedDuplicates());
        assertNotNull(result.backupFile());

        ListTag servers = readServers(datFile);
        assertEquals(2, servers.size());
        assertServer(servers, 0, "First", "first.example.org");
        assertServer(servers, 1, "Second", "second.example.org");
    }

    @Test
    void skipsAllDuplicatesWithoutCreatingBackup() throws Exception {
        File datFile = tempDir.resolve("servers.dat").toFile();

        ServerService.insert(datFile, List.of(new ServerEntry("First", "first.example.org")));
        ImportResult result = ServerService.insert(datFile, List.of(new ServerEntry("Again", "first.example.org")));

        assertEquals(0, result.inserted());
        assertEquals(1, result.skippedDuplicates());
        assertNull(result.backupFile());

        ListTag servers = readServers(datFile);
        assertEquals(1, servers.size());
        assertServer(servers, 0, "First", "first.example.org");
    }

    @Test
    void comparesEquivalentServerAddresses() throws Exception {
        File datFile = tempDir.resolve("servers.dat").toFile();

        ServerService.insert(datFile, List.of(
                new ServerEntry("Domain", "Example.org."),
                new ServerEntry("IPv6", "[2001:DB8::1]:25565")
        ));

        ImportResult result = ServerService.insert(datFile, List.of(
                new ServerEntry("Same domain", " example.org:25565 "),
                new ServerEntry("Same IPv6", "2001:db8::1"),
                new ServerEntry("Custom port", "EXAMPLE.ORG:25566"),
                new ServerEntry("Custom IPv6 port", "[2001:db8::1]:25566")
        ));

        assertEquals(2, result.inserted());
        assertEquals(2, result.skippedDuplicates());

        ListTag servers = readServers(datFile);
        assertEquals(4, servers.size());
        assertServer(servers, 2, "Custom port", "EXAMPLE.ORG:25566");
        assertServer(servers, 3, "Custom IPv6 port", "[2001:db8::1]:25566");
    }

    @Test
    void rejectsEmptyInsertList() {
        File datFile = tempDir.resolve("servers.dat").toFile();

        Exception error = assertThrows(Exception.class, () -> ServerService.insert(datFile, List.of()));

        assertEquals("Nothing to insert. Load preview first.", error.getMessage());
        assertFalse(Files.exists(datFile.toPath()));
    }

    @Test
    void rejectsNbtWithoutServersList() throws Exception {
        File datFile = tempDir.resolve("invalid.dat").toFile();
        CompoundTag root = new CompoundTag("root");
        root.put(new com.github.steveice10.opennbt.tag.builtin.StringTag("name", "not a server list"));
        NBTIO.writeFile(root, datFile, false, false);

        IOException error = assertThrows(IOException.class, () -> ServerService.insert(
                datFile, List.of(new ServerEntry("Test", "example.org"))));

        assertEquals("The selected NBT file does not contain a servers list.", error.getMessage());
    }

    private static ListTag readServers(File datFile) throws Exception {
        CompoundTag root = (CompoundTag) NBTIO.readFile(datFile, false, false);
        return root.get("servers");
    }

    private static void assertServer(ListTag servers, int index, String expectedName, String expectedIp) {
        CompoundTag server = (CompoundTag) servers.get(index);
        assertEquals(expectedName, server.get("name").getValue());
        assertEquals(expectedIp, server.get("ip").getValue());
    }
}
