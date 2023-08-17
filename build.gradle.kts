plugins {
    java
    application
}

group = "explode"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("cn.taskeren.explode3.caretaker.Main")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "cn.taskeren.explode3.caretaker.Main"
        )
    }
}