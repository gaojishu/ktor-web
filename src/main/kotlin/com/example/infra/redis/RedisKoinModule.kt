package com.example.infra.redis

import com.example.infra.log.log
import io.ktor.server.config.ApplicationConfig
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.support.ConnectionPoolSupport
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.dsl.onClose

fun redisKoinModule(config: ApplicationConfig): Module = module {

    /**
     * RedisClient（全局单例）
     */
    single {
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

        RedisClient.create(uri)

    }.onClose {
        log.info("💥 redis close")
        it?.close()
    }

    /**
     * Redis 连接池
     */
    single<GenericObjectPool<StatefulRedisConnection<String, String>>> {

        val client = get<RedisClient>()

        val maxTotal = config.property("redis.pool.maxTotal").getString().toInt()
        val maxIdle = config.property("redis.pool.maxIdle").getString().toInt()
        val minIdle = config.property("redis.pool.minIdle").getString().toInt()

        val poolConfig = GenericObjectPoolConfig<StatefulRedisConnection<String, String>>().apply {
            this.maxTotal = maxTotal
            this.maxIdle = maxIdle
            this.minIdle = minIdle
            blockWhenExhausted = true
        }

        ConnectionPoolSupport.createGenericObjectPool(
            { client.connect() },
            poolConfig
        )
    }

    single(createdAtStart = true) {

        val pool = get<GenericObjectPool<StatefulRedisConnection<String, String>>>()

        val conn = pool.borrowObject()
        try {
            val pong = conn.sync().ping()
            log.info("[Redis] started: $pong")
        } finally {
            pool.returnObject(conn)
        }
    }
}