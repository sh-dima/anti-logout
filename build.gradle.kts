import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

plugins {
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.shadow)
	alias(libs.plugins.publish)
}

version = "2.1.0"
group = "org.samo_lego"

repositories {
	maven("https://maven.fabricmc.net/")
	maven("https://oss.sonatype.org/content/repositories/snapshots")
	maven("https://jitpack.io")
}

loom {
	splitEnvironmentSourceSets()

	mods {
		create(name) {
			sourceSet("main")
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

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

val generatedResources: Directory = layout.buildDirectory.dir("generated/resources").get()

sourceSets {
	getByName("main") {
		resources {
			exclude("assets/${project.name}/icon.xcf")
			srcDir(generatedResources)
		}
	}
}

val exportIcon by tasks.registering(Exec::class) {
	group = "build"
	description = "Exports icon.xcf to icon.png using ImageMagick."

	val inputFile = file("src/main/resources/assets/${project.name}/icon.xcf")
	val outputFile = generatedResources.file("assets/${project.name}/icon.png").asFile

	inputs.file(inputFile)
	outputs.file(outputFile)

	doFirst {
		outputFile.parentFile.mkdirs()
	}

	commandLine(
		"sh",
		"-c",
		"""
		gimp-console --batch-interpreter=python-fu-eval --quit --batch '
		from gi.repository import Gimp, Gio

		image = Gimp.file_load(
			Gimp.RunMode.NONINTERACTIVE,
			Gio.File.new_for_path("${inputFile.absolutePath}"),
		)

		Gimp.file_save(
			Gimp.RunMode.NONINTERACTIVE,
			image,
			Gio.File.new_for_path("${outputFile.absolutePath}"),
			None,
		)

		image.delete()
		' &&

		pngcrush -rem iCCP -ow "${outputFile.absolutePath}" &&

		magick "${outputFile.absolutePath}" -strip "${outputFile.absolutePath}"
		""".trimIndent(),
	)
}

tasks.processResources {
	dependsOn(exportIcon)

	inputs.property("version", project.version)
	inputs.property("minecraft_version", libs.versions.minecraft.get())
	inputs.property("fabric_version", libs.versions.fabric.loader.get())
	inputs.property("fabric_api_version", libs.versions.fabric.api.get())
	inputs.property("java_version", java.toolchain.languageVersion.get().asInt())

	inputs.property("name", project.name)
	inputs.property("group", project.group)

	filesMatching("fabric.mod.json") {
		expand(
			mapOf(
				"name" to inputs.properties["name"],
				"group" to inputs.properties["group"],

				"minecraft_version" to inputs.properties["minecraft_version"],
				"fabric_version" to inputs.properties["fabric_version"],
				"fabric_api_version" to inputs.properties["fabric_api_version"],
				"java_version" to inputs.properties["java_version"],
				"version" to inputs.properties["version"],
			),
		)
	}
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

tasks.named<Jar>("jar") {
	inputs.property("archivesName", project.base.archivesName)
}

val shadowJar by tasks.named<ShadowJar>("shadowJar") {
	configurations = listOf(shadowOnly)
	enableAutoRelocation = true
	relocationPrefix = "${project.group}.${project.name}.shadow"

	into("META-INF/") {
		from("LICENSE.txt")
		from("NOTICE.txt")
		from("docs/DISCLAIMER.txt")
	}
}
tasks.named<RemapJarTask>("remapJar") {
	dependsOn(shadowJar)
	inputFile.set(shadowJar.archiveFile)
	doLast {
		shadowJar.archiveFile.get().asFile.delete()
	}
}
