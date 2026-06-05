package com.example.infra.storage

import com.example.infra.storage.enums.StorageProviderEnum
import com.example.infra.storage.provider.StorageProvider
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class StorageFactory(
    providers: List<StorageProvider>,
    private val defaultProvider: StorageProviderEnum,
) {

    private val providerMap: Map<StorageProviderEnum, StorageProvider> = providers.associateBy { it.provider }

    fun getProvider(provider: StorageProviderEnum? = null): StorageProvider {
        val target = provider ?: defaultProvider
        return providerMap[target] ?: throw IllegalArgumentException("服务商 ${target.label} 未初始化")
    }
}
