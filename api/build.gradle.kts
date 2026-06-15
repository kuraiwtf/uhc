plugins {
    id("uhc.java-library-conventions")
    id("uhc.maven-publishing-conventions")
}

java {
    toolchain { languageVersion = JavaLanguageVersion.of(25) }

    withSourcesJar()
    withJavadocJar()
}

dependencies {
    api(platform(libs.adventure.bom))
    api(libs.bundles.apollo)
    api(libs.bundles.adventure)
    api(libs.bundles.jackson)
    api(libs.menu.api)
    api(libs.entity.lib)
    api(libs.actionbar.api)
    api(libs.lombok)
    annotationProcessor(libs.lombok)
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
