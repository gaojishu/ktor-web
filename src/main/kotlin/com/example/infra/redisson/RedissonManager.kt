package com.example.infra.redisson

import com.example.infra.log.log
import io.ktor.server.config.ApplicationConfig
import io.lettuce.core.ClientOptions
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config

object RedissonManager {
    private var client: RedissonClient? = null

    fun init(config: ApplicationConfig) {
        if (client != null) return

        val host = config.property("redis.host").getString()
        val port = config.property("redis.port").getString().toInt()
        val dbIndex = config.property("redis.database").getString().toInt()
        val password = config.propertyOrNull("redis.password")?.getString()

        val timeout = config.property("redis.timeout").getString().toInt()
        val connectTimeout = config.property("redis.connectTimeout").getString().toInt()

        val maxTotal = config.property("redis.pool.maxTotal").getString().toInt()
        val minIdle = config.property("redis.pool.minIdle").getString().toInt()

        val redissonConfig = Config().apply {
            useSingleServer().apply {
                address = "redis://$host:$port"
                database = dbIndex
                this.connectTimeout = connectTimeout
                this.timeout = timeout
                connectionPoolSize = maxTotal
                connectionMinimumIdleSize = minIdle
            }
        }

        client = Redisson.create(redissonConfig)

        // 修改客户端配置，显式关闭维护通知
        val options = ClientOptions.builder()
            // 寻找或配置有关维护通知、SCH (Smart Client Handoff) 的开关
            // 不同的 Lettuce 升级版本API略有不同，通常在 SocketOptions 或 ProtocolOptions 附近
            .build()

        try {
            val count = client!!.keys.count()
            log.info("[Redisson] 连接初始化成功 $count")
        } catch (e: Exception) {
            log.error("[Redisson] 连接失败！错误原因: ${e.message}")
            close()
            throw e
        }
    }

    fun getClient(): RedissonClient =
        client ?: throw IllegalStateException("Redisson 未初始化")

    fun close() {
        client?.shutdown()
        client = null
    }
}