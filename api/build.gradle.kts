plugins {
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
  //  api(libs.menu.api)
  api(files("../libs/MenuAPI-core-1.5.5.jar"))
  api(libs.entity.lib)
  api(libs.actionbar.api)
  api(libs.lombok)
  api(libs.fastutil)
  annotationProcessor(libs.lombok)
  compileOnly(libs.packetevents.spigot)
  compileOnly(libs.spigot)
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])

      groupId = project.group.toString()
      artifactId = "${rootProject.name}-${project.name}"
      version = project.version.toString()
    }
  }

  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/kuraiwtf/uhc")
      credentials {
        username = System.getenv("GITHUB_ACTOR")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
}
