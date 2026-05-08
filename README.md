# 🧩 Minecraft Server Inserter

> **Modern GUI tool** for importing Minecraft multiplayer servers from a text file into `servers.dat`.

A small quality-of-life application that helps you quickly import large server lists into Minecraft without manual editing.

---

## ✨ Features

* 🎨 **Modern UI** (FlatLaf)
* 🌙 **Dark / Light theme toggle**
* 📂 **Drag & Drop** support
* 👀 **Preview servers** before importing
* ✅ **Format validation**
* 🛟 **Automatic backup** before editing an existing `servers.dat`
* 💻 **Cross-platform**

  * Windows
  * Linux
  * macOS
* 📊 **Sortable server table**
* 🔽 **Auto-scroll** for large lists

---

## 🎯 Motivation

This project started **just for fun**.

While experimenting with a Minecraft server scanner that parsed server names and IP addresses, I realized it would be useful to **import those results directly into Minecraft** instead of adding servers manually.

What began as a simple script evolved into a full GUI application focused on convenience and usability.

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
* Maven

### Build

```bash
mvn clean package
```

### Run

```bash
java -jar target/minecraft-server-inserter.jar
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

* **FlatLaf** — modern Swing look & feel
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
