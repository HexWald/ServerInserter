package dev.hexwald.serverinserter.service;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.*;
import dev.hexwald.serverinserter.model.ServerEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ServerService {

    private static final DateTimeFormatter BACKUP_STAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    public static ImportResult insert(File datFile, List<ServerEntry> entries) throws Exception {

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

        CompoundTag root;

        if (datFile.exists()) {
            root = (CompoundTag) NBTIO.readFile(datFile, false, false);
        } else {
            root = new CompoundTag("servers");
            root.put(new ListTag("servers", CompoundTag.class));
        }

        ListTag list = root.get("servers");
        Set<String> knownIps = collectKnownIps(list);
        int inserted = 0;
        int skippedDuplicates = 0;

        for (ServerEntry e : entries) {
            String normalizedIp = normalizeIp(e.getIp());
            if (!knownIps.add(normalizedIp)) {
                skippedDuplicates++;
                continue;
            }

            CompoundTag tag = new CompoundTag("");
            tag.put(new StringTag("name", e.getName()));
            tag.put(new StringTag("ip", e.getIp()));

            list.add(tag);
            inserted++;
        }

        if (inserted == 0) {
            return new ImportResult(0, skippedDuplicates, null);
        }

        File backup = datFile.exists() ? createBackup(datFile) : null;
        NBTIO.writeFile(root, datFile, false, false);
        return new ImportResult(inserted, skippedDuplicates, backup);
    }

    private static File createBackup(File datFile) throws IOException {
        Path source = datFile.toPath();
        String stamp = BACKUP_STAMP.format(LocalDateTime.now());
        Path backup = source.resolveSibling(datFile.getName() + "." + stamp + ".bak");

        Files.copy(source, backup);
        return backup.toFile();
    }

    private static Set<String> collectKnownIps(ListTag servers) {
        Set<String> ips = new HashSet<>();

        for (int i = 0; i < servers.size(); i++) {
            Tag tag = servers.get(i);
            if (!(tag instanceof CompoundTag serverTag)) {
                continue;
            }

            Tag ipTag = serverTag.get("ip");
            if (ipTag instanceof StringTag stringTag) {
                ips.add(normalizeIp(stringTag.getValue()));
            }
        }

        return ips;
    }

    private static String normalizeIp(String ip) {
        return ip == null ? "" : ip.trim().toLowerCase(Locale.ROOT);
    }
}
