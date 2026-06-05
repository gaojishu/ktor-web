package com.example.infra.redis

import io.ktor.server.config.ApplicationConfig
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.support.ConnectionPoolSupport
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class RedisModule {

    @Single(binds = [RedisClient::class])
    fun provideRedisClient(config: ApplicationConfig): RedisClient {
        val host = config.property("redis.host").getString()
        val port = config.property("redis.port").getString().toInt()
        val password = config.propertyOrNull("redis.password")?.getString()
        val database = config.property("redis.database").getString().toInt()

        val uri = buildString {
            append("redis://")
            if (!password.isNullOrBlank()) {
                append(":$password@")
            }
            append("$host:$port/$database")
        }

        return RedisClient.create(uri)
    }

    @Single(binds = [GenericObjectPool::class])
    fun provideRedisPool(
        client: RedisClient,
        config: ApplicationConfig,
    ): GenericObjectPool<StatefulRedisConnection<String, String>> {
        val maxTotal = config.property("redis.pool.maxTotal").getString().toInt()
        val maxIdle = config.property("redis.pool.maxIdle").getString().toInt()
        val minIdle = config.property("redis.pool.minIdle").getString().toInt()

        val poolConfig = GenericObjectPoolConfig<StatefulRedisConnection<String, String>>().apply {
            this.maxTotal = maxTotal
            this.maxIdle = maxIdle
            this.minIdle = minIdle
            blockWhenExhausted = true
        }

        return ConnectionPoolSupport.createGenericObjectPool(
            { client.connect() },
            poolConfig,
        )
    }
}
