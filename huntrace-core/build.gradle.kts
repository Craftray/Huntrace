group = "io.craftray.huntrace"

dependencies {
    taboo("io.papermc:paperlib:1.0.8-SNAPSHOT")
    compileOnly(project(":huntrace-interface"))
    compileOnly(project(":huntrace-game"))
    compileOnly(project(":huntrace-common"))
}