package com.example.infra.redisson

import org.koin.mp.KoinPlatform.getKoin

fun <T> redissonLock(
    key: String,
    leaseMs: Long = 30000,
    block: () -> T
): T {
    val lock = getKoin().get<RedissonLock>()
    return lock.withLock(
        key = key,
        leaseTimeMs = leaseMs,
        block = block
    )
}