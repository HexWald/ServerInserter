package org.example.service;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.*;
import org.example.model.ServerEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServerService {

    private static final DateTimeFormatter BACKUP_STAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    public static File insert(File datFile, List<ServerEntry> entries) throws Exception {

        if (datFile == null || datFile.getPath().isBlank()) {
            throw new IllegalArgumentException("Select servers.dat first.");
        }

        if (entries == null || entries.isEmpty()) {
            throw new IllegalArgumentException("Nothing to insert. Load preview first.");
        }

        File parent = datFile.getAbsoluteFile().getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Cannot create folder: " + parent.getAbsolutePath());
        }

        File backup = datFile.exists() ? createBackup(datFile) : null;

        CompoundTag root;

        if (datFile.exists()) {
            root = (CompoundTag) NBTIO.readFile(datFile, false, false);
        } else {
            root = new CompoundTag("servers");
            root.put(new ListTag("servers", CompoundTag.class));
        }

        ListTag list = root.get("servers");

        for (ServerEntry e : entries) {

            CompoundTag tag = new CompoundTag("");
            tag.put(new StringTag("name", e.getName()));
            tag.put(new StringTag("ip", e.getIp()));

            list.add(tag);
        }

        NBTIO.writeFile(root, datFile, false, false);
        return backup;
    }

    private static File createBackup(File datFile) throws IOException {
        Path source = datFile.toPath();
        String stamp = BACKUP_STAMP.format(LocalDateTime.now());
        Path backup = source.resolveSibling(datFile.getName() + "." + stamp + ".bak");

        Files.copy(source, backup);
        return backup.toFile();
    }
}
