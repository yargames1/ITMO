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


kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("ServerMain")
}
tasks.jar {
    manifest {
        attributes["Main-Class"] = "ServerMain" // <- то же имя, что и в application
    }

    // Включаем зависимости в jar (fat jar)
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}