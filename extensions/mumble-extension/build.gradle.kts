plugins {
  id("uhc.extension-conventions")
}

dependencies {
  annotationProcessor(libs.lombok)
  compileOnly(libs.spigot)
}
