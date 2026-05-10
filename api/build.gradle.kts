plugins {
    id("uhc.java-library-conventions")
    id("uhc.maven-publishing-conventions")
}

val targetJavaVersion = 23

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)

    withSourcesJar()
    withJavadocJar()
}

dependencies {
    api(platform(libs.adventure.bom))
    api(libs.bundles.apollo)
    api(libs.bundles.adventure)
    api(libs.menu.api)
    compileOnly(libs.packetevents.spigot)
    compileOnly(libs.spigot.api)
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])

        groupId = project.group.toString()
        artifactId = "${rootProject.name}-${project.name}"
        version = project.version.toString()
    }
}
