package com.example.di

import com.example.infra.database.DatabaseModule
import com.example.infra.redis.RedisModule
import com.example.infra.redisson.RedissonModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        DatabaseModule::class,
        RedisModule::class,
        RedissonModule::class,
    ]
)
@ComponentScan(
    value = [
        "com.example"
    ]
)
class AppModule

