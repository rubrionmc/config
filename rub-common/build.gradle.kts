dependencies {
    implementation(project(":api"))
    implementation(libs.common)
    compileOnly(libs.jetanno)
    compileOnly(libs.slf4j)
    implementation(libs.logback)
    implementation(libs.bundles.config)
}

tasks.register<Copy>("exportResources") {
    from("src/main/resources")
    into("${buildDir}/exportedResources")
}
