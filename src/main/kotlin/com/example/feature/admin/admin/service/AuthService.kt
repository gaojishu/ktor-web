package com.example.feature.admin.admin.service

import com.example.feature.admin.admin.dto.AdminDto
import com.example.feature.admin.admin.dto.AuthLoginReq
import com.example.feature.admin.admin.repo.AdminRepo
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class AuthService(
    private val adminRepo: AdminRepo,
) {

    suspend fun info(id: UUID): AdminDto? {
        return adminRepo.getById(id)
    }

    suspend fun login(req: AuthLoginReq): AdminDto? {
        return adminRepo.getByUsername(req.username)
    }
}