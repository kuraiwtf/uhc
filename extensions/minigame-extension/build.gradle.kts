plugins {
    id("uhc.extension-conventions")
}

dependencies {
    annotationProcessor(libs.lombok)
    compileOnly(libs.spigot)
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    processResources {
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
}
