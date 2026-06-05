package com.example.infra.storage.dto

import kotlinx.serialization.Serializable

@Serializable
data class OssUploadPolicy(
    var accessId: String,   
    var host: String,
    var policy: String,
    var signature: String,
    var expire: String,
    var prefix: String,
)