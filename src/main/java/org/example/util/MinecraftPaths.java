package org.example.util;

public class MinecraftPaths {

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
