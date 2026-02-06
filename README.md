Minecraft Server Inserter

Modern GUI tool for importing Minecraft multiplayer servers from a text
file into servers.dat.

------------------------------------------------------------------------

FEATURES - Modern UI (FlatLaf) - Dark / Light theme toggle - Drag & Drop
file support - Preview servers before importing - Format validation -
Cross-platform (Windows / Linux / macOS) - Sorting servers in table -
Auto scroll for large server lists

------------------------------------------------------------------------

MOTIVATION

This project was originally made just for fun.

I came across an interesting Minecraft server scanner that was parsing
server names and IP addresses. After experimenting with it, I thought it
would be cool to create a simple tool that allows importing those
results directly into Minecraft’s servers.dat.

So this app was built as a small quality-of-life tool and later turned
into a full GUI project.

------------------------------------------------------------------------

HOW IT WORKS

The app reads a servers.txt file and inserts entries into Minecraft’s
servers.dat (NBT format).

------------------------------------------------------------------------

SERVERS.TXT FORMAT

Server Name;IP Address

Example: Hypixel;mc.hypixel.net My SMP;play.mysmp.org Local
Server;127.0.0.1

------------------------------------------------------------------------

DEFAULT MINECRAFT PATHS

Windows: %APPDATA%.minecraft.dat

Linux: ~/.minecraft/servers.dat

macOS: ~/Library/Application Support/minecraft/servers.dat

------------------------------------------------------------------------

INSTALLATION

Requirements: - Java 17+ - Maven

Build: mvn clean package

Run: java -jar target/minecraft-server-inserter.jar

------------------------------------------------------------------------

USAGE

1.  Select or drag servers.txt
2.  Select Minecraft servers.dat
3.  Click Load Preview
4.  Check server list
5.  Click Insert Servers

------------------------------------------------------------------------

DEPENDENCIES - FlatLaf - OpenNBT

------------------------------------------------------------------------

DISCLAIMER

This tool modifies Minecraft servers.dat. Always backup your file before
importing.

------------------------------------------------------------------------

LICENSE MIT License
