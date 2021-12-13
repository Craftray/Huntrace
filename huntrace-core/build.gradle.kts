group = "io.craftray.huntrace"

dependencies {
    taboo("io.papermc:paperlib:1.0.8-SNAPSHOT")
    compileOnly(project(":huntrace-interaction"))
    compileOnly(project(":huntrace-game"))
    compileOnly(project(":huntrace-util"))
    compileOnly(project(":huntrace-abstract"))
}