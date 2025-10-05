dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {}
}

rootProject.name = "rub-config"

include("api", "common")
include("paper", "spigot")
include("velocity", "bungee")

project(":api").projectDir = file("rub-api")
project(":common").projectDir = file("rub-common")
project(":paper").projectDir = file("rum-paper")
project(":spigot").projectDir = file("rum-spigot")
project(":velocity").projectDir = file("rup-velocity")
project(":bungee").projectDir = file("rup-bungee")