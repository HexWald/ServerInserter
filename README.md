# 🧩 Minecraft Server Inserter

> GUI tool for importing Minecraft multiplayer servers from a text file into `servers.dat`.

It is for the boring part: take a list, preview it, back up the old file, and write the entries in Minecraft's NBT format.

---

## ✨ Features

* 🎨 **FlatLaf UI**
* 🌙 **Dark / Light theme toggle**
* 📂 **Drag & Drop** support
* 👀 **Preview servers** before importing
* ✅ **Format validation**
* 🛟 **Automatic backup** before editing an existing `servers.dat`
* 🔁 **Duplicate IP skip** during insert
* 💻 **Cross-platform**

  * Windows
  * Linux
  * macOS
* 📊 **Sortable server table**
* 🔽 **Auto-scroll** for large lists

---

## 🎯 Motivation

This project started while testing a Minecraft server scanner.

The scanner could collect names and addresses, but adding everything to Minecraft by hand was annoying. ServerInserter handles that last step without making you edit `servers.dat` yourself.

---

## ⚙️ How It Works

The application:

1. Reads a `servers.txt` file
2. Parses server entries
3. Inserts them into Minecraft’s `servers.dat` file
4. Uses **NBT format** for full compatibility

---

## 📝 servers.txt Format

Each line must follow this format:

```
Server Name;IP Address
```

Blank lines and lines starting with `#` are ignored, so you can keep short notes in the file.

### Example

```txt
Hypixel;mc.hypixel.net
My SMP;play.mysmp.org
Local Server;127.0.0.1
```

---

## 📁 Default Minecraft Paths

| OS          | Path                                                  |
| ----------- | ----------------------------------------------------- |
| **Windows** | `%APPDATA%\.minecraft\servers.dat`                    |
| **Linux**   | `~/.minecraft/servers.dat`                            |
| **macOS**   | `~/Library/Application Support/minecraft/servers.dat` |

---

## 🚀 Installation

### Requirements

* Java **17+**
* No local Maven install required. The repository includes Maven Wrapper.

### Build

```bash
./mvnw clean package
```

On Windows:

```bat
mvnw.cmd clean package
```

### Run

```bash
java -jar target/minecraft-server-inserter-1.0.0.jar
```

---

## 🧭 Usage

1. Select or **drag & drop** `servers.txt`
2. Select Minecraft `servers.dat`
3. Click **Load Preview**
4. Review the server list
5. Click **Insert Servers**

---

## 📦 Dependencies

* **FlatLaf** — Swing look and feel
* **OpenNBT** — NBT file handling

---

## ⚠️ Disclaimer

This tool **modifies Minecraft’s `servers.dat` file**. If the file already exists, the app creates a timestamped `.bak` file next to it before writing changes.

---

## 🖼 Screenshot

<img width="892" height="646" alt="Screenshot" src="https://github.com/user-attachments/assets/3195d040-48ed-47de-a19d-6a0e4b1e63a8" />

---

## 📄 License

MIT License
