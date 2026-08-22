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

## Architecture

```
uhc/
├── api/            → dev.kurai.uhc.* public interfaces & models (published as a library)
├── engine/          → UHC-Engine, the Spigot plugin implementing the api
│   └── UltraHardcorePlugin  → JavaPlugin entry point, delegates to UltraHardcoreEngine/UltraHardcoreAPI
├── extensions/
│   └── mumble-extension/  → Mumble-Extension, depends on UHC-Engine at runtime
└── build-logic/    → included build with shared Gradle convention plugins
```

Key subsystems implemented in the engine (mirrored as contracts in `api`):

- **Game lifecycle** — cycle/episode management, start sequencing, scatter (player spreading), disconnect handling,
  death handling, host controls, win conditions.
- **Rules & scenarios** — a `GameRuleService` for toggleable game rules and a `ScenarioService` for UHC scenarios, both
  configurable through in-game menus (`GameRulesMenu`, `ScenarioMenu`).
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

## Tech Stack

- **Java 25** (via Gradle toolchains) — `api` module
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

- JDK 25 (for building; Gradle toolchains will provision it if not already installed)
- A Spigot/Paper 1.8.8-compatible server for running the built plugin
- The [PacketEvents](https://github.com/retrooper/packetevents) plugin installed on the server (declared as a `depend`
  in `plugin.yml`)

## Building

The project uses the Gradle wrapper, so no local Gradle install is required.

```bash
# Build everything
./gradlew build

# Build just the shaded engine plugin jar
./gradlew :engine:shadowJar

# Build a specific extension
./gradlew :extensions:mumble-extension:build
```

Build artifacts:

- `engine/build/libs/uhc-engine.jar` — the shaded `UHC-Engine` plugin (from `shadowJar`)
- `extensions/mumble-extension/build/libs/` — the `Mumble-Extension` plugin jar
- `api/build/libs/` — the `api` library jar (plus sources/javadoc jars, since it's published)

## Running

1. Build `:engine:shadowJar` and drop the resulting jar into your server's `plugins/` folder.
2. Install [PacketEvents](https://github.com/retrooper/packetevents).
3. Start the server once to generate `config.yml` (spawn location) under `plugins/UHC-Engine/`.

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

`api` and each extension apply `uhc.maven-publishing-conventions` and publish a `maven` publication under group
`dev.kurai.uhc` (extensions under `dev.kurai.uhc.extension`), versioned from `gradle.properties`.

## License

No license file is currently included in this repository. All rights reserved unless stated otherwise.
