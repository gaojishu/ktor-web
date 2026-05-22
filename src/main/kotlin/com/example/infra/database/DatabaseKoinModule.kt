package com.example.infra.database

import org.jooq.DSLContext
import org.koin.dsl.module

val databaseKoinModule = module {
    single<DSLContext> { DatabaseManager.dsl }
}