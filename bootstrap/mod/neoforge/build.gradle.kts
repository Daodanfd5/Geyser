plugins {
    id("geyser.modded-conventions")
    id("geyser.modrinth-uploading-conventions")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

// This is provided by "org.cloudburstmc.math.mutable" too, so yeet.
// NeoForge's class loader is *really* annoying.
provided("org.cloudburstmc.math", "api")
provided("com.google.errorprone", "error_prone_annotations")

// Jackson shipped by Minecraft is too old, so we shade & relocate our newer version
relocate("com.fasterxml.jackson")

val includeTransitive: Configuration = configurations.getByName("includeTransitive")

dependencies {
    // See https://github.com/google/guava/issues/6618
    modules {
        module("com.google.guava:listenablefuture") {
          replacedBy("com.google.guava:guava", "listenablefuture is part of guava")
        }
    }

    neoForge(libs.neoforge.minecraft)

    api(project(":mod", configuration = "namedElements"))
    shadow(project(path = ":mod", configuration = "transformProductionNeoForge")) {
        isTransitive = false
    }
    shadow(projects.core) { isTransitive = false }

    // Minecraft (1.21.2+) includes jackson. But an old version!
    shadow(libs.jackson.core) { isTransitive = false }
    shadow(libs.jackson.databind) { isTransitive = false }
    shadow(libs.jackson.dataformat.yaml) { isTransitive = false }
    shadow(libs.jackson.annotations) { isTransitive = false }

    // Let's shade in our own api
    shadow(projects.api) { isTransitive = false }

    // cannot be shaded, since neoforge will complain if floodgate-neoforge tries to provide this
    include(projects.common)

    // Include all transitive deps of core via JiJ
    includeTransitive(projects.core)

    modImplementation(libs.cloud.neoforge)
    include(libs.cloud.neoforge)
}

tasks.withType<Jar> {
    manifest.attributes["Main-Class"] = "org.geysermc.geyser.platform.neoforge.GeyserNeoForgeMain"
}

tasks {
    remapJar {
        archiveBaseName.set("Geyser-NeoForge")
    }

    remapModrinthJar {
        archiveBaseName.set("geyser-neoforge")
    }

    shadowJar {
        // Without this, jackson's service files are not relocated
        mergeServiceFiles()
    }
}

modrinth {
    loaders.add("neoforge")
    uploadFile.set(tasks.getByPath("remapModrinthJar"))
}
