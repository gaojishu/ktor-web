package com.example.infra.storage

import com.example.infra.storage.enums.StorageProviderEnum
import com.example.infra.storage.impl.AliyunStorageProviderImpl
import io.ktor.server.config.ApplicationConfig
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class StorageModule {

    @Single
    fun provideAliyunStorageProvider(config: ApplicationConfig): AliyunStorageProviderImpl {
        val aliyunConfig = config.config("storage.aliyun")
        return AliyunStorageProviderImpl(
            endpoint = aliyunConfig.property("endpoint").getString(),
            accessKeyId = aliyunConfig.property("accessKeyId").getString(),
            accessKeySecret = aliyunConfig.property("accessKeySecret").getString(),
            bucket = aliyunConfig.property("bucket").getString(),
            uploadPrefix = aliyunConfig.property("uploadPrefix").getString(),
        )
    }

    @Single
    fun provideStorageProviders(
        aliyun: AliyunStorageProviderImpl,
    ): Map<StorageProviderEnum, StorageProvider> = mapOf(
        StorageProviderEnum.ALIYUN to aliyun,
    )

    @Single
    fun provideDefaultStorageProvider(config: ApplicationConfig): StorageProviderEnum {
        val defaultStr = config.propertyOrNull("storage.default")?.getString()?.uppercase()
        return if (defaultStr != null) {
            StorageProviderEnum.valueOf(defaultStr)
        } else {
            StorageProviderEnum.ALIYUN
        }
    }

}
