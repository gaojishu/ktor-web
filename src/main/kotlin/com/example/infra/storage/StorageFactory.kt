package com.example.infra.storage

import com.example.infra.storage.enums.StorageProviderEnum
import com.example.infra.storage.impl.AliyunStorageProviderImpl


object StorageFactory {
    private val providers = mutableMapOf<StorageProviderEnum, StorageProvider>()
    var defaultProvider: StorageProviderEnum = StorageProviderEnum.ALIYUN
        private set

    // 在 Ktor 启动时调用此方法初始化
    fun initialize(config: io.ktor.server.config.ApplicationConfig) {
        val aliyunConfig = config.config("storage.aliyun")
        providers[StorageProviderEnum.ALIYUN] = AliyunStorageProviderImpl(
            endpoint = aliyunConfig.property("endpoint").getString(),
            accessKeyId = aliyunConfig.property("accessKeyId").getString(),
            accessKeySecret = aliyunConfig.property("accessKeySecret").getString(),
            bucketName = aliyunConfig.property("bucket").getString()
        )


        val defaultStr = config.propertyOrNull("storage.default")?.getString()?.uppercase()
        if (defaultStr != null) {
            defaultProvider = StorageProviderEnum.valueOf(defaultStr)
        }
    }

    // 根据枚举获取指定服务商，如果不传则返回默认服务商
    fun getProvider(provider: StorageProvider? = null): StorageProvider {
        val target = provider ?: defaultProvider
        return providers[target] ?: throw IllegalArgumentException("服务商 $target 未初始化")
    }
}
