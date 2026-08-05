import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    alias(libs.plugins.spring.dependency.management)
}

configure<DependencyManagementExtension> {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    testImplementation(project(":infrastructure"))
    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.cucumber.java)
    testImplementation(libs.cucumber.spring)
    testImplementation(libs.rest.assured)
    testRuntimeOnly(libs.cucumber.junit.engine)
}