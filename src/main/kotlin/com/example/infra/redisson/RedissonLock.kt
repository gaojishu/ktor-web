package com.example.infra.redisson

object RedissonLock {

    private val client get() = RedissonManager.getClient()

    fun <T> withLock(
        key: String,
        waitTimeMs: Long = 0,
        leaseTimeMs: Long = 30000,
        block: () -> T
    ): T {
        val lock = client.getLock(key)

        val success = if (waitTimeMs > 0) {
            lock.tryLock(waitTimeMs, leaseTimeMs, java.util.concurrent.TimeUnit.MILLISECONDS)
        } else {
            lock.tryLock(leaseTimeMs, java.util.concurrent.TimeUnit.MILLISECONDS)
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