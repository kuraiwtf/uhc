# Changelog

## [1.6.0](https://github.com/kuraiwtf/uhc/compare/v1.5.0...v1.6.0) (2026-09-04)


### Features

* broadcast message when player is added to the game via late scatter ([df4ecbd](https://github.com/kuraiwtf/uhc/commit/df4ecbd44c84b5a26a4f405cf517f0a871d978f7))
* implement late scatter functionality for handling late-joining players ([f7189a0](https://github.com/kuraiwtf/uhc/commit/f7189a027375e0b7a8f1f415fa96570135e581ec))

## [1.5.0](https://github.com/kuraiwtf/uhc/compare/v1.4.2...v1.5.0) (2026-09-04)


### Features

* add `ItemFlag` support to `ScenarioButton` for improved item customization ([33d4b8e](https://github.com/kuraiwtf/uhc/commit/33d4b8e80b85087df7c26b34533716d578b767e5))
* add `MISSING_PERMISSION` constant for permission-related messages in `CC` utility class ([c038a4d](https://github.com/kuraiwtf/uhc/commit/c038a4dbad2edd6ad58a8563ae8950bf58015588))
* add default lore provider method in `AbstractScenario` ([5cbc708](https://github.com/kuraiwtf/uhc/commit/5cbc70808afc3950da366f0f277d07a39b600ad7))
* add descriptions to subcommands in `AnswerCommand` and `GroupCommand` for better clarity ([6add019](https://github.com/kuraiwtf/uhc/commit/6add0193292a814805bd533494d344d51bfdb7af))
* add lore definitions for default scenarios using the new `provideLore` method ([f8dced3](https://github.com/kuraiwtf/uhc/commit/f8dced3684f977a2df8e015d26d0fad532cfe060))
* add permission requirements to various commands and their subcommands ([86cb067](https://github.com/kuraiwtf/uhc/commit/86cb0677488c55dea7f0424675e3a3d9b29369ef))
* add permission support and CommandSender usage to `WhitelistCommand` subcommands ([df28a35](https://github.com/kuraiwtf/uhc/commit/df28a35658dc8db38fca2a0919bb51b580f363f0))
* annotate `command` and `command.help` packages with `@NullMarked` for null-safety compliance ([08112a7](https://github.com/kuraiwtf/uhc/commit/08112a762f70305ad97b5375e8e6d867b236a5a2))
* enhance `ActionbarService` with customizable join configuration and per-tick update frequency ([235a669](https://github.com/kuraiwtf/uhc/commit/235a6691ec957cdcf8b91abbd25b00f4ec672108))


### Bug Fixes

* adjust lore color formatting in `ScenarioButton` for consistency with theme ([eeeb919](https://github.com/kuraiwtf/uhc/commit/eeeb919f2814628ca15d48d5ed319ada7c20357e))


### Code Refactoring

* enhance `unregisterPower` to handle `AbstractItemPower` and `Listener` cleanup ([18ac22e](https://github.com/kuraiwtf/uhc/commit/18ac22e3d5295f862dd120b2d7ab008d67bf8c88))
* extract `ScenarioButton` to a dedicated class and replace inline implementations in scenario menus ([8af3f4c](https://github.com/kuraiwtf/uhc/commit/8af3f4c93fdc0a48df5fcd743a4ea72db40ae327))
* extract death processing logic to `BuiltinDeathProcessor` to improve modularity and readability ([8ca573f](https://github.com/kuraiwtf/uhc/commit/8ca573fd9ff23d90d6a5edec5fe3c90759b4ddcb))
* replace hardcoded permission messages with `MISSING_PERMISSION` constant in commands ([f1118c0](https://github.com/kuraiwtf/uhc/commit/f1118c08b3b58afa3f9b49d805b5b242954d5283))
* simplify `WinCelebration` interface by removing generics and update related implementations ([99e6075](https://github.com/kuraiwtf/uhc/commit/99e6075003b0fc12adf6e99b0dc3286ab7ffb599))

## [1.4.2](https://github.com/kuraiwtf/uhc/compare/v1.4.1...v1.4.2) (2026-09-02)


### Code Refactoring

* remove player notification and sound from `PvPTimer`'s onEnd method to simplify implementation ([6a07d22](https://github.com/kuraiwtf/uhc/commit/6a07d222d36d229bdf790b6a88808e8c17a59a51))

## [1.4.1](https://github.com/kuraiwtf/uhc/compare/v1.4.0...v1.4.1) (2026-09-02)


### Code Refactoring

* rename `registerEntry` and `unregisterEntry` methods to `registerActionbarEntry` and `unregisterActionbarEntry` respectively; update dependencies and Gradle version ([dcf402a](https://github.com/kuraiwtf/uhc/commit/dcf402a9b1b3c7764e9ae86305adf43eedf3ef62))

## [1.4.0](https://github.com/kuraiwtf/uhc/compare/v1.3.0...v1.4.0) (2026-09-02)


### Features

* add `findRule` method to `GameRuleService` for querying rules by identifier ([d88b042](https://github.com/kuraiwtf/uhc/commit/d88b042687f271efed388fa23357adce3a8c8688))

## [1.3.0](https://github.com/kuraiwtf/uhc/compare/v1.2.0...v1.3.0) (2026-09-02)


### Features

* automate releases and publishing via GitHub Actions and Release Please integration ([b6dec04](https://github.com/kuraiwtf/uhc/commit/b6dec0423f131f3b816f2bb5f664208d4e01e53e))
