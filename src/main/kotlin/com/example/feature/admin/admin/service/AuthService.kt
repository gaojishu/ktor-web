package com.example.feature.admin.admin.service

import com.example.common.exception.BusinessException
import com.example.common.functions.verifyArgon2
import com.example.feature.admin.admin.dto.AdminDto
import com.example.feature.admin.admin.dto.AuthLoginReq
import com.example.feature.admin.admin.dto.AuthLoginRes
import com.example.feature.admin.admin.repo.AdminRepo
import com.example.feature.admin.captcha.service.CaptchaService
import com.example.feature.admin.permission.dto.PermissionDto
import com.example.feature.admin.permission.repo.PermissionRepo
import com.example.infra.security.AdminJwtService
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class AuthService(
    private val adminRepo: AdminRepo,
    private val adminJwtService: AdminJwtService,
    private val captchaService: CaptchaService,
    private val permissionRepo: PermissionRepo
) {

    suspend fun info(id: Uuid): AdminDto? {
        return adminRepo.selectById(id)
    }

    suspend fun login(req: AuthLoginReq): AuthLoginRes {
        //验证码验证
        captchaService.verify(req.captchaUuid,req.captchaCode)

        val record = adminRepo.selectByUsername(req.username)

        //密码验证
        record?.password?.verifyArgon2(req.password) ?: throw BusinessException("用户名或密码错误")

        //用户状态验证
        record.status?.checkActive()

        val token = adminJwtService.generateToken(record.id.toString())
        return AuthLoginRes(
            token = token
        )
    }

    suspend fun logout() {

    }

    suspend fun permission(adminId: Uuid): List<PermissionDto> {
        val dto = permissionRepo.selectPermissionByAdminId(adminId)

        return dto
    }
}