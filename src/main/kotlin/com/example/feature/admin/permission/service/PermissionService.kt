package com.example.feature.admin.permission.service

import com.example.feature.admin.permission.dto.PermissionDto
import com.example.feature.admin.permission.repo.PermissionRepo
import org.koin.core.annotation.Single

@Single
class PermissionService(
    private val permissionRepo: PermissionRepo
) {
    suspend fun create(req: PermissionDto) {
        permissionRepo.create(req)
    }
}