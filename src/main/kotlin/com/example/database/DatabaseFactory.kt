package com.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL

object DatabaseFactory {

    lateinit var dsl: DSLContext

    fun init(config: ApplicationConfig) {

        val hikariConfig = HikariConfig().apply {

            jdbcUrl =
                config.property("database.jdbcUrl").getString()
            username =
                config.property("database.username").getString()
            password =
                config.property("database.password").getString()

            driverClassName =
                config.property("database.driverClassName").getString()

            maximumPoolSize =
                config.property("database.maximumPoolSize").getString().toInt()

            minimumIdle =
                config.property("database.minimumIdle").getString().toInt()

            idleTimeout =
                config.property("database.idleTimeout").getString().toLong()

            maxLifetime =
                config.property("database.maxLifetime").getString().toLong()

            connectionTimeout =
                config.property("database.connectionTimeout").getString().toLong()
        }
        val hikariSource = HikariDataSource(
            hikariConfig
        )

        runFlyway(hikariSource)

        dsl = DSL.using(
            hikariSource,
            SQLDialect.POSTGRES
        )
    }

    private fun runFlyway(dataSource: HikariDataSource) {
        val flyway = Flyway.configure()
            .dataSource(dataSource)
            // 设置迁移脚本存放路径（对应 resources/db/migration 目录）
            .locations("db/migration")
            .schemas("admin","public")
            .cleanDisabled(true)
            // 允许不按顺序执行历史缺失的版本（推荐团队协作开启）
            .outOfOrder(false)
            // 允许在非空数据库上初始化基准
            .baselineOnMigrate(true)
            .baselineVersion("0")
            .load()

        // 执行迁移
        flyway.migrate()
    }
}