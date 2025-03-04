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