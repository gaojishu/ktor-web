package com.example.infra.redisson

import org.redisson.api.RedissonClient
import java.util.concurrent.TimeUnit

class RedissonLock(
    private val client: RedissonClient
) {

    fun <T> withLock(
        key: String,
        waitTimeMs: Long = 0,
        leaseTimeMs: Long = 30000,
        block: () -> T
    ): T {

        val lock = client.getLock(key)

        val success = if (waitTimeMs > 0) {
            lock.tryLock(waitTimeMs, leaseTimeMs, TimeUnit.MILLISECONDS)
        } else {
            lock.tryLock(leaseTimeMs, TimeUnit.MILLISECONDS)
        }

        if (!success) {
            throw IllegalStateException("获取锁失败: $key")
        }

        try {
            return block()
        } finally {
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }
}