package com.example.infra.storage

import com.example.infra.storage.enums.StorageProviderEnum
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class StorageFactory(
    private val providers: Map<StorageProviderEnum, StorageProvider>,
    private val defaultProvider: StorageProviderEnum,
) {
    fun getProvider(provider: StorageProviderEnum? = null): StorageProvider {
        val target = provider ?: defaultProvider
        return providers[target] ?: throw IllegalArgumentException("服务商 $target 未初始化")
    }
}
