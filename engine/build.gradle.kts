plugins {
    id("uhc.java-library-conventions")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":api"))
    compileOnly(libs.packetevents.spigot)
    compileOnly(libs.spigot.api)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.processResources {
    filteringCharset = "UTF-8"
}
