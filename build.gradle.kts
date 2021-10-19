plugins {
    java
    id("io.izzel.taboolib") version "1.30"
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
}

taboolib {
    install("common")
    install("platform-bukkit")
    install("module-configuration")
    install("module-chat")
    install("module-lang")
    version = "6.0.3-5"

    description {
        contributors {
            name("小白").description("Huntrace")
            name("Kylepoops")
        }
        // 依赖选项
//        dependencies {
//            name("ExamplePlugin")
//            name("ExamplePlugin").optional(true)
//        }
    }
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.17-R0.1-SNAPSHOT")
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}
