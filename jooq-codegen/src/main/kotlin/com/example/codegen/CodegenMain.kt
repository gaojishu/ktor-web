package com.example.codegen

import com.example.codegen.config.AdminForcedTypeConfig
import com.example.codegen.config.GlobalForcedTypeConfig
import org.jooq.meta.jaxb.*
import java.io.File
import java.util.Properties

fun main() {
    val props = Properties()

    // 1. 安全获取项目根目录 (ktor-web)
    val userDir = File(System.getProperty("user.dir"))
    val rootDir = if (userDir.name == "jooq-codegen") {
        userDir.parentFile // 如果在子模块里，根目录是上一级
    } else {
        userDir // 如果已经在根目录
    }

    // 2. 定位并加载 gradle.properties
    val file = rootDir.resolve("gradle.properties")
    if (!file.exists()) {
        error("配置文件不存在，期望路径: ${file.absolutePath}")
    }

    file.inputStream().use {
        props.load(it)
    }

    val url = props.getProperty("db.url")
    val user = props.getProperty("db.user")
    val password = props.getProperty("db.pass")
    val schemas = props.getProperty("db.schemas")
    val dbDriver = props.getProperty("db.driver")

    val schemasList = schemas
        .split(",")
        .map { it.trim() }
        .map { schema ->
            if (schema == "public") {
                SchemaMappingType().withInputSchema(schema).withOutputSchemaToDefault(true)
            } else {
                SchemaMappingType().withInputSchema(schema)
            }
        }

    // 3. 修正输出目录：应该生成到 jooq-codegen 模块内部的 build 目录中
    val outputDir = userDir.resolve("build/generated-sources/jooq").absoluteFile

    val configuration = Configuration()
        .withJdbc(
            Jdbc()
                .withDriver(dbDriver)
                .withUrl(url)
                .withUser(user)
                .withPassword(password)
        )
        .withGenerator(
            Generator()
                .withName("org.jooq.codegen.KotlinGenerator")
                .withDatabase(
                    Database()
                        .withName("org.jooq.meta.postgres.PostgresDatabase")
                        .withSchemata(schemasList)
                        .withIncludeRoutines(false)
                        .withExcludes(
                            """
                            pg_.*

                            |information_schema.*
                            |.*halfvec.*
                            |.*vector.*
                            """.trimIndent()
                        )
                        .withForcedTypes(
                            *GlobalForcedTypeConfig.forcedTypes.toTypedArray(),
                            *AdminForcedTypeConfig.forcedTypes.toTypedArray(),
                        )
                )
                .withGenerate(
                    Generate()
                        .withDaos(false)
                        .withRecords(true)
                        .withImmutablePojos(true)
                        .withFluentSetters(true)
                )
                .withTarget(
                    org.jooq.meta.jaxb.Target()
                        .withPackageName("com.example.jooq.generate")
                        .withDirectory(outputDir.toString())
                )
        )

    println("OUTPUT DIR = $outputDir")

    // 🔴 4. 真正触发 jOOQ 代码生成（确保你的逻辑里有这一行执行代码）
    org.jooq.codegen.GenerationTool.generate(configuration)
    println("jOOQ 代码生成成功！")
}
