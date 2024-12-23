plugins {
    id("java")
    id("application")
}

group = "io.javaside.harmonizer"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.16")

    compileOnly("com.fasterxml.jackson.core:jackson-core:2.18.2")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.18.2")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation(project(":api"))
    implementation(project(":core"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    // Replace with the fully qualified name of your main class
    mainClass.set("io.javaside.jharmonizer.kaligosample.Launcher")
}
