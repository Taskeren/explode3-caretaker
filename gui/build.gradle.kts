import org.jetbrains.compose.desktop.application.dsl.TargetFormat

/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reserved.
 */

plugins {
	kotlin("jvm")
	id("org.jetbrains.compose") version "1.5.10"
	id("com.github.johnrengelman.shadow") version "8.1.1"
}

val guiVersion = "1.0.0"

project.version = guiVersion

repositories {
	mavenCentral()
}

dependencies {
	implementation(rootProject)
	implementation(compose.desktop.currentOs)
}

kotlin {
}

compose.desktop {
	application {
		mainClass = "cn.taskeren.explode3.caretaker.gui.MainKt"

		nativeDistributions {
			targetFormats(TargetFormat.Exe)

			packageName = "Caretaker"
			packageVersion = guiVersion

			windows {
				menuGroup = "Caretaker"
				upgradeUuid = "47fdd58f-49c8-4a61-8d07-80d926cd63b7"
			}
		}
	}
}

tasks.build {
	dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
	manifest {
		attributes("Main-Class" to "cn.taskeren.explode3.caretaker.gui.MainKt")
	}
}
