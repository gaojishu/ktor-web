package com.example.feature.admin.captcha.dto

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class CaptchaDto(
    var uuid: Uuid? = null,

    var imageBase64Data: String,
)