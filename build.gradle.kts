plugins {
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

subprojects {
    apply(plugin = "java")

    group = "dev.agustinventura"
    version = "0.0.1-SNAPSHOT"

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        "testImplementation"(rootProject.libs.junit.jupiter)
        "testRuntimeOnly"(rootProject.libs.junit.platform.launcher)
        "testImplementation"(rootProject.libs.assertj)
    }
}

configure(
    listOf(
        project(":domain"),
        project(":application"),
        project(":infrastructure")
    )
) {
    dependencies {
        "testImplementation"(rootProject.libs.mockito)
    }
}
