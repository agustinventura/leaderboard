plugins {
    id("org.springframework.boot")
    id("java")
}

dependencies {
    implementation(project(":application"))
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jdbc)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)
    implementation(libs.spring.kafka)
    developmentOnly(libs.spring.boot.devtools)
    runtimeOnly(libs.postgresql)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.kafka.test)
}