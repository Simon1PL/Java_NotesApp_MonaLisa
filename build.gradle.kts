plugins {
    java
    id("application")
    id("org.openjfx.javafxplugin").version("0.0.9")
}

group = "pl.edu.agh.monalisa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClassName = "pl.edu.agh.monalisa.MonaLisaApplication"
}

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
}

javafx {
    version = "15.0.1"
    modules = listOf("javafx.base", "javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("junit", "junit", "4.12")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.google.inject:guice:4.2.3")
}
