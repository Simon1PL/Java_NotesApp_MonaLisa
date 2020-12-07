plugins {
    java
    id("application")
    id("org.openjfx.javafxplugin").version("0.0.9")
}

group = "pl.edu.agh.monalisa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/speljohan/rxjavafx-mirror")
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

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.4.2")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.google.inject:guice:4.2.3")
    implementation("io.reactivex.rxjava3:rxjava:3.0.8")
    implementation("io.reactivex.rxjava3:rxjavafx:3.0.1")
}
