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
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        "testRuntimeOnly"(rootProject.libs.junit.platform.launcher)
        "testRuntimeOnly"(rootProject.libs.junit.platform.engine)
        "testRuntimeOnly"(rootProject.libs.junit.platform.commons)
        "testImplementation"(rootProject.libs.junit.jupiter)
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
