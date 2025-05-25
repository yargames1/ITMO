plugins {
    kotlin("jvm")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Other"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
application {
    mainClass.set("ClientMain")
}
tasks.jar {
    manifest {
        attributes["Main-Class"] = "ClientMain" // <- то же имя, что и в application
    }

    // Включаем зависимости в jar (fat jar)
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}