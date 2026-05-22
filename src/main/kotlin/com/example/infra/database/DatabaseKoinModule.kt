package com.example.infra.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.dsl.onClose

fun databaseKoinModule(config: ApplicationConfig): Module = module {

    /**
     * Hikari DataSource（交给 Koin 管生命周期）
     */
    single<HikariDataSource>(createdAtStart = true) {

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = config.property("database.jdbcUrl").getString()
            username = config.property("database.username").getString()
            password = config.property("database.password").getString()
            driverClassName = config.property("database.driverClassName").getString()

            maximumPoolSize = config.property("database.maximumPoolSize").getString().toInt()
            minimumIdle = config.propertyOrNull("database.minimumIdle")?.getString()?.toInt() ?: 2

            idleTimeout = config.propertyOrNull("database.idleTimeout")?.getString()?.toLong() ?: 600000
            maxLifetime = config.propertyOrNull("database.maxLifetime")?.getString()?.toLong() ?: 1800000
            connectionTimeout = config.propertyOrNull("database.connectionTimeout")?.getString()?.toLong() ?: 30000
        }

        HikariDataSource(hikariConfig)
    }.onClose {
        it?.close()
    }

    /**
     * Flyway（启动即执行 migration）
     */
    single(createdAtStart = true) {
        val ds = get<HikariDataSource>()

        Flyway.configure()
            .dataSource(ds)
            .locations("db/migration")
            .schemas("public","admin") // 建议单 schema
            .baselineOnMigrate(true)
            .baselineVersion("0")
            .outOfOrder(false)
            .cleanDisabled(true)
            .load()
            .migrate()
    }

    /**
     * jOOQ DSLContext（全局共享）
     */
    single<DSLContext> {
        DSL.using(get<HikariDataSource>(), SQLDialect.POSTGRES)
    }
}