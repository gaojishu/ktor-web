package com.example.feature.admin.permission.service

import com.example.feature.admin.permission.dto.PermissionDto
import com.example.feature.admin.permission.repo.PermissionRepo
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class PermissionService(
    private val permissionRepo: PermissionRepo
) {
    suspend fun create(req: PermissionDto) {
        permissionRepo.create(req)
    }

    suspend fun update(req: PermissionDto) {
        permissionRepo.updateById(req)
    }

    suspend fun delete(id: Uuid) {
        permissionRepo.deleteById(id)
    }

    suspend fun list(): List<PermissionDto> {
        return permissionRepo.list()
    }
}