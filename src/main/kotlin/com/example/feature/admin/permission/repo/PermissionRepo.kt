package com.example.feature.admin.permission.repo

import com.example.feature.admin.admin.dto.AdminDto
import com.example.feature.admin.permission.dto.PermissionDto
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

    suspend fun create(){
        val record = dsl.newRecord(PERMISSION)

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