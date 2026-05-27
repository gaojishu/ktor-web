package com.example.feature.admin.role.repo

import com.example.common.exception.BusinessException
import com.example.feature.admin.role.dto.RoleDto
import com.example.jooq.generate.tables.references.ROLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class RoleRepo(
    private val dsl: DSLContext,
) {
    suspend fun list(): List<RoleDto> {
        val dto = withContext(Dispatchers.IO) {
            dsl.selectFrom(ROLE)
                .fetchInto(RoleDto::class.java)
        }
        return dto
    }

    suspend fun create(dto: RoleDto): RoleDto {
        val record = dsl.newRecord(ROLE)
        record.name = dto.name
        record.remark = dto.remark
        record.permissionKey = dto.permissionKey?.toTypedArray()
        record.store()
        return record.into(RoleDto::class.java)
    }

    suspend fun updateById(dto: RoleDto): RoleDto {
        val record = withContext(Dispatchers.IO) {
            dsl.fetchOne(ROLE, ROLE.ID.eq(dto.id))
        } ?: throw BusinessException("权限模版不存在")

        record.name = dto.name
        record.remark = dto.remark
        record.permissionKey = dto.permissionKey?.toTypedArray()
        record.store()
        return record.into(RoleDto::class.java)
    }

    suspend fun deleteById(id: Uuid) {
        withContext(Dispatchers.IO) {
            dsl.deleteFrom(ROLE)
                .where(ROLE.ID.eq(id))
                .execute()
        }
    }

}