group = "${rootProject.group}.${rootProject.name}"
version = rootProject.version

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://repo.lunarclient.dev")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.j4c0b3y.net/public/")
}
