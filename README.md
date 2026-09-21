# Minecraft Server Inserter

A small Java desktop tool for adding Minecraft servers from a text file to `servers.dat`.

It reads a list, lets you check it in a table, creates a backup when needed, and writes the result as NBT. You do not need to edit `servers.dat` by hand.

## Features

- preview the list before changing anything;
- sort the preview table by name or address;
- load files through a file chooser or drag and drop one file at a time;
- accept semicolon or comma separated input;
- ignore blank lines and comments starting with `#`;
- skip addresses that are already in `servers.dat`;
- create a timestamped backup before changing an existing file;
- use a dark or light theme;
- run on Windows, Linux, and macOS.

## Requirements

- Java 17 or newer;
- no separate Maven installation is needed because the Maven Wrapper is included.

## Quick start

Build the application from the repository root:

```bash
./mvnw clean package
```

On Windows:

```bat
mvnw.cmd clean package
```

Run the packaged application:

```bash
java -jar target/minecraft-server-inserter-1.0.0.jar
```

## How to use it

1. Select or drop your input text file.
2. Select the Minecraft `servers.dat` file, or use the default path button.
3. Click `Load Preview` and check the table.
4. Click `Insert Servers`.

When an existing `servers.dat` is changed, the old file is copied next to it with a timestamp and the `.bak` extension.

## Input format

Use one server per line. Separate the name and address with `;` or `,`:

```text
Hypixel;mc.hypixel.net
My SMP;play.mysmp.org
Practice Server,practice.example.org
Local Server;127.0.0.1
```

Blank lines and comments are ignored:

```text
# personal favorites
Hypixel;mc.hypixel.net
```

The parser splits at the first separator. Server names containing `;` or `,` are not supported by this simple format.

## Default Minecraft paths

- Windows: `%APPDATA%\\.minecraft\\servers.dat`
- Linux: `~/.minecraft/servers.dat`
- macOS: `~/Library/Application Support/minecraft/servers.dat`

The default button uses these paths. If you use a custom launcher profile or game directory, select `servers.dat` manually.

## What gets changed

Only the `servers` list in the selected NBT file is updated. Existing entries and their other fields are kept. New entries contain a name and an address.

If the address is already present, it is skipped. Duplicate checks ignore domain letter case, a final domain dot, and the default `25565` port. Bracketed and plain IPv6 addresses are also compared correctly.

## Version history

### Unreleased changes after v1.0.0

- comma separated input is supported in addition to semicolon separated input;
- duplicate addresses are skipped without creating an unnecessary backup;
- repository documentation and issue templates were cleaned up.

### v1.0.0

First stable release. It introduced the Maven Wrapper, background UI actions, status messages, file chooser filters, the default path button, automated tests, and the shaded release JAR.

The complete list is in [CHANGELOG.md](CHANGELOG.md).

## Project files

- `src/main/java` contains the application code;
- `src/test/java` contains parser and NBT writing tests;
- `pom.xml` defines the build and dependencies;
- `.github/workflows/build.yml` runs tests and packages the JAR.

## License

MIT. See [LICENSE](LICENSE).
