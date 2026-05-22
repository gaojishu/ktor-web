package com.example.infra.redisson

fun <T> redissonLock(
    key: String,
    leaseMs: Long = 30000,
    block: () -> T
): T {
    return RedissonLock.withLock(
        key = key,
        leaseTimeMs = leaseMs,
        block = block
    )
}