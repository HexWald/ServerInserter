# ServerInserter

Adds servers from a text file to Minecraft's multiplayer list. Tested with Minecraft Java 26.3. ServerInserter runs on Java 17 or newer. [Download v1.1.1](https://github.com/HexWald/ServerInserter/releases/tag/v1.1.1)

```sh
java -jar minecraft-server-inserter-1.1.1.jar
```

## Import a list

1. Close Minecraft.
2. Choose a UTF-8 text file and click **Load Preview**. You can also drop the file onto the server list field.
3. Pick `servers.dat`. **Use default** selects the standard Minecraft folder. For a custom launcher instance, pick its file manually.
4. Check the preview and click **Insert Servers**.

Existing entries stay in the list, and duplicate addresses are skipped. Before replacing an existing file, ServerInserter saves a timestamped `.bak` beside it. To undo the import, close Minecraft and copy that backup over `servers.dat`.

## List format

Put one server per line, with a semicolon or comma between the name and address. Blank lines and lines starting with `#` are ignored.

```text
# Favorites
Hypixel;mc.hypixel.net
Friends,play.example.org:25566
Local;127.0.0.1
IPv6;[2001:db8::1]:25565
```

Use brackets around an IPv6 address with a port. The default port is 25565.

## Build

From the repository root, run `mvnw.cmd verify` on Windows or `./mvnw verify` on Linux and macOS.

```sh
java -jar target/minecraft-server-inserter-1.1.1.jar
```

[Changelog](CHANGELOG.md) | [MIT license](LICENSE)
