dependencies {
    implementation(project(":api"))
    compileOnly(libs.jetanno)
    implementation(libs.bundles.config)
}

tasks.register<Copy>("exportResources") {
    from("src/main/resources")
    into("${buildDir}/exportedResources")
}
