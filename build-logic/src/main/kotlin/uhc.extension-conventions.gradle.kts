plugins {
  id("uhc.maven-publishing-conventions")
}

dependencies.compileOnly(project(":api"))

publishing {
  publications.create<MavenPublication>("maven") {
    from(components["java"])

    groupId = project.group.toString() + ".extension"
    artifactId = project.name
    version = project.version.toString()
  }
}
