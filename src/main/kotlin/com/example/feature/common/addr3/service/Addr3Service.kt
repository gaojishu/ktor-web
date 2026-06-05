package com.example.feature.common.addr3.service

import com.example.feature.common.addr3.dto.Addr3Dto
import com.example.feature.common.addr3.repo.Addr3Repo
import org.koin.core.annotation.Single

@Single
class Addr3Service(
    private val addr3Repo: Addr3Repo,
) {
    suspend fun list(): List<Addr3Dto> {
        return addr3Repo.list()
    }
}
