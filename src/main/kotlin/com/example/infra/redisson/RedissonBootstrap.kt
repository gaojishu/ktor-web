package com.example.infra.redisson

import org.koin.core.annotation.Single
import org.redisson.api.RedissonClient

@Single(createdAtStart = true)
class RedissonBootstrap(
    val client: RedissonClient,
)
