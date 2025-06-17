import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java") // Java support
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.intelliJPlatform) // IntelliJ Platform Gradle Plugin
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
    alias(libs.plugins.qodana) // Gradle Qodana Plugin
    alias(libs.plugins.kover) // Gradle Kover Plugin
    id("io.gitlab.arturbosch.detekt") version "1.23.4" // Detekt for static code analysis
    id("info.solidsoft.pitest") version "1.15.0"
    id("org.jetbrains.grammarkit") version "2022.3.2.2" // Grammar-Kit plugin
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    google()

    intellijPlatform {
        defaultRepositories()
    }
}

// Dependencies are managed with Gradle version catalog - read more: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
dependencies {
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.opentest4j)

    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))

        // Plugin Dependencies. Uses `platformBundledPlugins` property from the gradle.properties file for bundled IntelliJ Platform plugins.
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })

        // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file for plugin from JetBrains Marketplace.
        plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })

        testFramework(TestFrameworkType.Platform)
    }
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    pluginConfiguration {
        name = providers.gradleProperty("pluginName")
        version = providers.gradleProperty("pluginVersion")

        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        val changelog = project.changelog // local variable for configuration cache compatibility
        changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }
        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
        }
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        channels = providers.gradleProperty("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
}

// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration
kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}

grammarKit {
    jflexRelease = "1.9.1" // Specify JFlex version
}

// Task configuration for JFlex lexer generation
tasks.withType<org.jetbrains.grammarkit.tasks.GenerateLexerTask>().configureEach {
    sourceFile.set(layout.projectDirectory.file("src/main/grammars/Picat.flex"))
    targetOutputDir.set(layout.buildDirectory.dir("generated/sources/grammarkit/gen/com/github/avrilfanomar/picatplugin/language/lexer"))
    outputs.upToDateWhen { false } // Force task to always run
}

val genDir = layout.buildDirectory.dir("generated/sources/grammarkit/gen")
val basePackagePath = "com/github/avrilfanomar/picatplugin/language"

tasks.withType<GenerateParserTask>().configureEach {
    outputs.upToDateWhen { false } // Force task to always run
    sourceFile.set(layout.projectDirectory.file("src/main/grammars/Picat.bnf"))
    targetRootOutputDir.set(genDir) // genDir is layout.buildDirectory.dir("/generated/sources/grammarkit/gen")

    pathToParser.set("$basePackagePath/parser") // Directory for the parser class
    pathToPsiRoot.set("$basePackagePath/psi")
}

sourceSets {
    main {
        java {
            srcDirs(layout.buildDirectory.dir("generated/sources/grammarkit/gen"))
        }
    }
    test {
        // Generated sources should be taken from main output, not re-added here
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.named("compileKotlin", org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
    dependsOn(tasks.named("generateParser"))
    dependsOn(tasks.named("generateLexer"))
}

tasks.named("compileTestKotlin", org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
    dependsOn(tasks.named("generateParser"))
    dependsOn(tasks.named("generateLexer"))
    // Ensure main output (including compiled Java PSI) is on the classpath
    libraries.from(project.sourceSets.main.get().output.classesDirs)
}

// Configure Detekt - read more: https://detekt.dev/docs/introduction/gradle/
detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules
    config.setFrom(files("$projectDir/config/detekt.yml")) // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

// Configure Pitest for mutation testing - read more: https://pitest.org/
pitest {
    junit5PluginVersion = "1.2.0"
    targetClasses = listOf("com.github.avrilfanomar.picatplugin.*")
    excludedClasses = listOf(
        // Exclude generated files and UI-related classes that are hard to test
        "com.github.avrilfanomar.picatplugin.language.psi.impl.*",
        "com.github.avrilfanomar.picatplugin.language.PicatParserDefinition",
        "com.github.avrilfanomar.picatplugin.language.PicatLanguage"
    )
    threads = Runtime.getRuntime().availableProcessors()
    outputFormats = listOf("HTML", "XML")
    timestampedReports = false
    mutators = listOf(
        "STRONGER", // Use stronger mutation operators
        "DEFAULTS"  // Plus the defaults
    )
    avoidCallsTo = listOf(
        "kotlin.jvm.internal",
        "kotlin.collections.CollectionsKt"
    )
    verbose = true
    testPlugin = "junit5"
}

tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    publishPlugin {
        dependsOn(patchChangelog)
    }

    // Register a task to run Pitest mutation testing
    register<DefaultTask>("runMutationTests") {
        group = "verification"
        description = "Runs Pitest mutation tests"

        dependsOn("pitest")

        doLast {
            println("Mutation testing completed. Reports can be found in build/reports/pitest")
        }
    }
}

intellijPlatformTesting {
    runIde {
        register("runIdeForUiTests") {
            task {
                jvmArgumentProviders += CommandLineArgumentProvider {
                    listOf(
                        "-Drobot-server.port=8082",
                        "-Dide.mac.message.dialogs.as.sheets=false",
                        "-Djb.privacy.policy.text=<!--999.999-->",
                        "-Djb.consents.confirmation.enabled=false",
                    )
                }
            }

            plugins {
                robotServerPlugin()
            }
        }
    }
}
