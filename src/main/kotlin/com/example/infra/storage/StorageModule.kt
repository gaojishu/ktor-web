package com.example.infra.storage

import com.example.infra.storage.config.AliyunStorageConfig
import com.example.infra.storage.enums.StorageProviderEnum
import io.ktor.server.config.ApplicationConfig
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class StorageModule {

    @Single
    fun provideAliyunStorageConfig(config: ApplicationConfig): AliyunStorageConfig {
        return AliyunStorageConfig.fromConfig(config)
    }

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
