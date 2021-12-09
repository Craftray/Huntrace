group = "io.craftray.huntrace"

taboolib {
    install("module-chat")
    install("module-lang")
    install("module-ui")
}

dependencies {
    compileOnly(project(":huntrace-common"))
    compileOnly(project(":huntrace-game"))
    taboo("cloud.commandframework:cloud-paper:1.6.0")
    taboo("cloud.commandframework:cloud-annotations:1.6.0")
}