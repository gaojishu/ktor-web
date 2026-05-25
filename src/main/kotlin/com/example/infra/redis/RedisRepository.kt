package com.example.infra.redis

import io.lettuce.core.api.StatefulRedisConnection
import kotlinx.coroutines.future.await
import org.apache.commons.pool2.impl.GenericObjectPool
import org.koin.core.annotation.Single

@Single
class RedisRepository(
    private val pool: GenericObjectPool<StatefulRedisConnection<String, String>>
) {

    /**
     * 高阶辅助函数：安全地从池中借出异步连接，并在执行完后自动归还
     */
    private suspend fun <T> executeAsync(block: suspend (StatefulRedisConnection<String, String>) -> T): T {
        val connection = pool.borrowObject()
        return try {
            block(connection)
        } finally {
            pool.returnObject(connection)
        }
    }

    /**
     * 写入键值对并设置过期时间（秒）
     */
    suspend fun setEx(key: String, value: String, seconds: Long) {
        executeAsync { conn ->
            conn.async().setex(key, seconds, value).await()
        }
    }

    /**
     * 读取键值
     */
    suspend fun get(key: String): String? {
        return executeAsync { conn ->
            conn.async().get(key).await()
        }
    }

    /**
     * 删除键
     */
    suspend fun del(key: String): Boolean {
        return executeAsync { conn ->
            val result = conn.async().del(key).await() ?: 0L
            result > 0L
        }
    }

    /**
     * 原子自增（限流用）
     */
    suspend fun incr(key: String): Long {
        return executeAsync { conn ->
            conn.async().incr(key).await() ?: 0L
        }
    }

    /**
     * 设置过期时间
     */
    suspend fun expire(key: String, seconds: Long): Boolean {
        return executeAsync { conn ->
            conn.async().expire(key, seconds).await() ?: false
        }
    }
}

