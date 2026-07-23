import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

plugins {
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.shadow)
	alias(libs.plugins.publish)
}

version = "2.1.0"
group = "org.samo_lego"

base {
	archivesName.set("antilogout")
}

repositories {
	maven("https://maven.fabricmc.net/")
	maven("https://oss.sonatype.org/content/repositories/snapshots")
	maven("https://jitpack.io")
}

loom {
	splitEnvironmentSourceSets()

	mods {
		create("modid") {
			sourceSet("main")
			sourceSet("client")
		}
	}
}

val shadowOnly by configurations.creating

dependencies {
	minecraft(libs.minecraft)
	mappings("${libs.yarn.get()}:v2")
	modImplementation(libs.fabric.loader)
	modImplementation(libs.fabric.api)

	modImplementation(libs.fabric.permissions)
	implementation(libs.config.core)
	implementation(libs.config.toml)
	shadowOnly(libs.config.core)
	shadowOnly(libs.config.toml)
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand(
			mapOf(
				"version" to inputs.properties["version"],
			),
		)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release.set(21)
}

tasks.withType<AbstractArchiveTask>().configureEach {
	isPreserveFileTimestamps = false
	isReproducibleFileOrder = true

	filePermissions {
		user.read = true
		user.write = true
		user.execute = false

		group.read = true
		group.write = false
		group.execute = false

		other.read = true
		other.write = false
		other.execute = false
	}

	dirPermissions {
		user.read = true
		user.write = true
		user.execute = true

		group.read = true
		group.write = false
		group.execute = true

		other.read = false
		other.write = false
		other.execute = true
	}
}

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.named<Jar>("jar") {
	inputs.property("archivesName", project.base.archivesName)

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archivesName"]}" }
	}
}

val shadowJar by tasks.named<ShadowJar>("shadowJar") {
	configurations = listOf(shadowOnly)
	relocate("com.electronwill.nightconfig", "org.samo_lego.antilogout.shadow.nightconfig")
}
tasks.named<RemapJarTask>("remapJar") {
	dependsOn(shadowJar)
	inputFile.set(shadowJar.archiveFile)
	doLast {
		shadowJar.archiveFile.get().asFile.delete()
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = "antilogout"
			from(components["java"])
		}
	}
	repositories {
		// Add repositories to publish to here.
	}
}
