package com.example.feature.admin.admin.service

import com.example.feature.admin.admin.dto.db.AdminDto
import com.example.feature.admin.admin.repo.AdminRepo
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class AdminService(
    private val adminRepo: AdminRepo,
) {

    suspend fun detail(id: UUID): AdminDto? {
        return adminRepo.getById(id)?.into(AdminDto::class.java)
    }
}