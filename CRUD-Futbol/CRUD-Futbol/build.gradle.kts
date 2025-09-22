plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    //Mockito
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")

    // Logger
    implementation("org.lighthousegames:logging:1.5.0")
    implementation("ch.qos.logback:logback-classic:1.5.12")

    //BD
    implementation("com.h2database:h2:2.2.224")
    // JDBI
    // Reflexión
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Core
    implementation("org.jdbi:jdbi3-core:3.48.0")
    // SQL Object
    implementation("org.jdbi:jdbi3-sqlobject:3.48.0")
    // Extensión para Kotlin
    implementation("org.jdbi:jdbi3-kotlin:3.48.0")
    // Extensión de SQL Object para Kotlin
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.48.0")

    // Railway Oriented Programming
    implementation("com.michael-bull.kotlin-result:kotlin-result:2.0.0")

    // Caché Caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}