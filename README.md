# BorderedSMP

![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white)
![Paper](https://img.shields.io/badge/Paper-1.26.2-blue?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCI+PHBhdGggZmlsbD0id2hpdGUiIGQ9Ik0xMyAyLjA1djIuMDJjMy45NS41NCA3IDMuOTkgNyA4LjQ4YzAgMy42MS0yLjAxIDYuNzMtNS4wMyA4LjE3TDEzIDE4LjdWMjNsOC04LTgtOHY0LjA2YzIuOTctMS40OSA1LTQuNSA1LTcuOTljMC00Ljk5LTQtOC45My05LTguOTJ6bS0yIDBDNi4wNSAyLjA1IDIgNiAyIDExYzAgMy41IDIuMDMgNi41IDUgOC4wNlY0LjkzQzQuMDMgNi40NiAyIDguNzIgMiAxMWMwIDMuMyAyLjM3IDYuMDYgNS41NiA2Ljc5TDkgMjNsLTcuNS04LjVMMiAxM3YtMWMwLTQuNDkgMy4wNi03LjkzIDctOC40OHoiLz48L3N2Zz4=&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green)
![Build](https://img.shields.io/badge/Build-Gradle-02303A?logo=gradle&logoColor=white)

A hardcore SMP plugin that raises the stakes on every death. Lives are finite, the world border shrinks, and revival requires crafting — not commands.

---

## Features

### Lives System
Players start with a configurable number of lives. Every death removes one life. Reaching zero results in an automatic ban until another player revives them.

### Border System
Every death shrinks the world border by a random amount within a configurable range. A Wither spawn sound plays globally whenever the border changes.

### Revive System
Players can craft a **Revive Token** using a configurable recipe. Right-clicking the token prompts for a banned player's name, consuming the token and restoring their lives.

---

## Commands

All commands require the `borderedsmp.admin` permission.

### `/lives`
| Subcommand | Description |
|---|---|
| `set <player> <amount>` | Set a player's lives to an exact value |
| `add <player> <amount>` | Add lives to a player |
| `remove <player> <amount>` | Remove lives from a player |
| `check <player>` | Check how many lives a player has |

### `/border`
| Subcommand | Description |
|---|---|
| `setrange <min> <max>` | Set the random shrink range (in blocks) per death |

### `/revive`
| Subcommand | Description |
|---|---|
| `toggle` | Enable or disable the revive system |
| `reload` | Reload the crafting recipe from `config.yml` |

---

## Configuration

### `config.yml`

```yaml
border:
  enabled: true
  range:
    min: 1       # Minimum blocks removed per death
    max: 10      # Maximum blocks removed per death
  sound:
    volume: 1.0  # Volume of the Wither spawn sound

revive:
  enabled: true
  lives-to-restore: 3
  recipe:
    result-material: TOTEM_OF_UNDYING
    shape:
      - "DGD"
      - "GEG"
      - "DGD"
    ingredients:
      D: DIAMOND
      G: GOLD_INGOT
      E: EMERALD
```

### `lives.yml`

```yaml
default-lives: 3   # Lives each new player starts with
players: {}        # Per-player live counts (managed automatically)
```

---

## Permissions

| Node | Description |
|---|---|
| `borderedsmp.admin` | Access to all `/lives`, `/border`, and `/revive` commands |

---

## Installation

1. Download the latest `.jar` from [Releases](https://github.com/Glatinis/BorderedSMP/releases)
2. Place it in your server's `plugins/` folder
3. Restart the server — default configs are generated automatically
4. Edit `plugins/BorderedSMP/config.yml` and `lives.yml` to your liking

**Requirements:** Paper 1.26.2+ · Java 25+

---

## Building from Source

```bash
git clone https://github.com/Glatinis/BorderedSMP.git
cd BorderedSMP
./gradlew build
```

The output jar will be in `build/libs/`.

To run a local test server:

```bash
./gradlew runServer
```

---

## License

MIT © [Glatinis](https://github.com/Glatinis)
