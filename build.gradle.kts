plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
//org.jetbrains.kotlin.plugin.serialization

}

group = "com.example"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "com.example.ApplicationKt"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.openfolder.kotlinAsyncapiKtor)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)




    //序列化
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.contentNegotiation)

    //全局异常
    implementation(ktorLibs.server.statusPages)

    //日志
    implementation(ktorLibs.server.callLogging)

    //压缩
    implementation(ktorLibs.server.compression)

    //限流
    implementation(ktorLibs.server.rateLimit)

    //
    implementation(ktorLibs.server.websockets)

    //gzip 压缩
    implementation(ktorLibs.server.compression)

    //反向代理
    implementation(ktorLibs.server.forwardedHeader)
}

