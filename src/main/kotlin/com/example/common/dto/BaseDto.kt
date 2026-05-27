package com.example.common.dto

import com.example.common.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlin.uuid.Uuid

// 保持 @Serializable，Kotlinx 会把它当成多态基类处理
@Serializable
abstract class BaseDto(
    open var id: Uuid? = null,

    @Serializable(with = LocalDateTimeSerializer::class)
    open var createdAt: LocalDateTime? = null,

    @Serializable(with = LocalDateTimeSerializer::class)
    open var updatedAt: LocalDateTime? = null
)
