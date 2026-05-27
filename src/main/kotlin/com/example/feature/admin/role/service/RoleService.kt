package com.example.feature.admin.role.service

import com.example.feature.admin.role.dto.RoleDto
import com.example.feature.admin.role.repo.RoleRepo
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class RoleService(
    private val roleRepo: RoleRepo
) {
    suspend fun create(req: RoleDto): RoleDto {
        return roleRepo.create(req)
    }

    suspend fun update(req: RoleDto){
        roleRepo.updateById(req)
    }

    suspend fun delete(id: Uuid){
        roleRepo.deleteById(id)
    }

    suspend fun list(): List<RoleDto>{
        return roleRepo.list()
    }
}