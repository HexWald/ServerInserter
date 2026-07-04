package dev.hexwald.serverinserter.util;

import dev.hexwald.serverinserter.model.ServerEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerParserTest {

    @TempDir
    Path tempDir;

    @Test
    void parsesServerListAndSkipsComments() throws Exception {
        Path file = tempDir.resolve("servers.txt");
        Files.writeString(
                file,
                """
                # favorites
                Hypixel;mc.hypixel.net

                Localhost ; 127.0.0.1
                """,
                StandardCharsets.UTF_8
        );

        List<ServerEntry> servers = ServerParser.parse(file.toFile());

        assertEquals(2, servers.size());
        assertEquals("Hypixel", servers.get(0).getName());
        assertEquals("mc.hypixel.net", servers.get(0).getIp());
        assertEquals("Localhost", servers.get(1).getName());
        assertEquals("127.0.0.1", servers.get(1).getIp());
    }

    @Test
    void rejectsInvalidLineWithLineNumber() throws Exception {
        Path file = tempDir.resolve("bad.txt");
        Files.writeString(file, "Hypixel mc.hypixel.net", StandardCharsets.UTF_8);

        Exception error = assertThrows(Exception.class, () -> ServerParser.parse(file.toFile()));

        assertTrue(error.getMessage().contains("line 1"));
    }

    @Test
    void rejectsEmptyList() throws Exception {
        Path file = tempDir.resolve("empty.txt");
        Files.writeString(file, "# nothing here", StandardCharsets.UTF_8);

        Exception error = assertThrows(Exception.class, () -> ServerParser.parse(file.toFile()));

        assertEquals("No servers found in file.", error.getMessage());
    }
}
