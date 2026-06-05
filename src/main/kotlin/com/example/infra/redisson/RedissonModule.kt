package com.example.infra.redisson

import io.ktor.server.config.ApplicationConfig
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config

@Module
class RedissonModule {

    @Single(binds = [RedissonClient::class])
    fun provideRedissonClient(config: ApplicationConfig): RedissonClient {
        val host = config.property("redis.host").getString()
        val port = config.property("redis.port").getString().toInt()
        val dbIndex = config.property("redis.database").getString().toInt()
        val password = config.propertyOrNull("redis.password")?.getString()

        val timeout = config.property("redis.timeout").getString().toInt()
        val connectTimeout = config.property("redis.connectTimeout").getString().toInt()

        val maxTotal = config.property("redis.pool.maxTotal").getString().toInt()
        val minIdle = config.property("redis.pool.minIdle").getString().toInt()

        val configObj = Config().apply {
            if (!password.isNullOrBlank()) {
                this.password = password
            }

            useSingleServer().apply {
                address = "redis://$host:$port"
                database = dbIndex
                this.timeout = timeout
                this.connectTimeout = connectTimeout
                connectionPoolSize = maxTotal
                connectionMinimumIdleSize = minIdle
            }
        }
        return Redisson.create(configObj)
    }
}
