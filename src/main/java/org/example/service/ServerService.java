package org.example.service;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.*;
import org.example.model.ServerEntry;

import java.io.File;
import java.util.List;

public class ServerService {

    public static void insert(File datFile, List<ServerEntry> entries) throws Exception {

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
    }
}

