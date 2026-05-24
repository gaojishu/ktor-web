package com.example.infra.redis

import com.example.common.utils.log.log
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

    /**
     * RedisClient（全局单例）
     * 🎯 核心更正：新版插件推荐显式声明具体的 Closeable 绑定，或依靠 Ktor 关闭事件兜底
     */
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

    /**
     * Redis 连接池
     * 🎯 核心更正：显式声明 binds，以便在 Koin 销毁时自动执行其底层 close 逻辑
     */
    @Single(binds = [GenericObjectPool::class])
    fun provideRedisPool(
        client: RedisClient,
        config: ApplicationConfig
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
            poolConfig
        )
    }

    /**
     * Redis 启动连通性测试与服务引导
     * 🎯 设置为 createdAtStart = true 保证随 Ktor 启动时立刻执行
     */
    @Single(createdAtStart = true)
    fun provideRedisInitializer(
        pool: GenericObjectPool<StatefulRedisConnection<String, String>>
    ): RedisInitMarker {
        val conn = pool.borrowObject()
        try {
            val pong = conn.sync().ping()
            log.info("[Redis] started: $pong")
        } finally {
            pool.returnObject(conn)
        }
        return RedisInitMarker
    }
}

/**
 * 辅助标记类：因为新版 Koin 注解方法必须返回一个具体的实例对象
 */
object RedisInitMarker
