package com.example.infra.database

import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class DatabaseBootstrap(
    val dataSource: HikariDataSource,
    flyway: Flyway,
)
