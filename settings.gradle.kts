rootProject.name = "uhc"

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS

    repositories {
        mavenCentral()
        maven("https://repo.lunarclient.dev")
        maven("https://jitpack.io")
        maven("https://repo.aikar.co/nexus/content/repositories/aikar/")
        maven("https://repo.codemc.io/repository/maven-releases/")
        maven("https://repo.codemc.io/repository/maven-snapshots/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.j4c0b3y.net/public/")
    }
}

includeBuild("build-logic")
include("api", "engine")
include("extensions:mumble-extension", "extensions:minigame-extension")