# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build everything (produces shaded jar in engine/build/libs/)
./gradlew shadowJar

# Build without shading (for API publishing)
./gradlew :api:build

# Publish API to local Maven repository
./gradlew :api:publishToMavenLocal

# Clean
./gradlew clean
```

There are no tests in this project.

## Project Structure

Two-module Gradle project (Kotlin DSL, Java 23):

- **`api/`** — Public contracts: abstract service interfaces, annotations, ECS base classes, data models. Published as a
  Maven artifact. Has no implementation code.
- **`engine/`** — Bukkit plugin implementation. Depends on `api`. The shadow jar produced here is the deployable plugin.
- **`build-logic/`** — Shared Gradle convention plugins (`uhc.base-conventions`, `uhc.java-library-conventions`,
  `uhc.maven-publishing-conventions`).

Group ID: `dev.kurai.uhc` (root) → `dev.kurai.uhc.api` / `dev.kurai.uhc.engine` per module.

## Architecture

### Entry Point & Wiring

`UltraHardcorePlugin` (engine, extends `JavaPlugin`) delegates all lifecycle to `UltraHardcoreEngine`, which extends the
abstract `UltraHardcoreAPI`. The API class holds the singleton instance and declares every service as an abstract
getter. `UltraHardcoreEngine.onEnable()` is where all services are instantiated and wired together — it is the
composition root.

To access any service from anywhere: `UltraHardcoreAPI.getInstance().get<X>Service()`.

### Service Pattern

Every subsystem follows this pattern:

- **`api/`** declares the interface (e.g., `GameService`)
- **`engine/`** provides the implementation (e.g., `GameServiceImpl`)
- Services are instantiated once in `UltraHardcoreEngine.onEnable()` and referenced only through the interface

### Annotation-Based Command System

Commands are registered via `CommandRegistrar.registerCommands(Object...)`. The registrar uses reflection to scan for:

- `@Command(@CommandMeta(...))` on a **class** → parent command with sub-commands
- `@Command(@CommandMeta(...))` on a **method** → orphan (standalone) command
- `@SubCommand(@CommandMeta(...))` on **methods** within a `@Command` class → sub-commands

Method signatures follow this contract:

- First parameter: `CommandSender` or `Player` (auto-enforced)
- Remaining parameters: annotated with `@Argument(name = "...")` and resolved via registered `ArgumentResolver<T>`
  instances
- Array parameters (e.g., `int[]`, `Player[]`) consume all "middle" args, with any parameters after the array consumed
  from the end of the arg list

**Custom argument types** are registered after `CommandRegistrar` is created:

```java
commandRegistrar.getArgumentResolverRegistrar()
    .

registerArgumentResolver(MyType .class, new MyArgumentResolver(...));
```

Built-in resolvers: `String`, `int`/`Integer`, `boolean`/`Boolean`, `double`/`Double`, `long`/`Long`, `Player`, `World`,
`AbstractTimer`.

### ECS (Entity Component System)

The `ecs` package in `api/` provides base `Entity` and `Component` abstractions used to attach behaviour to game
entities (e.g., players). Components are retrieved from their entity by type.

### Module System

`AbstractModule` (api) represents game scenarios/modifiers. Modules are registered through `ModuleService` and can
expose commands, listeners, and configuration. `ModuleServiceImpl` manages their lifecycle.

### Text & Messaging

All user-facing text uses the **Adventure API** (`net.kyori.adventure`). Access the `Audience` for a sender via
`UltraHardcoreAPI.getInstance().getBukkitAudiences().sender(sender)`. The `CC` utility class provides the standard
message prefix via `CC.prefix()`.

### Async Tasks

Two repeating async tasks run at 1-tick intervals:

- `ActionbarUpdaterTask` — pushes actionbar entries to players
- `TabListUpdaterTask` — updates the tab list

Both are registered in `UltraHardcoreEngine.onEnable()` via `Bukkit.getScheduler().runTaskTimerAsynchronously(...)`.

## Code Style

- **Minimal comments**: Do not add comments unless absolutely necessary. The codebase uses strong OOP, abstraction, and
  modular design - code should be self-explanatory through good naming and structure.
- Only add comments for truly complex algorithms or non-obvious business logic.
