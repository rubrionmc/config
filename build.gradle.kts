import java.net.HttpURLConnection
import java.net.URL
import java.io.File

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

// -------------------- PACKET BUILD TASK --------------------
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

// -------------------- AI JAVADOC TASK --------------------
val openRouterApiKey = System.getenv("DEEPSEEK_API_KEY")
val openRouterModel = "deepseek/deepseek-r1:free"
val backupFolder = "build/backup"

tasks.register("generateAiDocs") {
    group = "documentation"
    description = "Generates AI-enhanced JavaDocs for all Java files."

    doLast {
        if (openRouterApiKey.isNullOrBlank())
            throw GradleException("DEEPSEEK_API_KEY environment variable not set!")

        val backupRoot = File(backupFolder).apply { mkdirs() }

        subprojects.forEach { proj ->
            val srcFolder = proj.file("src/main/java")
            if (!srcFolder.exists()) return@forEach

            srcFolder.walkTopDown().filter { it.isFile && it.extension == "java" }.forEach { javaFile ->
                println("Processing: ${javaFile.relativeTo(proj.projectDir)}")

                val backupFile = File(backupRoot, javaFile.relativeTo(proj.projectDir).invariantSeparatorsPath)
                backupFile.parentFile.mkdirs()
                javaFile.copyTo(backupFile, overwrite = true)

                val code = javaFile.readText()
                val prompt = """
                    Return the code as plain text, without any Markdown formatting or escaped characters.
                    You are a Java expert. Enhance existing JavaDoc or add comprehensive documentation.
                    Include @author, @since, @param, @return, and @throws where applicable.
                    Keep code structure intact.
                    Java Code:
                    $code
                """.trimIndent()

                val jsonBody = """
                    {
                        "model": "$openRouterModel",
                        "messages": [{"role":"user","content":"$prompt"}],
                        "temperature": 0.2,
                        "max_tokens": 4096
                    }
                """.trimIndent()

                try {
                    val url = URL("https://openrouter.ai/api/v1/chat/completions")
                    val conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.setRequestProperty("Authorization", "Bearer $openRouterApiKey")
                    conn.setRequestProperty("Content-Type", "application/json")
                    conn.doOutput = true

                    conn.outputStream.use { it.write(jsonBody.toByteArray()) }

                    if (conn.responseCode != 200) {
                        println("HTTP ${conn.responseCode} for ${javaFile.name}")
                        return@forEach
                    }

                    val response = conn.inputStream.bufferedReader().readText()
                    val content = "\"content\":\"".toRegex().find(response)?.let { m -> response.substring(m.range.last + 1, response.lastIndexOf("\"")) }
                    if (content.isNullOrBlank()) return@forEach
                    val cleanedContent = content.replace("\\n", "\n")
                        .replace("\\t", "\t")
                        .replace("\\\"", "\"")
                        .replace("\\'", "'")
                    javaFile.writeText(cleanedContent.trim())
                    println("Updated ${javaFile.name}")
                } catch (e: Exception) {
                    println("Failed ${javaFile.name}: ${e.message}")
                }

                Thread.sleep(50000)
            }
        }

        println("[X] AI JavaDoc generation done. Backups in $backupFolder.")
    }
}
