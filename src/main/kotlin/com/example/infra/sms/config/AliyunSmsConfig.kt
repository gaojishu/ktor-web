package com.example.infra.sms.config

import io.ktor.server.config.ApplicationConfig

data class AliyunSmsConfig(
    val accessKeyId: String,
    val accessKeySecret: String,
    val endpoint: String,
    val regionId: String,
    val signName: String,
) {
    companion object {
        fun fromConfig(config: ApplicationConfig): AliyunSmsConfig {
            val aliyunSmsConfig = config.config("sms.aliyun")
            return AliyunSmsConfig(
                accessKeyId = aliyunSmsConfig.property("accessKeyId").getString(),
                accessKeySecret = aliyunSmsConfig.property("accessKeySecret").getString(),
                endpoint = aliyunSmsConfig.property("endpoint").getString(),
                regionId = aliyunSmsConfig.property("regionId").getString(),
                signName = aliyunSmsConfig.property("signName").getString(),
            )
        }
    }
}