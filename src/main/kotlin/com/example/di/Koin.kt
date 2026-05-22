package com.example.di

import com.example.feature.app.user.UserRepo
import com.example.feature.app.user.app.AppUserService
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