plugins {
    `java-library`
}

dependencies {
    testImplementation(project(":infrastructure"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.cucumber:cucumber-java:7.30.0")
    testImplementation("io.cucumber:cucumber-spring:7.30.0")
    testRuntimeOnly("io.cucumber:cucumber-junit-platform-engine:7.30.0")
    testImplementation("io.rest-assured:rest-assured:5.5.6")
}

tasks.withType<Test> {
    useJUnitPlatform()
}