package org.example.util;

import org.example.model.ServerEntry;

import java.io.*;
import java.util.*;

public class ServerParser {

    public static List<ServerEntry> parse(File file) throws Exception {

        List<ServerEntry> list = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int lineNum = 0;

        while ((line = br.readLine()) != null) {

            lineNum++;

            String[] parts = line.split(";");

            if (parts.length != 2) {
                throw new Exception("Ошибка формата в строке " + lineNum);
            }

            list.add(new ServerEntry(parts[0].trim(), parts[1].trim()));
        }

        br.close();
        return list;
    }
}
