package com.example.di

import com.example.feature.user.repo.UserRepo
import com.example.feature.user.service.AppUserService
import com.example.infra.database.databaseKoinModule
import org.koin.dsl.module

val allModules = listOf(
    databaseKoinModule,
    module {
        single { UserRepo(get()) }
    },
    module {
        single { AppUserService(get()) }
    }
)