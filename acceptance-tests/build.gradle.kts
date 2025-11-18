import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    id("io.spring.dependency-management")
}

configure<DependencyManagementExtension> {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    testImplementation(project(":infrastructure"))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.cucumber.java)
    testImplementation(libs.cucumber.spring)
    testRuntimeOnly(libs.cucumber.junit.engine)
    testImplementation(libs.rest.assured)
}

tasks.withType<Test> {
    useJUnitPlatform()
}