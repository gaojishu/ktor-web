package com.example.infra.redis

import com.example.infra.log.log
import io.ktor.server.config.ApplicationConfig
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.support.ConnectionPoolSupport
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig

object RedisManager {
    private var client: RedisClient? = null

    // 连接池核心对象
    private var pool: GenericObjectPool<StatefulRedisConnection<String, String>>? = null

    fun init(config: ApplicationConfig) {
        if (client == null) {
            val host = config.property("redis.host").getString()
            val port = config.property("redis.port").getString().toInt()
            val password = config.propertyOrNull("redis.password")?.getString()
            val database = config.property("redis.database").getString().toInt()

            val maxTotal = config.property("redis.pool.maxTotal").getString().toInt()
            val maxIdle = config.property("redis.pool.maxIdle").getString().toInt()
            val minIdle = config.property("redis.pool.minIdle").getString().toInt()

            val uri = "redis://$password@$host:$port/$database"

            client = RedisClient.create(uri)

            // 1. 配置连接池参数
            val poolConfig = GenericObjectPoolConfig<StatefulRedisConnection<String, String>>().apply {
                this.maxTotal = maxTotal        // 最大连接数
                this.maxIdle = maxIdle          // 最大空闲连接数
                this.minIdle = minIdle          // 最小空闲连接数
                blockWhenExhausted = true      // 连接耗尽时等待
            }

            // 2. 使用 Lettuce 的工具类创建连接池
            pool = ConnectionPoolSupport.createGenericObjectPool(
                { client!!.connect() }, // 连接工厂
                poolConfig
            )

            try {
                val pingResult = execute { connection ->
                    connection.sync().ping()
                }
                log.info("[Redis] 连接池初始化成功，Ping 响应: $pingResult")
            } catch (e: Exception) {
                log.error("[Redis] 连接失败！错误原因: ${e.message}")
                throw e
            }
        }
    }


    fun <T> execute(block: (StatefulRedisConnection<String, String>) -> T): T {
        val poolInstance = pool ?: throw IllegalStateException("Redis 连接池未初始化")
        // borrowObject() 借出连接
        val connection = poolInstance.borrowObject()
        return try {
            block(connection)
        } finally {
            // returnObject() 归还连接
            poolInstance.returnObject(connection)
        }
    }

    fun close() {
        pool?.close()
        client?.shutdown()
    }
}