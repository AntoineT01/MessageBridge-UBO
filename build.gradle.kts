plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "com.ubo"
version = "1.1.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-controls:19.0.2.1")
    implementation("org.openjfx:javafx-fxml:19.0.2.1")
    implementation("org.openjfx:javafx-base:19.0.2.1")
    implementation("org.openjfx:javafx-graphics:19.0.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation ("org.yaml:snakeyaml:2.0")
    implementation("io.github.cdimascio:java-dotenv:5.2.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

javafx {
    version = "19.0.2.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

tasks.test {
    useJUnitPlatform()
}

// Configuration d'encodage en Kotlin DSL
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<JavaExec> {
    systemProperty("file.encoding", "UTF-8")
}

// Pour la tâche run spécifique si vous en avez une
tasks.findByName("run")?.let {
    it as JavaExec
    it.systemProperty("file.encoding", "UTF-8")
}

// Configuration du JAR exécutable
tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to "com.ubo.tp.message.app.MessageAppLauncher",
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }

    // Inclure toutes les dépendances dans le JAR (fat JAR)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}