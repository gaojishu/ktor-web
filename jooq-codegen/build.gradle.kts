// jooq-codegen/build.gradle.kts
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jooq.codegen)
    java
}
//
//repositories {
//    gradlePluginPortal()
//    mavenCentral()
//    maven { url = uri("https://maven.aliyun.com/repository/public") }
//}

kotlin {
    jvmToolchain(25)
}

dependencies {
//    // jOOQ 插件及其代码生成核心
    implementation(libs.postgresql)

    //implementation(libs.jooq.codegen.gradle)
    implementation(libs.jooq.meta)
    implementation(libs.jooq.codegen)
}

