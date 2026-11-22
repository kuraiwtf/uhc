# UHC

A modular Spigot/Bukkit plugin suite for running **Ultra Hardcore (UHC)** Minecraft matches, built as a multi-module
Gradle project with a clean API/engine split and a pluggable extension system.

## Overview

The project is split into three kinds of modules:

- **`api`** — the public contract of the plugin: interfaces and models for game state, rules, scenarios, teams, timers,
  the ECS-style entity/component system, menus, scoreboards, tablist, whitelist, world handling, and more. Extensions
  and other integrations are meant to compile against this module only.
- **`engine`** — the concrete implementation (`UHC-Engine`), i.e. the actual Spigot plugin that implements everything
  declared in `api`: commands, listeners, menus, game rule/scenario services, scoreboard/tablist rendering, whitelist
  handling, etc. This is the plugin you deploy on a server.
- **`extensions/*`** — optional add-on plugins that depend on `api` (and at runtime on `engine`) to extend UHC
  functionality. Currently includes:
    - **`mumble-extension`** — integrates a Mumble voice-proximity channel system into games (channels, join listeners,
      in-game item to manage it).
    - **`minigame-extension`** — lets players challenge each other to menu-based duels (see
      [Minigames](#minigames)).

## Architecture

```
uhc/
├── api/            → dev.kurai.uhc.* public interfaces & models (published as a library)
├── engine/         → UHC-Engine, the Spigot plugin implementing the api
│   └── UltraHardcorePlugin  → JavaPlugin entry point, delegates to UltraHardcoreEngine/UltraHardcoreAPI
├── extensions/
│   ├── mumble-extension/    → Mumble-Extension, depends on UHC-Engine at runtime
│   └── minigame-extension/  → MiniGame-Extension, depends on UHC-Engine at runtime
├── libs/           → vendored jars (MenuAPI, bundled into the api jar)
└── build-logic/    → included build with shared Gradle convention plugins
```

Key subsystems implemented in the engine (mirrored as contracts in `api`):

- **Game lifecycle** — cycle/episode management, start sequencing, scatter (player spreading), disconnect handling,
  death handling, host controls, win conditions.
- **Rules & scenarios** — a `GameRuleService` for toggleable game rules and a `ScenarioService` for UHC scenarios, both
  configurable through in-game menus (`GameRulesMenu`, `ScenarioConfigurationMenu`).
- **Teams & groups** — team assignment, group management, and related menus.
- **Modules** — pluggable systems such as camp detection, roles, "power" mechanics, and timers.
- **Player profile** — per-player state/profile tracking with actions and components.
- **UI** — menu-driven configuration (`ConfigurationMenu` and friends), scoreboard (objective/sidebar/team), tablist
  adapters/updaters.
- **World & whitelist** — world border/setup utilities and a host-side player whitelist (including HostMC lookups).
- **Commands** — an annotation-driven command framework (`api/command/annotation`) with argument parsing, defaults, and
  generated help.

Convention plugins in `build-logic` centralize shared repositories (`uhc.base-conventions`), Java library setup
(`uhc.java-library-conventions`), extension packaging (`uhc.extension-conventions`), and Maven publishing
(`uhc.maven-publishing-conventions`) across all modules.

## Minigames

`minigame-extension` adds 1v1 duels played entirely in menus.

| Command                 | Description                                           |
|-------------------------|-------------------------------------------------------|
| `/duel <player>`        | Pick a game and send a duel request to a player       |
| `/duel-accept <player>` | Accept that player's request (also clickable in chat) |

Requests expire after 30 seconds and can't be accepted while the challenger is already in a duel.

| Game                | In-game name           | Rules                                                                                   |
|---------------------|------------------------|-----------------------------------------------------------------------------------------|
| Four in a row       | Puissance 4            | 9×5 grid. Click anywhere in a column to drop a token, align 4 in any direction to win.  |
| Tic-tac-toe         | Morpion                | 3×3 grid, align 3 to win.                                                               |
| Rock paper scissors | Pierre Feuille Ciseaux | Both players choose within 10 seconds. A player who didn't choose loses to one who did. |

- The first player is random in four in a row and tic-tac-toe. A full grid without an alignment is a draw.
- Closing the menu or disconnecting during a game forfeits it. Anything that closes the inventory server-side
  (teleport, death, scatter at game start) counts as closing the menu.
- When a game ends, the menu stays open for 3 seconds. On a win, the winning line or choice glows.

## Tech Stack

- **Java 25** — all modules (`api` pins it with a Gradle toolchain)
- **Kotlin DSL** for all Gradle build scripts
- **Spigot API** `1.8.8-R0.1-SNAPSHOT`
- **Lombok** for boilerplate reduction
- **Adventure** (`adventure-platform-bukkit`, MiniMessage) for text/UI components
- **PacketEvents** for low-level packet manipulation
- **Jackson** (core, databind, jdk8, jsr310) for JSON/config serialization
- **fastutil** for primitive collections
- **Apollo API** (Lunar Client) integration
- **MenuAPI**, **EntityLib**, **actionbar-api** for GUI menus, NPC entities, and action bars

## Requirements

- JDK 25 to build, installed locally and used to run Gradle (no toolchain download repository is configured)
- A Spigot/Paper 1.8.8-compatible server able to run on Java 25, as the plugins are compiled to Java 25 bytecode
- The [PacketEvents](https://github.com/retrooper/packetevents) plugin installed on the server (declared as a `depend`
  in `plugin.yml`)
- Optional: Apollo-Bukkit, for Lunar Client features (declared as a `softdepend`)

## Building

The project uses the Gradle wrapper, so no local Gradle install is required.

```bash
# Build everything
./gradlew build

# Build just the shaded engine plugin jar
./gradlew :engine:shadowJar

# Build a specific extension
./gradlew :extensions:mumble-extension:build
./gradlew :extensions:minigame-extension:build
```

Build artifacts:

- `engine/build/libs/uhc-engine.jar` — the shaded `UHC-Engine` plugin (from `shadowJar`). Don't deploy the
  `engine-<version>.jar` next to it: it doesn't bundle the api or its dependencies.
- `extensions/mumble-extension/build/libs/mumble-extension-<version>.jar` — the `Mumble-Extension` plugin jar
- `extensions/minigame-extension/build/libs/minigame-extension-<version>.jar` — the `MiniGame-Extension` plugin jar
- `api/build/libs/` — the `api` library jar (plus sources/javadoc jars, since it's published)

## Running

1. Build `:engine:shadowJar` and drop the resulting jar into your server's `plugins/` folder.
2. Install [PacketEvents](https://github.com/retrooper/packetevents).
3. Optionally drop any extension jar into `plugins/` as well.
4. Start the server once to generate `config.yml` (spawn location) under `plugins/UHC-Engine/`.

### Configuration

`engine/src/main/resources/config.yml` defines the lobby spawn point and the authorization key for
the [HostMC](https://docs.bot-mc.fr/) automatic whitelist support:

```yaml
spawn:
  world: world
  x: 197.5
  y: 33
  z: 214.5
  yaw: 90.0
  pitch: 0.0

authorization: "YOUR_HOSTMC_AUTHORIZATION_KEY"
```

## Publishing

`api` and each extension apply `uhc.maven-publishing-conventions` and declare a `maven` publication, versioned from
`gradle.properties`:

| Module         | Coordinates                                  | Remote repository |
|----------------|----------------------------------------------|-------------------|
| `api`          | `dev.kurai.uhc:uhc-api:<version>`            | GitHub Packages   |
| `extensions/*` | `dev.kurai.uhc.extension:<module>:<version>` | GitHub Packages   |

The GitHub Packages repository is declared in `uhc.maven-publishing-conventions`, so new extensions are published
automatically. Extension artifacts don't pull in `uhc-api`: declare it alongside them.

`uhc-api` bundles the MenuAPI build from `libs/`, so don't add `net.j4c0b3y:MenuAPI-core` next to it. The official
`1.5.5` artifact differs from that build (`MenuSize.ONE` and `TWO` don't exist in the bundled one).

Versions and the changelog are managed by release-please. Publishing a GitHub release builds the project, runs
`./gradlew publish` and attaches the engine and extension jars to the release.

## License

No license file is currently included in this repository. All rights reserved unless stated otherwise.
