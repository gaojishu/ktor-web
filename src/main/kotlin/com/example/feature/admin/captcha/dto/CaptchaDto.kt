package com.example.feature.admin.captcha.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.uuid.Uuid

@Serializable
data class CaptchaDto(
    @Contextual
    var uuid: UUID? = null,

    var imageBase64Data: String,
)