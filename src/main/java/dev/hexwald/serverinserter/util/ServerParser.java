package dev.hexwald.serverinserter.util;

import dev.hexwald.serverinserter.model.ServerEntry;

import java.io.*;
import java.net.IDN;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class ServerParser {

    public static List<ServerEntry> parse(File file) throws IOException {

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
                if (lineNum == 1 && line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                String cleanLine = line.trim();

                if (cleanLine.isEmpty() || cleanLine.startsWith("#")) {
                    continue;
                }

                list.add(parseLine(cleanLine, lineNum));
            }
        }

        if (list.isEmpty()) {
            throw new IOException("No servers found in file.");
        }

        return list;
    }

    private static ServerEntry parseLine(String line, int lineNum) throws IOException {
        String[] parts = splitServerLine(line);

        if (parts.length != 2) {
            throw new IOException("Missing separator on line " + lineNum + ". Use ; or ,");
        }

        String name = parts[0].trim();
        String ip = parts[1].trim();

        if (name.isEmpty() || ip.isEmpty()) {
            throw new IOException("Empty server name or address on line " + lineNum);
        }
        if (!validAddress(ip)) {
            throw new IOException("Invalid server address on line " + lineNum + ": " + ip);
        }

        return new ServerEntry(name, ip);
    }

    private static String[] splitServerLine(String line) {
        if (line.contains(";")) {
            return line.split(";", 2);
        }

        return line.split(",", 2);
    }

    private static boolean validAddress(String address) {
        if (address.startsWith("[")) {
            int end = address.indexOf(']');
            return end > 0 && validIpv6(address.substring(1, end))
                    && (end == address.length() - 1 || validPort(address.substring(end + 1)));
        }

        int colon = address.indexOf(':');
        if (colon != address.lastIndexOf(':')) {
            return validIpv6(address);
        }
        String host = colon < 0 ? address : address.substring(0, colon);
        try {
            String ascii = IDN.toASCII(host, IDN.USE_STD3_ASCII_RULES);
            return !ascii.isEmpty() && !ascii.equals(".")
                    && (colon < 0 || validPort(address.substring(colon)));
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static boolean validIpv6(String address) {
        String host = "[" + address + "]";
        try {
            return host.equals(new URI("minecraft://" + host).getHost());
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    private static boolean validPort(String suffix) {
        if (!suffix.matches(":[0-9]+")) {
            return false;
        }
        try {
            int port = Integer.parseInt(suffix.substring(1));
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
