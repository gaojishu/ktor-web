package com.example.feature.admin.permission.repo

import com.example.common.exception.BusinessException
import com.example.feature.admin.admin.dto.AdminDto
import com.example.feature.admin.permission.dto.PermissionDto
import com.example.infra.database.tx
import com.example.jooq.generate.tables.references.ADMIN
import com.example.jooq.generate.tables.references.ADMIN_PERMISSION
import com.example.jooq.generate.tables.references.PERMISSION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.jooq.impl.DSL.multiset
import org.jooq.impl.DSL.select
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class PermissionRepo(
    private val dsl: DSLContext
) {

    suspend fun selectByParentId(parentId: Uuid): List<PermissionDto> {
        val dto = withContext(Dispatchers.IO) {
            dsl.selectFrom(PERMISSION)
                .where(PERMISSION.PARENT_ID.eq(parentId))
                .fetchInto(PermissionDto::class.java)
        }
        return dto
    }

    suspend fun selectById(id: Uuid): PermissionDto? {
        val dto = withContext(Dispatchers.IO) {
            dsl.selectFrom(PERMISSION)
                .where(PERMISSION.ID.eq(id))
                .fetchOneInto(PermissionDto::class.java)
        }
        return dto
    }

    suspend fun create(dto: PermissionDto){
        var parent: PermissionDto? = null
        if (dto.parentId != null) {
            parent = withContext(Dispatchers.IO) {
                selectById(dto.parentId!!)
            }
        }

        dsl.tx { ctx ->
            val record = ctx.newRecord(PERMISSION)
            record.icon = dto.icon
            record.name = dto.name
            record.parentId = dto.parentId
            record.path = dto.path
            record.remark = dto.remark
            record.sort = dto.sort
            record.type = dto.type
            record.code = dto.code
            record.level = parent?.level?.plus(1) ?: 1
            record.store()

            val newId = record.id!!
            record.key = parent?.key?.let { "$it-$newId" } ?: newId.toString()

            record.store()
        }

    }

    suspend fun updateById(dto: PermissionDto){
        var parent: PermissionDto? = null
        if (dto.parentId != null) {
            parent = selectById(dto.parentId!!)
        }

        val record = withContext(Dispatchers.IO) {
            dsl.fetchOne(PERMISSION, PERMISSION.ID.eq(dto.id))
        } ?: throw BusinessException("权限不存在")

        record.icon = dto.icon
        record.name = dto.name
        record.parentId = dto.parentId
        record.path = dto.path
        record.remark = dto.remark
        record.sort = dto.sort
        record.type = dto.type
        record.code = dto.code
        record.level = parent?.level?.plus(1) ?: 1
        record.key = parent?.key?.let { "$it-${record.id}" } ?: record.id.toString()

        record.store()
    }

    suspend fun list(): List<PermissionDto> {
        val dto = withContext(Dispatchers.IO) {
            dsl.selectFrom(PERMISSION)
                .orderBy(PERMISSION.SORT.asc(), PERMISSION.ID.asc())
                .fetchInto(PermissionDto::class.java)
        }
        return dto
    }

    suspend fun deleteById(id: Uuid) {
        val child = withContext(Dispatchers.IO) {
           selectByParentId(id)
        }

        if (child.isNotEmpty()) {
            throw BusinessException("请先删除子权限")
        }

        withContext(Dispatchers.IO) {
            dsl.deleteFrom(PERMISSION)
                .where(PERMISSION.ID.eq(id))
                .execute()
        }
    }

    suspend fun selectPermissionByAdminId(adminId: Uuid): List<PermissionDto> {
        var dto: List<PermissionDto> = emptyList()
        if(adminId == Uuid.parse("019d5228-5235-768e-8f49-6189373b7191")){
            dto = withContext(Dispatchers.IO) {
                dsl.selectFrom(PERMISSION)
                    .orderBy(PERMISSION.SORT.asc())
                    .fetchInto(PermissionDto::class.java)
            }

        }else{
            val admin = withContext(Dispatchers.IO) {
                dsl.select(
                    ADMIN,
                    multiset(
                        select(PERMISSION)
                            .from(PERMISSION)
                            .join(ADMIN_PERMISSION).on(PERMISSION.ID.eq(ADMIN_PERMISSION.PERMISSION_ID))
                            .where(ADMIN_PERMISSION.ADMIN_ID.eq(ADMIN.ID))
                            .orderBy(PERMISSION.SORT.asc())

                    ).`as`("permission")
                        .convertFrom { result -> result.into(PermissionDto::class.java) }
                ).from(ADMIN)
                    .where(ADMIN.ID.eq(adminId))
                    .fetchOneInto(AdminDto::class.java)
            }

            admin?.let { dto = it.permission }
        }

        return dto
    }

}