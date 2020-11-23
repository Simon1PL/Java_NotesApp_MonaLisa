plugins {
    java
}

group = "pl.edu.agh.monalisa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
}

dependencies {
    testImplementation("junit", "junit", "4.12")
}
