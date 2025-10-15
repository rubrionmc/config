plugins {
    java
    `maven-publish`
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.lombok) apply false
}

group = property("group")!!
version = property("version")!!

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(property("javaVersion").toString().toInt()))
}

allprojects {
    group = property("group")!!
    version = property("version")!!

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://libraries.minecraft.net")
        maven("https://repo.codemc.io/repository/maven-releases/")
        maven("https://repo.codemc.io/repository/maven-snapshots/")
        maven("https://rubrionmc.github.io/repository/")
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "io.freefair.lombok")

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(property("javaVersion").toString().toInt()))
    }

    tasks.withType<JavaCompile> { options.encoding = "UTF-8" }

    tasks.withType<Jar> {
        archiveBaseName.set("${rootProject.name}-${project.name}")

        val dependencies = configurations.runtimeClasspath.get()
        from({
            dependencies.map { if (it.isDirectory) it else zipTree(it) }
        })

        exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
            }
        }
        repositories {
            maven {
                name = "local-repo"
                url = uri(rootProject.projectDir.resolve("../repository"))
            }
        }
    }
}

tasks.register("packets") {
    group = "build"
    description = "Builds all platform variants and copies them to /out"
    dependsOn(subprojects.mapNotNull { it.tasks.findByName("build") })

    doLast {
        val outDir = rootProject.file("out").apply { mkdirs() }
        subprojects.filter { it.name != "api" }.forEach { proj ->
            val jar = proj.buildDir.resolve("libs/${rootProject.name}-${proj.name}-${proj.version}.jar")
            if (jar.exists()) {
                jar.copyTo(outDir.resolve(jar.name), overwrite = true)
                println("[C] Copied ${jar.name} to out/")
            }
        }
        println("[X] All builds finished.")
    }
}