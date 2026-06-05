package com.example.infra.redis

import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import org.apache.commons.pool2.impl.GenericObjectPool
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class RedisBootstrap(
    val redisClient: RedisClient,
    pool: GenericObjectPool<StatefulRedisConnection<String, String>>,
)
