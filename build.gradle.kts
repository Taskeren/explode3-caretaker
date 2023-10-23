/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reversed.
 */

plugins {
	java
	application
	`maven-publish`
}

group = "explode"
version = "1.3.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

java {
	withJavadocJar()
	withSourcesJar()
}

tasks.getByName<Test>("test") {
	useJUnitPlatform()
}

distributions {
	forEach {
		it.contents {
			from("../LICENSE")
			from("../caretaker.dll")
		}
	}
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

	from(rootDir) {
		include("LICENSE")
		include("caretaker.dll")
	}
}

tasks.publish {
	dependsOn(tasks.test)
}

publishing {
	repositories {
		maven {
			val suffix =
				if("${project.version}".endsWith("SNAPSHOT", ignoreCase = true)) {
					"snapshots"
				} else {
					"releases"
				}
			url = uri("http://play.elytra.cn:31055/$suffix")

			isAllowInsecureProtocol = true

			credentials {
				username = project.findProperty("repo.username")?.toString() ?: System.getenv("REPO_USERNAME")
				password = project.findProperty("repo.password")?.toString() ?: System.getenv("REPO_PASSWORD")
			}
		}
	}

	publications {
		create<MavenPublication>("TaskerenRepo") {
			groupId = "cn.taskeren.explode3"
			artifactId = "caretaker"
			version = project.version.toString()

			from(components["java"])
		}
	}
}
