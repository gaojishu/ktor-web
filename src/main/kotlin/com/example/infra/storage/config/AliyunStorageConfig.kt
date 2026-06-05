package com.example.infra.storage.config

import io.ktor.server.config.ApplicationConfig

data class AliyunStorageConfig(
    val endpoint: String,
    val accessKeyId: String,
    val accessKeySecret: String,
    val bucket: String,
    val uploadPrefix: String,
) {
    companion object {
        fun fromConfig(config: ApplicationConfig): AliyunStorageConfig {
            val aliyunConfig = config.config("storage.aliyun")
            return AliyunStorageConfig(
                endpoint = aliyunConfig.property("endpoint").getString(),
                accessKeyId = aliyunConfig.property("accessKeyId").getString(),
                accessKeySecret = aliyunConfig.property("accessKeySecret").getString(),
                bucket = aliyunConfig.property("bucket").getString(),
                uploadPrefix = aliyunConfig.property("uploadPrefix").getString(),
            )
        }
    }
}