plugins {
    id("java")
}

group = "com.ubo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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