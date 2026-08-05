import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.openapi.generator)
    java
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.swagger.annotations)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
}

tasks.withType<GenerateTask> {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/api/openapi.yaml")
    outputDir.set(layout.buildDirectory.dir("generated").get().asFile.absolutePath)
    apiPackage.set("dev.agustinventura.leaderboard.adapters.in.rest")
    modelPackage.set("dev.agustinventura.leaderboard.adapters.in.rest.dto")
    modelNameSuffix.set("RESTDTO")
    openapiGeneratorIgnoreList.set(listOf("src/main/java/org/openapitools/OpenApiGeneratorApplication.java"))
    configOptions.set(
        mapOf(
            "delegatePattern" to "true",
            "useSpringBoot4" to "true",
            "useJackson3" to "true",
            "openApiNullable" to "false",
            "documentationProvider" to "springdoc",
            "useTags" to "true"
        )
    )
}

java {
    sourceSets.main {
        java {
            srcDir(layout.buildDirectory.dir("generated/src/main/java").get().asFile.absolutePath)
        }
    }
}

tasks.compileJava {
    dependsOn(tasks.withType<GenerateTask>())
}

tasks.clean {
    delete(layout.buildDirectory.dir("generated"))
}