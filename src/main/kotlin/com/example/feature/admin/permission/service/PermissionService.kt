package com.example.feature.admin.permission.service

import com.example.feature.admin.permission.repo.PermissionRepo
import org.koin.core.annotation.Single

@Single
class PermissionService(
    private val permissionRepo: PermissionRepo
) {
    suspend fun create(){
        permissionRepo.create()
    }
}