package dev.hexwald.serverinserter.util;

import java.io.File;
import java.nio.file.Path;
import java.util.Locale;

public class MinecraftPaths {

    public static File getDefaultServersDat() {
        return getMinecraftDirectory().resolve("servers.dat").toFile();
    }

    private static Path getMinecraftDirectory() {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
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

}
