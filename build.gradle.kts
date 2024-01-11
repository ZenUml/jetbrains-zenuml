import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.date
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  // Java support
  id("java")
  // Kotlin support
  id("org.jetbrains.kotlin.jvm") version "2.0.0-Beta2"
  // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
  id("org.jetbrains.intellij") version "1.16.1"
  // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
  id("org.jetbrains.changelog") version "1.3.1"
  // detekt linter - read more: https://detekt.github.io/detekt/gradle.html
  id("io.gitlab.arturbosch.detekt") version "1.21.0"
  // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
  id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
  id("groovy")
  id("com.github.node-gradle.node") version "3.2.1"
}

apply(plugin = "com.github.node-gradle.node")

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
  mavenCentral()
  flatDir { dirs("lib") }
}
dependencies {
  // https://mvnrepository.com/artifact/com.googlecode.owasp-java-html-sanitizer/owasp-java-html-sanitizer
  implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer")
//  compile "org.jetbrains:markdown:${markdownParserVersion}"
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
  implementation("io.reactivex.rxjava2:rxjava:2.2.21")

  testImplementation("junit:junit:4.12")
  testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.10")
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
  testImplementation("org.spockframework:spock-core:2.2-groovy-4.0") {
    exclude("org.codehaus.groovy", "groovy-xml")
  }
}
// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
  pluginName.set(properties("pluginName"))
  version.set(properties("platformVersion"))
  type.set(properties("platformType"))
  downloadSources.set(properties("platformDownloadSources").toBoolean())
  updateSinceUntilBuild.set(false) // don't write information of current IntelliJ build into plugin.xml, instead use information from patchPluginXml

//  Plugin Dependencies:
//  https://www.jetbrains.org/intellij/sdk/docs/basics/plugin_structure/plugin_dependencies.html
//
  plugins.set(listOf("com.intellij.java", "org.intellij.intelliLang"))
}

// Configure detekt plugin.
// Read more: https://detekt.github.io/detekt/kotlindsl.html
detekt {
  config = files("./detekt-config.yml")
  buildUponDefaultConfig = true

  reports {
    html.enabled = false
    xml.enabled = false
    txt.enabled = false
  }
}
sourceSets {
  main {
    java {
      srcDirs("src")
      srcDirs("gen")
    }
    resources {
      srcDirs("resource")
    }
  }
  test {
    java {
      srcDirs("test/src")
    }
    resources {
      srcDirs("test/data")
    }
  }
}
//tasks.jar {
//  doFirst{
//    //check if needed draw.io submodule is initialized
//    if (!File(projectDir, "src/webview/drawio/src").exists()) {
//      throw GradleException("please init subprojects by execution 'git submodule update --init'")
//    }
//  }
//  from("src/webview/drawio/src/main/webapp") {
//    include("**/*")
//    exclude("index.html")
//    into("assets")
//  }
//  from("src/webview") {
//    include("index.html")
//    into("assets")
//  }
//}

tasks {
  // Set the compatibility versions to 11
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }
  listOf("compileKotlin", "compileTestKotlin").forEach {
    getByName<KotlinCompile>(it) {
      kotlinOptions.jvmTarget = "17"
    }
  }

  withType<Detekt> {
    jvmTarget = "11"
  }
  patchPluginXml {
    version.set(properties("pluginVersion"))
    sinceBuild.set((properties("pluginSinceBuild")))
    // untilBuild(pluginUntilBuild) --> don't set "untilBuild" to allow new versions to use existing plugin without changes until breaking API changes are known

    // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
    pluginDescription.set(
      provider {
        File(projectDir, "README.md").readText().lines().run {
          val start = "<!-- Plugin description -->"
          val end = "<!-- Plugin description end -->"

          if (!containsAll(listOf(start, end))) {
            throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
          }
          subList(indexOf(start) + 1, indexOf(end))
        }.joinToString("\n").run { markdownToHTML(this) }
      }
    )

    // Get the latest available change notes from the changelog file
    changeNotes.set(
      provider {
        changelog.getLatest().toHTML()
      }
    )
  }

  runPluginVerifier {
    ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
  }

  publishPlugin {
    dependsOn("patchChangelog")
    token.set(System.getenv("PUBLISH_TOKEN"))
    // if release is marked as a pre-release in the GitHub release, push it to EAP
    channels.set(listOf(if ("true" == System.getenv("PRE_RELEASE")) "EAP" else "default"))
  }
  changelog {
    version.set(properties("pluginVersion"))
    header.set(provider { "[${project.version}] - ${date()}" })
  }
  node {
    download.set(true)
    // Version of node to download and install (only used if download is true)
    // It will be unpacked in the workDir
    version.set("12.18.3")
    nodeProjectDir.set(file("${project.projectDir}/jetbrains-viewer"))
  }
}

tasks.test {
  //useJUnitPlatform()
}

project.gradle.startParameter.excludedTaskNames.add("ktlintKotlinScriptCheck")
project.gradle.startParameter.excludedTaskNames.add("ktlintMainSourceSetCheck")
