import io.izzel.taboolib.gradle.TabooLibPlugin

plugins {
    java
    id("io.izzel.taboolib") version "1.31"
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
}

taboolib {
    options("skip-kotlin")
    options("skip-kotlin-relocate")
    options("skip-env")
    version = "6.0.4-7"

    description {
        contributors {
            name("小白").description("Huntrace")
            name("Kylepoops")
        }

        dependencies {
            name("Multiverse-Core")
            name("Multiverse-Inventories")
            name("Multiverse-NetherPortals")
        }

        bukkitNodes = mapOf(
            "api-version" to "1.17",
            "libraries" to listOf("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
        )
    }
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.onarandombox.com/content/groups/public/")
}

dependencies {
    subprojects.forEach { taboo(it) }
}

allprojects {
    tasks.withType<org.gradle.jvm.tasks.Jar> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

subprojects {
    apply {
        plugin<JavaPlugin>()
        plugin<TabooLibPlugin>()
        plugin("org.jetbrains.kotlin.jvm")
    }

    taboolib {
        install("common")
        install("platform-bukkit")
        install("module-configuration")
        options("skip-kotlin")
        options("skip-kotlin-relocate")
        options("skip-env")
        exclude("taboolib")
        version = "6.0.4-7"
    }

    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.onarandombox.com/content/groups/public/")
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
}
