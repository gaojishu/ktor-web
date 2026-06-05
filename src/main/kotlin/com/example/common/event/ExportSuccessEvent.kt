package com.example.common.event

import io.ktor.events.EventDefinition
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class ExportSuccessDto(
    val userId: Uuid,
    val title: String,
    val content: String,
    val fileKey: String,
    val totalRows: Long,
)


val ExportSuccessEvent: EventDefinition<ExportSuccessDto> = EventDefinition()
