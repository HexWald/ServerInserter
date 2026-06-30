package dev.hexwald.serverinserter.util;

import dev.hexwald.serverinserter.model.ServerEntry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class ServerParser {

    public static List<ServerEntry> parse(File file) throws Exception {

        if (file == null || file.getPath().isBlank()) {
            throw new FileNotFoundException("Select servers.txt first.");
        }

        if (!file.isFile()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }

        List<ServerEntry> list = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String line;
            int lineNum = 0;

            while ((line = br.readLine()) != null) {

                lineNum++;
                String cleanLine = line.trim();

                if (cleanLine.isEmpty() || cleanLine.startsWith("#")) {
                    continue;
                }

                list.add(parseLine(cleanLine, lineNum));
            }
        }

        if (list.isEmpty()) {
            throw new Exception("No servers found in file.");
        }

        return list;
    }

    private static ServerEntry parseLine(String line, int lineNum) throws Exception {
        String[] parts = line.split(";", 2);

        if (parts.length != 2) {
            throw new Exception("Format error on line " + lineNum + ". Expected: Server Name;IP Address");
        }

        String name = parts[0].trim();
        String ip = parts[1].trim();

        if (name.isEmpty() || ip.isEmpty()) {
            throw new Exception("Empty server name or IP on line " + lineNum);
        }

        return new ServerEntry(name, ip);
    }
}
