val serializationVersion = "1.7.1"
val coroutineVersion = "1.9.0"
val dateTimeVersion = "0.6.1"
val exposedVersion = "0.54.0"
val psqlVersion = "42.7.2"
val ktorVersion = "2.3.12"
val auth0Version = "4.2.1"
val jwtVersion = "0.12.3"
val mailVersion = "8.6.3"

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"


}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-security")
    testImplementation("org.springframework.security", "spring-security-test")
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    testImplementation("org.jetbrains.kotlin", "kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher")
    implementation("org.jetbrains.kotlin", "kotlin-reflect")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", serializationVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor", coroutineVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", coroutineVersion)
    implementation("org.jetbrains.exposed", "exposed-spring-boot-starter", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-kotlin-datetime", exposedVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-datetime", dateTimeVersion)
    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
    implementation("org.postgresql", "postgresql", psqlVersion)
    implementation("org.simplejavamail:simple-java-mail:$mailVersion")
    implementation("com.auth0", "java-jwt", auth0Version)
    implementation("io.jsonwebtoken:jjwt-jackson:$jwtVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jwtVersion")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
