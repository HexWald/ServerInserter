package dev.hexwald.serverinserter.model;

public class ServerEntry {

    private String name;
    private String ip;

    public ServerEntry(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public String getName() { return name; }
    public String getIp() { return ip; }
}

