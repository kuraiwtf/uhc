plugins {
  id("uhc.maven-publishing-conventions")
}

java {
  toolchain { languageVersion = JavaLanguageVersion.of(25) }

  withSourcesJar()
  withJavadocJar()
}

val menuApiJar = file("../libs/MenuAPI-core-1.5.5.jar")

dependencies {
  api(platform(libs.adventure.bom))
  api(libs.bundles.apollo)
  api(libs.bundles.adventure)
  api(libs.bundles.jackson)
  compileOnlyApi(files(menuApiJar))
  api(libs.entity.lib)
  api(libs.actionbar.api)
  compileOnlyApi(libs.lombok)
  api(libs.fastutil)
  annotationProcessor(libs.lombok)
  compileOnly(libs.packetevents.spigot)
  compileOnly(libs.spigot)
}

tasks.jar {
  from(zipTree(menuApiJar)) {
    exclude("META-INF/**")
  }
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
}
