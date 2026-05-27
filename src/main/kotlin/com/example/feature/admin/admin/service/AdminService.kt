package com.example.feature.admin.admin.service

import com.example.feature.admin.admin.dto.AdminDto
import com.example.feature.admin.admin.dto.AdminPageReq
import com.example.feature.admin.admin.repo.AdminRepo
import com.example.common.dto.PageResult
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class AdminService(
    private val adminRepo: AdminRepo,
) {

    suspend fun page(req: AdminPageReq): PageResult<AdminDto> {
        return adminRepo.searchPage(req.params, req.page)
    }

    suspend fun detail(id: Uuid): AdminDto? {
        return adminRepo.selectById(id)
    }
}