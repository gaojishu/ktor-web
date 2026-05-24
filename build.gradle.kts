buildscript {
    dependencies {
        classpath(libs.flyway.database.postgresql)
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.flyway)
    alias(libs.plugins.koin.compiler.plugin)
}

// configuration
tasks.shadowJar {
    mergeServiceFiles{
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

sourceSets {
    main {
        kotlin.srcDir("build/generated-sources/jooq")
    }
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
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.openfolder.kotlinAsyncapiKtor)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)

    //验证器
    implementation(ktorLibs.server.requestValidation)
    //序列化
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.contentNegotiation)
    //全局异常
    implementation(ktorLibs.server.statusPages)
    //日志
    implementation(ktorLibs.server.callLogging)
    //限流
    implementation(ktorLibs.server.rateLimit)
    //gzip 压缩
    implementation(ktorLibs.server.compression)
    //反向代理
    implementation(ktorLibs.server.forwardedHeader)
    //websocket
    implementation(ktorLibs.server.websockets)
    //jwt
    implementation(ktorLibs.server.auth.jwt)

    //数据库
    implementation(libs.hikaricp)
    implementation(libs.postgresql)
    implementation(libs.jooq.core)

    //数据库迁移
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    //redis
    implementation(libs.lettuce.core)
    implementation(libs.commons.pool2)
    implementation(libs.redisson)

    //di
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.koin.annotations)

}


tasks.register<JavaExec>("codegen") {
    description = "jooq codegen"
    group = "build"

    classpath = project(":jooq-codegen").sourceSets["main"].runtimeClasspath

    mainClass.set("com.example.codegen.CodegenMainKt")
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
    schemas = dbSchemas
    defaultSchema = dbSchemas.first()
    baselineOnMigrate = true
    baselineVersion = "0"
    cleanDisabled = false
    locations = arrayOf("filesystem:src/main/resources/db/migration")
}
