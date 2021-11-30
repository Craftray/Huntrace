group = "io.craftray.huntrace"

taboolib {
    install("module-chat")
    install("module-lang")
    install("module-ui")
}

dependencies {
    compileOnly(project(":common"))
}