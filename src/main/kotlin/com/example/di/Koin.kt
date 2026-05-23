package com.example.di

import com.example.feature.user.repo.AppUserRepo
import com.example.feature.user.service.AppUserService
import com.example.infra.database.databaseKoinModule
import com.example.infra.redis.redisKoinModule
import com.example.infra.redisson.redissonKoinModule
import io.ktor.server.config.ApplicationConfig
import org.koin.dsl.module

val allModules = { config: ApplicationConfig ->
    listOf(
        databaseKoinModule(config),
        redisKoinModule(config),
        redissonKoinModule(config),
        module {
            single { AppUserRepo(get()) }
        },
        module {
            single { AppUserService(get()) }
        }
    )
}