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

project(":api").projectDir = file("rub-api")
project(":common").projectDir = file("rub-common")