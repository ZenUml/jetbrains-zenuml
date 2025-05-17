import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.date
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  // Java support
  id("java")
  // Kotlin support
  id("org.jetbrains.kotlin.jvm") version "2.0.0-Beta2"
  // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
  id("org.jetbrains.intellij.platform") version "2.2.0"
  // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
  id("org.jetbrains.changelog") version "1.3.1"
  // detekt linter - read more: https://detekt.github.io/detekt/gradle.html
  id("io.gitlab.arturbosch.detekt") version "1.23.6"
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
  intellijPlatform {
    defaultRepositories()
  }
  flatDir { dirs("lib") }
}
dependencies {
  intellijPlatform {
    create(properties("platformType"), properties("platformVersion"))
    bundledPlugins(listOf("com.intellij.java", "org.intellij.intelliLang"))
  }
  // https://mvnrepository.com/artifact/com.googlecode.owasp-java-html-sanitizer/owasp-java-html-sanitizer
  implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer")
//  compile "org.jetbrains:markdown:${markdownParserVersion}"
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
  implementation("io.reactivex.rxjava2:rxjava:2.2.21")

  testImplementation("junit:junit:4.12")
  testImplementation("org.powermock:powermock-module-junit4:2.0.2")
  testImplementation("org.powermock:powermock-api-mockito2:2.0.2")
  testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.10")
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
  testImplementation("org.spockframework:spock-core:2.2-groovy-4.0") {
    exclude("org.codehaus.groovy", "groovy-xml")
  }
}
// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellijPlatform {
  pluginConfiguration {
    name = properties("pluginName")
    version = properties("pluginVersion")

    description = File(projectDir, "README.md").readText().lines().run {
      val start = "<!-- Plugin description -->"
      val end = "<!-- Plugin description end -->"

      if (!containsAll(listOf(start, end))) {
        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
      }
      subList(indexOf(start) + 1, indexOf(end))
    }.joinToString("\n").run { markdownToHTML(this) }

    changeNotes = changelog.getLatest().toHTML()

    ideaVersion {
      sinceBuild = properties("pluginSinceBuild")
    }
  }

  pluginVerification {
    ides {
      properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty).forEach {
        ide(IntelliJPlatformType.IntellijIdeaCommunity, it)
      }
    }
  }

  publishing {
    token = System.getenv("PUBLISH_TOKEN")
    channels = listOf(if ("true" == System.getenv("PRE_RELEASE")) "EAP" else "default")
  }

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
  
  // Add duplicates strategy for all copy tasks
  withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
  }
  
  // Also add it for the jar task
  withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
  }
}

tasks.test {
  //useJUnitPlatform()
}

project.gradle.startParameter.excludedTaskNames.add("ktlintKotlinScriptCheck")
project.gradle.startParameter.excludedTaskNames.add("ktlintMainSourceSetCheck")
