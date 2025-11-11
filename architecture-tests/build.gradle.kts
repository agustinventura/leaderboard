plugins {
    `java-library`
}

dependencies {
    testImplementation(project(":domain"))
    testImplementation(project(":application"))
    testImplementation(project(":infrastructure"))
    testImplementation(libs.archunit)
}

tasks.withType<Test> {
    useJUnitPlatform()
}