package com.example.common.dto

import com.example.common.serializer.LocalDateTimeSerializer
import com.example.common.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

// 保持 @Serializable，Kotlinx 会把它当成多态基类处理
@Serializable
abstract class BaseDto(
    @Serializable(with = UUIDSerializer::class)
    open var id: UUID? = null,

    @Serializable(with = LocalDateTimeSerializer::class)
    open var createdAt: LocalDateTime? = null,

    @Serializable(with = LocalDateTimeSerializer::class)
    open var updatedAt: LocalDateTime? = null
)
