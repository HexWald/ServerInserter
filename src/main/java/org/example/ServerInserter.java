package org.example;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.*;

import java.io.*;
import java.util.*;

public class ServerInserter {

    public static void main(String[] args) throws Exception {

        File serversFile = new File(System.getProperty("user.home") + "/.minecraft/servers.dat");

        CompoundTag root;

        if (serversFile.exists()) {
            root = (CompoundTag) NBTIO.readFile(serversFile, false, false);
        } else {

            serversFile.getParentFile().mkdirs();

            root = new CompoundTag("servers");
            root.put(new ListTag("servers", CompoundTag.class));
        }


        ListTag serverList = root.get("servers");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        ServerInserter.class.getClassLoader()
                                .getResourceAsStream("servers.txt")
                )
        );

        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length != 2) continue;

            CompoundTag server = new CompoundTag("");
            server.put(new StringTag("name", parts[0]));
            server.put(new StringTag("ip", parts[1]));

            serverList.add(server);
        }

        br.close();

        NBTIO.writeFile(root, serversFile, false, false);

        System.out.println("Server be added!");
    }
}
