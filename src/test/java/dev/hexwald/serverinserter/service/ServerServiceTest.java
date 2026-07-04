package dev.hexwald.serverinserter.service;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import dev.hexwald.serverinserter.model.ServerEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
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

        File backup = ServerService.insert(datFile, List.of(
                new ServerEntry("Hypixel", "mc.hypixel.net"),
                new ServerEntry("Localhost", "127.0.0.1")
        ));

        assertNull(backup);
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
        File backup = ServerService.insert(datFile, List.of(new ServerEntry("Second", "second.example.org")));

        assertNotNull(backup);
        assertTrue(backup.isFile());
        assertTrue(backup.getName().startsWith("servers.dat."));
        assertTrue(backup.getName().endsWith(".bak"));

        ListTag backupServers = readServers(backup);
        assertEquals(1, backupServers.size());
        assertServer(backupServers, 0, "First", "first.example.org");

        ListTag servers = readServers(datFile);
        assertEquals(2, servers.size());
        assertServer(servers, 0, "First", "first.example.org");
        assertServer(servers, 1, "Second", "second.example.org");
    }

    @Test
    void rejectsEmptyInsertList() {
        File datFile = tempDir.resolve("servers.dat").toFile();

        Exception error = assertThrows(Exception.class, () -> ServerService.insert(datFile, List.of()));

        assertEquals("Nothing to insert. Load preview first.", error.getMessage());
        assertFalse(Files.exists(datFile.toPath()));
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
