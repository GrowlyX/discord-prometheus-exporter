import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "io.liftgate.discord.exporter"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

dependencies {
    api(kotlin("stdlib"))
    api("io.prometheus:simpleclient_httpserver:0.16.0")

    api("dev.kord:kord-core:0.8.0-M16")
    api("com.xenomachina:kotlin-argparser:2.0.7")
}

tasks.withType<Jar> {
    this.archiveFileName.set("discord-prometheus-exporter.jar")
}

application {
    this.mainClass.set("io.liftgate.discord.exporter.ApplicationKt")
}

tasks.withType<KotlinCompile> {
    this.kotlinOptions.jvmTarget = "1.8"
}

tasks.register<GradleBuild>("cleanBuild") {
    tasks = listOf("clean", "build")
}
