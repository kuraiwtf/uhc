# Changelog

## [1.20.0](https://github.com/kuraiwtf/uhc/compare/v1.19.0...v1.20.0) (2026-11-26)


### Features

* add usage provider for command argument errors ([69a0f44](https://github.com/kuraiwtf/uhc/commit/69a0f4401cd68173206c215975ab232609688e21))
* fake enchantment glint on other players' equipment ([7cc71a5](https://github.com/kuraiwtf/uhc/commit/7cc71a5a0bd0ffba7229a02af04303228123a1b8))
* notify helpop participants and whois target ([e27bb8e](https://github.com/kuraiwtf/uhc/commit/e27bb8e7d5bd53b05a8d7846160948e2ecd6c500))


### Bug Fixes

* avoid async tab list and effect tasks racing shared state ([9487ef4](https://github.com/kuraiwtf/uhc/commit/9487ef42eeb31820db8e5dade7a2d5612862a0bb))
* use unloadChunk with save and force flags in world preload ([ad9b526](https://github.com/kuraiwtf/uhc/commit/ad9b526176a9d7b3d39c251d8be172a3d08682f0))


### Code Refactoring

* reuse usage provider for command argument errors ([a4f2e0c](https://github.com/kuraiwtf/uhc/commit/a4f2e0ca65524107d48f68cc3c091c0034ff56af))

## [1.19.0](https://github.com/kuraiwtf/uhc/compare/v1.18.0...v1.19.0) (2026-11-22)


### Features

* add decrement event to power cooldowns ([175c2ee](https://github.com/kuraiwtf/uhc/commit/175c2ee153648bb2b964dc0b35ebf0fd4448979c))
* add editMeta helpers to ItemBuilder ([98f430e](https://github.com/kuraiwtf/uhc/commit/98f430e55147c5bc03a02cfe5eef52aadf7812cf))
* add effect lookup by key and type, and per-type removal ([78def1a](https://github.com/kuraiwtf/uhc/commit/78def1a8b6e1e8577bd3826772e1d31d381f4109))
* add formatGameMode helper to CC ([197caca](https://github.com/kuraiwtf/uhc/commit/197caca4831cb88ef2a272e72d00beddc4d1a071))
* add GameMode argument resolver ([00004b8](https://github.com/kuraiwtf/uhc/commit/00004b8b52e52e9473ebf7a8cce054ef0916fa58))
* add gamemode host command ([118778c](https://github.com/kuraiwtf/uhc/commit/118778cc1bf730d435b20123370a339530a74684))
* add lore to VisibleEffectScenario ([40380a0](https://github.com/kuraiwtf/uhc/commit/40380a0664125285f8875ce579a7b502f515d1a4))
* add minigame extension with duel system ([7e991c3](https://github.com/kuraiwtf/uhc/commit/7e991c39acbba971e8f934c8cdb3dda7d7dee109))
* add profile state to Profile API and ProfileImpl ([a06c887](https://github.com/kuraiwtf/uhc/commit/a06c88792f11f8c81f6d960dd7d0aea31a6dcdfb))
* add VisibleEffectScenario to show active effects in tab list ([27b4a9e](https://github.com/kuraiwtf/uhc/commit/27b4a9e8d61e0e4bcf78811fd64b840fc1aafef7))
* expose Profile name setter in API ([f561670](https://github.com/kuraiwtf/uhc/commit/f561670f55a8ecb07a727e960829546726ea5630))
* highlight already-applied colors in color menu ([d1c3df4](https://github.com/kuraiwtf/uhc/commit/d1c3df42ad4985dbf0e0eab227b9f397195da42c))
* implement Keyed on Effect ([4f7f6d9](https://github.com/kuraiwtf/uhc/commit/4f7f6d9a58d7c63ded597703e7f8c6dbe0c8a5ad))
* implement Listener on VisibleEffectScenario ([6979edb](https://github.com/kuraiwtf/uhc/commit/6979edb7511b503c1193ddca7ec730a3a8e6b447))
* reset chat option when PvP timer ends ([6e0f273](https://github.com/kuraiwtf/uhc/commit/6e0f273dc3115ccd6b16d32a0b986f42705a7769))


### Bug Fixes

* process item powers through their current power in cooldown updater ([41ea5fa](https://github.com/kuraiwtf/uhc/commit/41ea5fa90bb8a12cf45ef31d61e0c453b3eb5684))
* resolve power cooldown restriction by power id in development command ([7428231](https://github.com/kuraiwtf/uhc/commit/742823130c29d24e026a3c00d8a70ac63c0c5f47))
* resolve profile name from UUID via Bukkit and use entity id ([8970a11](https://github.com/kuraiwtf/uhc/commit/8970a11a30b4681c9f55fdfc6f5cc784b1cb424e))
* use item power identifier for cooldown restriction lookup ([2321afc](https://github.com/kuraiwtf/uhc/commit/2321afc182ca55ba3abd7968399fc4524eb2c65b))
* validate effect key and type in removeEffect ([2184604](https://github.com/kuraiwtf/uhc/commit/2184604fb2819735a901878b54f8217b9707586f))


### Code Refactoring

* add ProfileState enum and ProfileStateComponent ([8cd650b](https://github.com/kuraiwtf/uhc/commit/8cd650bb3337ae7f38eddd590bb85a59c875df0f))
* convert Effect to record and rename accessors ([e554c84](https://github.com/kuraiwtf/uhc/commit/e554c846cda14b1eb99d7a4fcd1c2c77c954ee70))
* drop unused ModuleService from AccessListener ([251710f](https://github.com/kuraiwtf/uhc/commit/251710f93364885ff5e1978213763e083b40e66e))
* make EntityBridge generic over id type only ([cad5cce](https://github.com/kuraiwtf/uhc/commit/cad5cce5f6b21c738592a6087bcf0c8dcad5b74e))
* move NullMarked to package-info across engine packages ([6a98108](https://github.com/kuraiwtf/uhc/commit/6a9810832198ec74f7aa206dff853016fd637b65))
* move NullMarked to package-info and relocate start types ([0c86e05](https://github.com/kuraiwtf/uhc/commit/0c86e0561fab43d72ff6e85c314b1861791e7878))
* remove unused util API package ([be421bc](https://github.com/kuraiwtf/uhc/commit/be421bc4a71f2cbb9a4fc9b452794461c33026c4))
* rename DeadComponent to DeathRecordComponent ([01e877e](https://github.com/kuraiwtf/uhc/commit/01e877ed370a03e51255ca07313a6e5d5fde34c7))
* rename EffectHolder getEffectValue to effectValue ([02dc251](https://github.com/kuraiwtf/uhc/commit/02dc2510566c51f79a292c3a012162c9c679d2ee))
* rename getEntireHealth to getHealthAndAbsorption ([1900add](https://github.com/kuraiwtf/uhc/commit/1900add9a468dc63dd59731f988e449b5e15ace9))
* rename WhitelistService.isWhitelisted to whitelisted ([d477dee](https://github.com/kuraiwtf/uhc/commit/d477dee41a32c6fb28540080dbb21991a15cba2e))
* replace profile state components with Profile state API ([50c4974](https://github.com/kuraiwtf/uhc/commit/50c4974bf0f3ccec24d1772fe143bb62d5c6354a))
* replace profile state with ECS components ([2489ff1](https://github.com/kuraiwtf/uhc/commit/2489ff112d45773d6ac28c85eeb574204fa48204))
* replace profile state with ECS components ([9067dd8](https://github.com/kuraiwtf/uhc/commit/9067dd8bf0d72a4bab368382042ba05be1f2f8e2))
* use adventure Key for effect identifiers ([fc206c6](https://github.com/kuraiwtf/uhc/commit/fc206c68b9350003e315b7fd83e8721aa43093d7))
* use EffectHolder lookup for effect totals ([f76bc1d](https://github.com/kuraiwtf/uhc/commit/f76bc1d7f9fcb8a95dd1124961c9e5b8a4f996d0))
* use EffectHolder lookup in effect tab list percentage ([30690f7](https://github.com/kuraiwtf/uhc/commit/30690f75ef8d492f6c3ef2ad0186babbff84d320))
* use record component fields in Effect constructor ([2ce3bd1](https://github.com/kuraiwtf/uhc/commit/2ce3bd1f83a1c336ebd17dc22e3237030252dfa6))

## [1.18.0](https://github.com/kuraiwtf/uhc/compare/v1.17.1...v1.18.0) (2026-11-20)


### Features

* add @Nullable annotation to ArgumentResolverRegistrar#resolveArgument for nullability support ([4a1e61f](https://github.com/kuraiwtf/uhc/commit/4a1e61f343e35aa58dd3dbc1540e22d2f2185dc5))
* add `Apollo-Bukkit` as a soft dependency in `plugin.yml` ([0d91548](https://github.com/kuraiwtf/uhc/commit/0d915487f33efa328e7245314005642d66e467e2))
* add `DisconnectMenu` for managing disconnection rules and refactor `DisconnectService` to use seconds-based time logic ([e093ccc](https://github.com/kuraiwtf/uhc/commit/e093ccc7c7ac107b07b59193aef83cb57a45ce0a))
* add `onCycle` method for roles and introduce `disabledPassives` flag ([307bc58](https://github.com/kuraiwtf/uhc/commit/307bc5890a1c3a8b3fd9dd395c4d7f420bbe4bb7))
* add `playing` state management in GameService ([09ca5c2](https://github.com/kuraiwtf/uhc/commit/09ca5c26679b83565d647ce589767556592e8b81))
* add `RoleDescriptionProvider` for dynamic role descriptions and integrate into role management system ([99b0f6a](https://github.com/kuraiwtf/uhc/commit/99b0f6a959f76eef876f2fefd47474882a0642ea))
* add `stop` methods to various game services for improved lifecycle management ([8c72c65](https://github.com/kuraiwtf/uhc/commit/8c72c658a62a1c3e888fd7aaa565c8ae6de2a455))
* add async packet sending through executor in PacketEventsWrapper to prevent blocking operations ([4f9774a](https://github.com/kuraiwtf/uhc/commit/4f9774ad971598aea84a2852b8ee02679658f689))
* add asynchronous task management and nullability annotations in `WhitelistCommand` ([98e5d47](https://github.com/kuraiwtf/uhc/commit/98e5d47cc7c6ee8d793ca41f968fb307e418f450))
* add centralized permission system with PermissionProvider interface and BuiltinPermissionProvider implementation ([a5ba294](https://github.com/kuraiwtf/uhc/commit/a5ba294051ee6a4afb1cab7116d0a900c1243437))
* add combat service and skip players in combat on /heal ([6e0f4ed](https://github.com/kuraiwtf/uhc/commit/6e0f4eda0492df1683d207597d17fa29b04f78e3))
* add configurable Mumble URL support and periodic advertisement task ([2781b80](https://github.com/kuraiwtf/uhc/commit/2781b80b908c9308b3d192a5b72732674b2acd13))
* add cooldown and message length restriction to `HelpOpCommand` ([4095a29](https://github.com/kuraiwtf/uhc/commit/4095a29a994c0f97c7cbd8cb18e6d43de54caa00))
* add default enum argument resolver with per-type override ([0b23521](https://github.com/kuraiwtf/uhc/commit/0b23521c3ef6a7d99551a8793afe0d186c3d79c1))
* add DevelopmentCommands with test command to toggle testMode in GameService ([a8f60b3](https://github.com/kuraiwtf/uhc/commit/a8f60b3786757078ed98c7d10e52caacd505703f))
* add diamond crafting recipe from gold ingot ([3520516](https://github.com/kuraiwtf/uhc/commit/352051697cb66aa4ba4f4292b1fc26dac4ce4b70))
* add disperse command for teleporting players within a specified radius ([2fe412f](https://github.com/kuraiwtf/uhc/commit/2fe412f4239cccada99f1c1d55ec2274433da31f))
* add ECS entity class with component add/remove events ([f5b73d9](https://github.com/kuraiwtf/uhc/commit/f5b73d96892e7912da0ab1047055d5d17d5cd67e))
* add getter and setter for `disabledPassives` in `AbstractRole` ([f385aac](https://github.com/kuraiwtf/uhc/commit/f385aac686d0955ed5e80e15cc4c722d15058bf8))
* add group variable system with dynamic application and periodic updates ([ef9e56f](https://github.com/kuraiwtf/uhc/commit/ef9e56f88d6db57d791763e1e9231e7842d295a4))
* add host access checks and group size validation in `GroupCommand` ([e276ff7](https://github.com/kuraiwtf/uhc/commit/e276ff73679874fb040d0f0552a8b80793eb76ef))
* add modular command registration with dynamic handler support and module-specific commands ([f2c049b](https://github.com/kuraiwtf/uhc/commit/f2c049bbe98544758e9c6169d4a275f89e00e8f9))
* add plugin() accessor to AbstractModule ([8ff7a80](https://github.com/kuraiwtf/uhc/commit/8ff7a80a7ccd6e6ac45103eadb0c4a4ccf5405f1))
* add read-only rules menu to /uhc ([0452fd3](https://github.com/kuraiwtf/uhc/commit/0452fd3e160f763e3829fc5a02b5f1884b6be2e2))
* add role color commands and enchantment limits system ([f705c08](https://github.com/kuraiwtf/uhc/commit/f705c08a20b62fda6f36ef177d7127875f66fd57))
* add support for `otherParticularities` in `RoleDescriptionProvider` with hoverable descriptions and improve component formatting ([1ffdc62](https://github.com/kuraiwtf/uhc/commit/1ffdc62d92a8fed39b99fc185d6c6e09da3b04b5))
* add synchronized `destroy` method to `UltraHardcoreAPI` for instance lifecycle management ([7f0d85f](https://github.com/kuraiwtf/uhc/commit/7f0d85fd112a737d96115f5c653d3adb28d7e8f5))
* add testMode flag to GameService and GameServiceImpl ([9279e55](https://github.com/kuraiwtf/uhc/commit/9279e55b52bbd9ba75c794a5cc12c0ed261d303a))
* add timer running checks, PlayerInformationComponent usage, and host access verification in `HostCommand` methods ([390fb27](https://github.com/kuraiwtf/uhc/commit/390fb27f775e31d5fab6d8e72dfe40c45453512b))
* enable `disconnectService` and `groupService` in game start flow and add `Title` utility for managing player titles ([986ed08](https://github.com/kuraiwtf/uhc/commit/986ed082c6cffbe81b74621a3802fcab6d69db70))
* enhance team logic with new assignment and death message features ([18fb26e](https://github.com/kuraiwtf/uhc/commit/18fb26e2fcb0d5860655078b4ded80471ab1ab82))
* implement chat toggle functionality with ChatListener and host command integration ([d3e2191](https://github.com/kuraiwtf/uhc/commit/d3e219106adb77d1a8c19362cdbd38952e6fab36))
* implement role attribution system with `RoleAttributionTimer` and improve `RoleModule` functionality ([715507d](https://github.com/kuraiwtf/uhc/commit/715507d24f99fcd31d8a5e14ec4026454c613023))
* implement SpectatorVisibility utility for managing spectator player visibility and integrate it across relevant components ([cb57439](https://github.com/kuraiwtf/uhc/commit/cb5743933660871d84f7d01642099e3644d1ccb8))
* implement team management system with full lifecycle support and modular integration ([d641165](https://github.com/kuraiwtf/uhc/commit/d64116570bdeeb70fe6cbd7fee55553e7bbb02ca))
* improve `HostMCWhitelistProviderTask` with error handling, async operations, and safer UUID parsing; replace `HashMap` with `ConcurrentHashMap` in `WhitelistServiceImpl` for thread safety ([278fafc](https://github.com/kuraiwtf/uhc/commit/278fafcc7854d51c029fc20b3f019b92779a2810))
* improve death handling by adding spectator respawn logic and excluding powered items from drops ([b356d4d](https://github.com/kuraiwtf/uhc/commit/b356d4dee44b0cffab9af0ca72871fcd6a3b8589))
* integrate `disconnectService` with player reconnection logic and update `DisconnectMenu` to support new elimination strategies ([91fea81](https://github.com/kuraiwtf/uhc/commit/91fea811e7067295c569e3427e564fa10d415d3e))
* introduce `GameTickEvent` and `GameSecondEvent` to improve tick-based game progression handling ([091db0b](https://github.com/kuraiwtf/uhc/commit/091db0b23986264770f0a9e7a690b8bb20e9635c))
* introduce episode duration customization and timer improvements ([5a68460](https://github.com/kuraiwtf/uhc/commit/5a6846032cf39313c6e60e37872164b3a3acfdf2))
* make EntityBridge return the implementing type from addComponent ([c9c5bc5](https://github.com/kuraiwtf/uhc/commit/c9c5bc52181180dab4a534a142a4f6b48dc77465))
* move resetcd command to DevelopmentCommands and restrict usage to test mode ([eba4811](https://github.com/kuraiwtf/uhc/commit/eba4811dda89904f49381fe9cab2444b60f9cc1d))
* prevent non-player entities from spawning during world preload and re-enable spawning post-load ([c6fd740](https://github.com/kuraiwtf/uhc/commit/c6fd740c54f8efe099e00e415c3638a5107052d2))
* restrict /heal to games in progress ([68cc233](https://github.com/kuraiwtf/uhc/commit/68cc233bdbd0c4cbc5c48635843cbc132f0c9047))
* set default target to "all" for heal command arguments ([106d2c4](https://github.com/kuraiwtf/uhc/commit/106d2c4c8ce532b2ecfc7ebe54600cdfa1298587))
* toggle `playing` state in GameService upon win celebration ([672ad6f](https://github.com/kuraiwtf/uhc/commit/672ad6f9a641471fa97243978fd865909db56f91))
* update skin state after removing a skin ([1b3bdb2](https://github.com/kuraiwtf/uhc/commit/1b3bdb214eab582158459a281dee41a750f280d9))
* update SLOTS_OPTION to use rangedOption with configurable bounds (1-500) ([2ee436b](https://github.com/kuraiwtf/uhc/commit/2ee436b64ac86d38231b4580ee7dd4c5328d1d75))
* use BorderConfiguration for dynamic preload size in ConfigurationMenu onClick ([3feb2d7](https://github.com/kuraiwtf/uhc/commit/3feb2d72a663b0706b4aba24199b5acf5daf3bb9))


### Bug Fixes

* add `stop` method and proper task management in `GroupServiceImpl` and `EpisodeServiceImpl` ([63ba879](https://github.com/kuraiwtf/uhc/commit/63ba87926967de44d49fb4433038224d37b0b428))
* add `stop` method to cancel `DisconnectServiceImpl` tasks and ensure proper task management ([50625a7](https://github.com/kuraiwtf/uhc/commit/50625a7e6f3bf29919060d6045a54eb52aab2aa6))
* add error handling for role attribution in `RoleAttributionTimer` ([582291e](https://github.com/kuraiwtf/uhc/commit/582291ea1de924c0215d69b2c67c24f0f8799f53))
* add group alert functionality in `GroupVariableTask` ([8da76c6](https://github.com/kuraiwtf/uhc/commit/8da76c637c2f039053af54f4cbd103aa4db80594))
* add proper task management in `StartServiceImpl` with `stopGameTasks` method ([2785432](https://github.com/kuraiwtf/uhc/commit/2785432b2edc57576d8b1344ce9edd229a8d4fcd))
* adjust chunk loading loop and refine profile filtering in `WorldPreloadTask` ([3e9947b](https://github.com/kuraiwtf/uhc/commit/3e9947bece429957f01ce9c1d5d7d7c8f74aaa04))
* correct effect value usage in damage calculation, add null check in `validateSpeed`, and ensure thread safety in `EffectHolderImpl` with `ConcurrentHashMap` ([50f7e77](https://github.com/kuraiwtf/uhc/commit/50f7e77ef2da0b1a0cba4ed5d189864e1c632bb8))
* display cycle duration and allow editing it in the rules menu ([39c5e23](https://github.com/kuraiwtf/uhc/commit/39c5e23009a331de4fa502e371ba866ccac5f2a4))
* enforce max stack size limit in ItemBuilder#setAmount ([a0732cc](https://github.com/kuraiwtf/uhc/commit/a0732cc44897fda54d447f6b5bd0206ddb94646e))
* ensure thread safety in host services by adding `volatile` and replacing `HashSet` with `ConcurrentHashMap` ([a01be9b](https://github.com/kuraiwtf/uhc/commit/a01be9b3e67a80976d4080a780ff8f796971925f))
* give the broken log to the player instead of dropping it in TimberScenario ([3f29a11](https://github.com/kuraiwtf/uhc/commit/3f29a11a4d0ceb7a89ff61d34f27a9ffd3433312))
* handle empty cycle phases in `CycleServiceImpl` and add `stop` method to properly cancel active tasks ([dd92360](https://github.com/kuraiwtf/uhc/commit/dd9236033419e37fbd47072184e4e5760808d2db))
* handle null `ItemStack` in tag utility methods to prevent potential NPEs ([adeab07](https://github.com/kuraiwtf/uhc/commit/adeab07e417f9c2eff3a0a8567815d8496650d28))
* handle null check for player in ScatterTask teleport logic ([cf4abe7](https://github.com/kuraiwtf/uhc/commit/cf4abe7c4a65ad632e8cfb652cedd93c8855f1ec))
* improve entity ownership tracking and add thread safety in `PowerListener` ([5648487](https://github.com/kuraiwtf/uhc/commit/5648487d6b35c1ff2e96e01f1493ea435a307a16))
* improve scenario logic and performance in `TimberScenario`, `BatsScenario`, `BetaZombieScenario`, and others ([e039d2a](https://github.com/kuraiwtf/uhc/commit/e039d2a2683ee61b9d0d46316dd64e84a6e09af7))
* improve spectator processing, suffocation handling, and block updates ([8a7466c](https://github.com/kuraiwtf/uhc/commit/8a7466cc2f5c179e6d24558f89e70e3a2b6a5bbb))
* improve thread safety and add batching in `TeamServiceImpl` ([10bcc71](https://github.com/kuraiwtf/uhc/commit/10bcc7111d6a496b55eb2970c3718287d96eaa04))
* improve thread safety and conditional processing in skin services ([5eae606](https://github.com/kuraiwtf/uhc/commit/5eae6064ab124077a19ecb08905b5b5fa21e68bb))
* improve thread safety and performance in `SidebarServiceImpl` and related classes ([e40eac8](https://github.com/kuraiwtf/uhc/commit/e40eac8b2122de508f30cb410a5ee84574ba68f2))
* improve thread safety in `ProfileServiceImpl` and update profile filtering ([89e8ac9](https://github.com/kuraiwtf/uhc/commit/89e8ac9251fe17aab36806e40bda62f3ebdd356a))
* integrate role-specific callbacks in game tasks and cycles ([f779f7b](https://github.com/kuraiwtf/uhc/commit/f779f7b405beb18189ceac158202eb2d567e9aa4))
* only add timer spacing when playing sidebar has timed lines ([68ff611](https://github.com/kuraiwtf/uhc/commit/68ff6110babfc937e27ee87ec7fd4f48a2ee6203))
* only display effect levels that are fully reached ([7866d72](https://github.com/kuraiwtf/uhc/commit/7866d728b472111975a59eabf8a32fe932dc4ef7))
* optimize duration formatting and implement chunk unloading in `WorldPreloadTask` ([b445fff](https://github.com/kuraiwtf/uhc/commit/b445fff06296b5eae835b661e0a872efa6dbf958))
* optimize scheduler intervals and enhance resource cleanup ([0404dc6](https://github.com/kuraiwtf/uhc/commit/0404dc676cbfcf9f1ba9f39cd358c9b5217ebefa))
* periodically refresh client-side effects to prevent flickering ([ec49888](https://github.com/kuraiwtf/uhc/commit/ec4988866973d6cef94232bcc8a5c618734d11a0))
* prevent division by zero in `BorderTimer` by capping minimum shrink speed value ([7151348](https://github.com/kuraiwtf/uhc/commit/71513481869bc655f5f038886c3febf5929f303c))
* refactor login handling and add null checks in event listeners ([bff429e](https://github.com/kuraiwtf/uhc/commit/bff429e4c6459058b9c0468948181b0a9fae3e79))
* remove extra space after prefix in command messages ([dd70e60](https://github.com/kuraiwtf/uhc/commit/dd70e60d822cdf874003e62cba81dc892676c34b))
* sanitize ampersands in ticket questions to prevent unintended formatting ([066982d](https://github.com/kuraiwtf/uhc/commit/066982d6a5562620b425722e4dc93f51d579ad9f))
* stop cancelling the block place event in PowerListener ([31bc671](https://github.com/kuraiwtf/uhc/commit/31bc67104b127173a5d2992d4a1967432a21336b))
* stop game tasks after win celebration and remove unused code in `BuiltinDeathProcessor` ([ff04e0d](https://github.com/kuraiwtf/uhc/commit/ff04e0deedd93116dd4ba028f5c9cb5f6fa16224))
* update `BuiltinWinCondition` to use state-based profile filtering ([e803f15](https://github.com/kuraiwtf/uhc/commit/e803f15a8947cee0292ebe5903713372a9c51216))
* use the default value of array arguments when none is provided ([a8f2f05](https://github.com/kuraiwtf/uhc/commit/a8f2f056b73e75269ffe20e30cd61e5c011bdb04))
* validate radius input in `ModerationCommands` to prevent invalid teleportation scenarios ([fca77d3](https://github.com/kuraiwtf/uhc/commit/fca77d38717db5324ef64c69a5c109e97a7d0c27))


### Code Refactoring

* clean up code formatting and remove redundant Javadoc in enchantment-related classes ([bc327e1](https://github.com/kuraiwtf/uhc/commit/bc327e1aae94a4a56129108ae0518300269d8b65))
* comment out unused `RulesMenu` initialization in UltraHardcoreCommand ([36f8859](https://github.com/kuraiwtf/uhc/commit/36f8859109eee8865c06f47eb670d31ff9617494))
* delegate profile components to an Entity instance ([abec6a7](https://github.com/kuraiwtf/uhc/commit/abec6a793b6a0a8a6a2e645f47ea4ab465355fd8))
* extract role management logic into `RoleAttributor` and integrate with role-related modules ([10fbe71](https://github.com/kuraiwtf/uhc/commit/10fbe71ae48d120a6434eb1fc105224cbbfae9de))
* extract title logic to BuiltinSidebarTitleAdapter and update WaitingSidebarAdapter implementation ([302d1a5](https://github.com/kuraiwtf/uhc/commit/302d1a5226d57fc2aace2f00174039073a46c0b6))
* move onRoles hook to RoleModule ([057f333](https://github.com/kuraiwtf/uhc/commit/057f333fff338ccc1fad94e70baf7a08960c6e33))
* relocate `GameStartEvent` dispatch to the correct lifecycle point ([8b0d786](https://github.com/kuraiwtf/uhc/commit/8b0d78657ac277e39534ee3139a21a23411cbb9f))
* remove `MAX_LENGTH` restriction in `HelpOpCommand` and related truncation logic ([fb3ebbc](https://github.com/kuraiwtf/uhc/commit/fb3ebbc55c87c94da128f534138a292f7f562842))
* remove compatibility check for TeamModule and CampModule in ModuleServiceImpl ([98b4c28](https://github.com/kuraiwtf/uhc/commit/98b4c283a1e722066eae1c832a5f6a93cec8c845))
* remove per-command permission checks in favor of centralized host access validation ([0f63bb6](https://github.com/kuraiwtf/uhc/commit/0f63bb666734eaeac314bd25948234741ed7032a))
* remove redundant Javadoc comments in EnchantmentLimitService interface ([a0493a7](https://github.com/kuraiwtf/uhc/commit/a0493a7629b0f05eede83f0dfd020b997a28019b))
* remove unused `color` command from PlayerCommands ([b2aace0](https://github.com/kuraiwtf/uhc/commit/b2aace0110003d439da922615c92e51071dadb09))
* replace `HashMap` with `ConcurrentHashMap` in `SitUtil` for thread safety and null-check `SEATS` entries before destruction ([d388192](https://github.com/kuraiwtf/uhc/commit/d388192481b600b017eb63238cbcf1bb4e60dc7a))
* replace `HashSet` with thread-safe `ConcurrentHashMap.newKeySet` and add `findProfile` method in `ProfileService` interface ([da0ff4e](https://github.com/kuraiwtf/uhc/commit/da0ff4e66ff7ee794d6a2263df7834c77cbf22b4))
* set custom thread name and daemon mode in `PacketEventsWrapper` executor for clarity and lifecycle management ([9835577](https://github.com/kuraiwtf/uhc/commit/9835577359159f02f67062e95168dbb933c63702))
* simplify `RoleAttributor` by replacing redundant Component loop and improving title creation readability ([a277f25](https://github.com/kuraiwtf/uhc/commit/a277f25c6f360739d497dfe7ddfeccfde731255b))
* simplify `TimerDurationMenu` title by removing redundant "Configuration:" prefix ([11a7f39](https://github.com/kuraiwtf/uhc/commit/11a7f397d6a64ec3f1fb1d0202b5ff848721c698))
* simplify ChatListener by removing GameService dependency and redundant checks ([226f53e](https://github.com/kuraiwtf/uhc/commit/226f53e14f8a3f3c3630e283c8f2ee8075eae4af))
* simplify sender type checks and replace `printStackTrace` with proper logging in command classes ([25d2fd9](https://github.com/kuraiwtf/uhc/commit/25d2fd9d372bb93f04393005d162df036a3dd40f))
* streamline role and camp system by removing outdated abstractions and introducing unified `RoleMeta` and `Camp` interfaces ([f02cf4a](https://github.com/kuraiwtf/uhc/commit/f02cf4a25b9081e5b5b84915685c09066d8c91ee))
* unify strength and resistance handling into a single damage formula and simplify related logic ([92aa0f0](https://github.com/kuraiwtf/uhc/commit/92aa0f0846dd422a3740e4da9579284f395bb543))
* use `compileOnlyApi` for Lombok dependency in build configuration ([0ccf09b](https://github.com/kuraiwtf/uhc/commit/0ccf09b2cc5fc1959d3f3b0ba9638d7a697194a1))
* wrap AbstractModule components in an Entity ([caee2ad](https://github.com/kuraiwtf/uhc/commit/caee2ad1018d79c58f3a43b7dda1926815a91ac3))

## [1.17.1](https://github.com/kuraiwtf/uhc/compare/v1.17.0...v1.17.1) (2026-09-22)


### Code Refactoring

* replace WaitingItem registration with standard item registration and update MumbleItem constructor with slot parameter ([7a35be2](https://github.com/kuraiwtf/uhc/commit/7a35be2369c44b3b8362ac5f65735e5b1407e131))

## [1.17.0](https://github.com/kuraiwtf/uhc/compare/v1.16.0...v1.17.0) (2026-09-21)


### Features

* add PlayerSpectatorItem for player visibility and register it in ItemServiceImpl ([30e8346](https://github.com/kuraiwtf/uhc/commit/30e8346797e40dcc92d97624ab5615846097e299))
* add PvPTimer event listener and auto-resurrection logic during PvP phase ([5b2c55c](https://github.com/kuraiwtf/uhc/commit/5b2c55c7e4c235083d2cbb2abe8337c0f008109b))
* add random XP drop for diamond ore mining under CutClean scenario ([040ef22](https://github.com/kuraiwtf/uhc/commit/040ef223894df480cedb9ad3fcdc9bbbd7b2f015))
* add slot assignment to CustomItem and update built-in items accordingly ([7fca3b7](https://github.com/kuraiwtf/uhc/commit/7fca3b73c218ef16a6745f81f379905d218a543d))
* add slot parameter to ConfigurationItem constructor for enhanced configurability ([cc8c94d](https://github.com/kuraiwtf/uhc/commit/cc8c94dc430e180d233fe15232ba1d4cf52b8fc8))
* clear player inventory and equip spectator-only items on death ([f72e796](https://github.com/kuraiwtf/uhc/commit/f72e796a1c38eaaef9f97277df39df2a14c8308b))
* enhance death messaging with styled empty lines for improved visual clarity ([986c487](https://github.com/kuraiwtf/uhc/commit/986c487515a1b97e2e31e67f3f05c14a529521ad))


### Bug Fixes

* adjust PvPTimer instantiation to remove redundant worldService parameter ([9704bdb](https://github.com/kuraiwtf/uhc/commit/9704bdbcb223c4c553640b86e1ddf03c55f9eb22))
* delay death processing to ensure inventory state updates ([d1e83bd](https://github.com/kuraiwtf/uhc/commit/d1e83bd64310d86f1e2a2388d2a00827453799ad))


### Code Refactoring

* remove WaitingItem and related logic for streamlined item registration in ItemServiceImpl ([af5aba3](https://github.com/kuraiwtf/uhc/commit/af5aba39b7d2f413143b49df089636b89670831a))
* simplify item handling in WaitingListener by replacing WaitingItem with CustomItem and updating logic accordingly ([457641c](https://github.com/kuraiwtf/uhc/commit/457641cddff05efc88d7ac326a89d7fe91735ef3))
* use player's lastLocation for teleportation on death and streamline component handling ([4e1ca34](https://github.com/kuraiwtf/uhc/commit/4e1ca34982ada30c4bb9b5f442a58a5d38c232ad))

## [1.16.0](https://github.com/kuraiwtf/uhc/compare/v1.15.0...v1.16.0) (2026-09-21)


### Features

* annotate power packages with @NullMarked in API and engine modules ([148bf31](https://github.com/kuraiwtf/uhc/commit/148bf311b8010ed8240c88da78e1859793f8b4a1))
* implement PowerService and PowerServiceImpl for managing and decorating power item icons ([4ef0a1f](https://github.com/kuraiwtf/uhc/commit/4ef0a1faa49b763378f59d4937d4a50224124d12))
* integrate PowerService into UltraHardcoreAPI and UltraHardcoreEngine ([acd601e](https://github.com/kuraiwtf/uhc/commit/acd601e184a86a17e12119e3e5fad205da5a9411))
* introduce PowerDecorator interface to support item decoration functionality ([de1864a](https://github.com/kuraiwtf/uhc/commit/de1864a458e878c5b05effea36443683cfd8a5f1))


### Bug Fixes

* prevent from placing any power ([a73f032](https://github.com/kuraiwtf/uhc/commit/a73f032d1839f9801a64cbf2926f772f347503cf))
* remove permission for /h refill ([2d69f07](https://github.com/kuraiwtf/uhc/commit/2d69f07abd1a5851e514ae59d3b1f8b2838951ad))
* replace profile name on join ([b20a21f](https://github.com/kuraiwtf/uhc/commit/b20a21f3b408a4946049c70717404e696e06993c))
* streamline power handling logic and improve glow validation in PowerListener and GlowingTargetUpdaterTask ([b6ad41a](https://github.com/kuraiwtf/uhc/commit/b6ad41ae678ae7ae2c6e9812c3046d3802f52f77))

## [1.15.0](https://github.com/kuraiwtf/uhc/compare/v1.14.1...v1.15.0) (2026-09-18)


### Features

* add PowerTargetSelectionEvent and handle it in PowerListener ([118f800](https://github.com/kuraiwtf/uhc/commit/118f80056b3bbd24be2bb81908f19ee42476c7ab))


### Bug Fixes

* ignore children powers in host refill command ([e07febe](https://github.com/kuraiwtf/uhc/commit/e07febe209063842d9204309e055de3808523e1b))
* spectator management using hostmc whitelist system ([671ffe2](https://github.com/kuraiwtf/uhc/commit/671ffe2902f4317af85f56a7577d2978e5c297c8))

## [1.14.1](https://github.com/kuraiwtf/uhc/compare/v1.14.0...v1.14.1) (2026-09-17)


### Bug Fixes

* power icon detection ([a9b31fd](https://github.com/kuraiwtf/uhc/commit/a9b31fd0d030718840a7b1df52ebfab54b849f4c))

## [1.14.0](https://github.com/kuraiwtf/uhc/compare/v1.13.0...v1.14.0) (2026-09-17)


### Features

* add getPower via identifier method ([c1e40c7](https://github.com/kuraiwtf/uhc/commit/c1e40c753972cb5f2c74cdd041defd8cdbad7fda))


### Bug Fixes

* improve power detection ([c32cfb4](https://github.com/kuraiwtf/uhc/commit/c32cfb43a98b593326d715f9284c4b0d07be0cd6))
* improve power icon detection ([5e90180](https://github.com/kuraiwtf/uhc/commit/5e901803e120a9679c62fdc3f0fc937d3e2190ad))
* improve power item icon update ([479992b](https://github.com/kuraiwtf/uhc/commit/479992bc8e2324f8676de8aa1da4ba5dc6b729ad))
* improve power item icon update ([64d6898](https://github.com/kuraiwtf/uhc/commit/64d6898b1ad2131a6e801624664ff2abfa761e65))
* pass player unique id in cooldown decrementation task to call PowerCooldownEndEvent ([2808af4](https://github.com/kuraiwtf/uhc/commit/2808af4e19daccbf6a681d7168d7036f302a5247))

## [1.13.0](https://github.com/kuraiwtf/uhc/compare/v1.12.0...v1.13.0) (2026-09-12)


### Features

* use NBTs for power detection ([4dd8fdc](https://github.com/kuraiwtf/uhc/commit/4dd8fdcb942157a5330bdc58a2b77499ed96e59e))

## [1.12.0](https://github.com/kuraiwtf/uhc/compare/v1.11.0...v1.12.0) (2026-09-12)


### Features

* add inv see command ([0f53a0b](https://github.com/kuraiwtf/uhc/commit/0f53a0bcd69504c891d9ec0f5759a74896fae958))
* add log categories ([627994c](https://github.com/kuraiwtf/uhc/commit/627994c23c1df2ca80bcb1f2a05945a878c11117))
* add logger service ([75b2dd4](https://github.com/kuraiwtf/uhc/commit/75b2dd4fa2a22002cfa6500034e2cde0b6068172))
* add top luck menu ([df0c02d](https://github.com/kuraiwtf/uhc/commit/df0c02db6137c460395c7255d1c2bb259d425e1d))
* implement kill logs ([64b9270](https://github.com/kuraiwtf/uhc/commit/64b92702d6bbb8656be74f73adbd295f59febb96))
* implement power use and command logs ([2923130](https://github.com/kuraiwtf/uhc/commit/292313025ae2be56cef386d973ba59ae9a61067b))
* open inventory menu on right click at player ([e65dccc](https://github.com/kuraiwtf/uhc/commit/e65dccca188a10af83452e99c5d7c60b726aa672))


### Bug Fixes

* prevent spectator management during game ([08ed97c](https://github.com/kuraiwtf/uhc/commit/08ed97cc8ad150000e73c76330b113c24171edf1))
* update spawn location method ([ef3a43b](https://github.com/kuraiwtf/uhc/commit/ef3a43b231bc77ef5e388683b76c98b9bd912ec0))


### Code Refactoring

* improve spectator-related commands ([059253c](https://github.com/kuraiwtf/uhc/commit/059253cfbac984b211c1e898701e4493340f3db4))
* make revive method public ([a72cfbb](https://github.com/kuraiwtf/uhc/commit/a72cfbb1e576d4598af856443e1c1e49ff07ff5c))
* remove sound on group alert ([06f01d7](https://github.com/kuraiwtf/uhc/commit/06f01d7abca74767432db5f0f21bff47c01fa02b))
* use profileService variable ([f771367](https://github.com/kuraiwtf/uhc/commit/f771367267f5d96cbc2d8dfc1a3c75991f4a9629))

## [1.11.0](https://github.com/kuraiwtf/uhc/compare/v1.10.0...v1.11.0) (2026-09-09)


### Features

* add builtin menus and buttons for modules ([a81cc9e](https://github.com/kuraiwtf/uhc/commit/a81cc9ed08643f278dfbe83d9121e743e670ea90))
* add Describable interface ([00e6bb3](https://github.com/kuraiwtf/uhc/commit/00e6bb3bcc0941555abf1d6d0daa0727affd7a89))
* add hidden composition in RoleModule ([5f96d97](https://github.com/kuraiwtf/uhc/commit/5f96d97a1f38c2545a6f17ef7465434cf8266986))
* add module random events ([8d58438](https://github.com/kuraiwtf/uhc/commit/8d58438094283c84df3ab4ced833212414fc8e5a))
* add module random events ([3c99c90](https://github.com/kuraiwtf/uhc/commit/3c99c902644908d30c4168d5eeff2415808b4647))


### Bug Fixes

* make random events unrelated to bukkit events ([03bce96](https://github.com/kuraiwtf/uhc/commit/03bce96614bd4110f5ffcb434f36fefb098ebd8b))
* Module must return a ModuleMenu ([a64ce3c](https://github.com/kuraiwtf/uhc/commit/a64ce3c60b17a4a352400b29d0032fd0f093cb6b))

## [1.10.0](https://github.com/kuraiwtf/uhc/compare/v1.9.0...v1.10.0) (2026-09-09)


### Features

* add PowerCooldownStartEvent ([ba3d7fb](https://github.com/kuraiwtf/uhc/commit/ba3d7fbe7019372dc0aef552349b222623c68f8f))

## [1.9.0](https://github.com/kuraiwtf/uhc/compare/v1.8.0...v1.9.0) (2026-09-09)


### Features

* add recipe configuration menu and spectator rule handling ([86bd629](https://github.com/kuraiwtf/uhc/commit/86bd6295c4ee6d02a3606fe2f93ca78759548cc6))
* add recipe Menu and listener to prevent recipes from being crafted ([c3fcf31](https://github.com/kuraiwtf/uhc/commit/c3fcf31a3aa83acc061f6e8718ae1e693f0975e3))
* add recipe service ([4e717d1](https://github.com/kuraiwtf/uhc/commit/4e717d1f8c70225392c5d883c02bfb247e7a2102))
* enforce host permission checks across all host commands ([be3be08](https://github.com/kuraiwtf/uhc/commit/be3be082273e0b9af477132d22a4b18f005a0238))
* implement `RecipeService` API and `RecipeServiceImpl` to manage custom recipes ([442eec8](https://github.com/kuraiwtf/uhc/commit/442eec82447606bdfcf9402bba3b88fa7547f629))
* introduce `GameRecipe` API and implementation to manage custom recipes ([bca31e5](https://github.com/kuraiwtf/uhc/commit/bca31e55abcee3a1a80bf3d7c3601c13e48b995c))


### Code Refactoring

* remove unused `GameConfiguration` class ([a77cff3](https://github.com/kuraiwtf/uhc/commit/a77cff35b68c2d73ecbff1ca48c5f66dd87df4f4))

## [1.8.0](https://github.com/kuraiwtf/uhc/compare/v1.7.0...v1.8.0) (2026-09-06)


### Features

* cancel item drop events for items matching `AbstractItemPower` icons ([b597377](https://github.com/kuraiwtf/uhc/commit/b597377821622402033ea6ea4f5f60e8ed944e31))

## [1.7.0](https://github.com/kuraiwtf/uhc/compare/v1.6.1...v1.7.0) (2026-09-04)


### Features

* introduce `developer` method in `AbstractModule` with implementation in `BuiltinModule` ([b782cf1](https://github.com/kuraiwtf/uhc/commit/b782cf104b0c1998aa47195ec1d766e17fbe2093))


### Code Refactoring

* centralize sidebar credit handling via `sidebarCredit` method ([7d7d11e](https://github.com/kuraiwtf/uhc/commit/7d7d11eb536a8c2907748963a0465b40626bf8d1))
* inject `ModuleService` into `CreditTabListPart` for dynamic developer display ([bcf59bd](https://github.com/kuraiwtf/uhc/commit/bcf59bd752177b973a93d10ae724989050cc8312))

## [1.6.1](https://github.com/kuraiwtf/uhc/compare/v1.6.0...v1.6.1) (2026-09-04)


### Bug Fixes

* add fall damage immunity for players during scatter ([5d18594](https://github.com/kuraiwtf/uhc/commit/5d1859438357f542f2d51539718604b991032771))
* add NameComponent to player profiles during scatter ([b5a09ff](https://github.com/kuraiwtf/uhc/commit/b5a09ffe8c0573eb335175d0b838ef7c9b885692))
* reformat message sent for invalid player addition in `HostCommand` ([be85299](https://github.com/kuraiwtf/uhc/commit/be852999367511bc0a8097a3de29107a01e5ee8d))
* remove SpectatorComponent from players during scatter ([c9d96a3](https://github.com/kuraiwtf/uhc/commit/c9d96a34f6cd11e8cbc29e03677cf20d4a415ff2))
* set player GameMode to SURVIVAL after teleport during scatter ([dcbe48d](https://github.com/kuraiwtf/uhc/commit/dcbe48d990dd93dfd0580d50fd5eafa2f93bcac2))
* set spectator mode for non-playing players to prevent unintended spawn interactions ([d9080eb](https://github.com/kuraiwtf/uhc/commit/d9080ebadc88b77b5ec4e9489e587e18a4f09bc6))
* update player state check to prevent adding invalid players to the game ([714f319](https://github.com/kuraiwtf/uhc/commit/714f319d3b1351be25503250a3e9fcbacbe74d6d))

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
