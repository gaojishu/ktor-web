package com.example.feature.admin.admin.dto.db

import com.example.common.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
abstract class BaseDto{
    var id: String? = null

    @Serializable(with = LocalDateTimeSerializer::class)
    var createdAt: LocalDateTime? = null

    @Serializable(with = LocalDateTimeSerializer::class)
    var updatedAt: LocalDateTime? = null
}
