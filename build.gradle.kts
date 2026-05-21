buildscript {
    dependencies {
        // 关键：让 Gradle 插件能找到 Postgres 驱动
        classpath(libs.flyway.database.postgresql)
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.flyway)
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

    implementation(ktorLibs.server.requestValidation)

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


    //数据库
    implementation(libs.hikaricp)
    implementation(libs.postgresql)
    implementation(libs.jooq.core)

    //数据库迁移
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)


}


// 1. 抽取变量读取逻辑（带默认值防止报错）
val dbUrl = project.property("db.url").toString()
val dbUser = project.property("db.user").toString()
val dbPass = project.property("db.pass").toString()
val dbDriver = project.property("db.driver").toString()
val dbSchemas = project.property("db.schemas").toString().split(",").toTypedArray()


flyway {
    url = dbUrl
    user = dbUser
    password = dbPass
    schemas = arrayOf("admin","public")
    defaultSchema = "admin"
    baselineOnMigrate = true
    baselineVersion = "0"
    cleanDisabled = false
    locations = arrayOf("filesystem:src/main/resources/db/migration")
}
