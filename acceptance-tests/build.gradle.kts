plugins {
    `java-library`
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