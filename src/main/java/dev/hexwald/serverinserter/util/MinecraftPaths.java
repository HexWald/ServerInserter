package dev.hexwald.serverinserter.util;

import java.io.File;
import java.nio.file.Path;

public class MinecraftPaths {

    public static File getDefaultServersDat() {
        return getMinecraftDirectory().resolve("servers.dat").toFile();
    }

    private static Path getMinecraftDirectory() {
        String os = System.getProperty("os.name", "").toLowerCase();
        String home = System.getProperty("user.home");

        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");
            if (appData != null && !appData.isBlank()) {
                return Path.of(appData, ".minecraft");
            }
        }

        if (os.contains("mac")) {
            return Path.of(home, "Library", "Application Support", "minecraft");
        }

        return Path.of(home, ".minecraft");
    }

    public static String getHints() {
        return """
Default location of servers.dat:

Windows:
  %APPDATA%\\.minecraft\\servers.dat

Linux:
  ~/.minecraft/servers.dat

MacOS:
  ~/Library/Application Support/minecraft/servers.dat

Tip:
You can drag & drop files into the fields above.
Format of servers.txt:
ServerName;IP
""";
    }
}
