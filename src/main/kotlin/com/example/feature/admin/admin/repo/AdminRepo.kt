package com.example.feature.admin.admin.repo

import com.example.feature.admin.admin.dto.AdminDto
import com.example.feature.admin.admin.dto.AdminPageReq
import com.example.feature.admin.admin.dto.AdminQueryParams
import com.example.feature.admin.admin.query.AdminQuery
import com.example.infra.database.dto.PageQuery
import com.example.infra.database.dto.PageResult
import com.example.infra.database.pageInto
import com.example.infra.database.selectActiveFrom
import com.example.jooq.generate.tables.records.AdminRecord
import com.example.jooq.generate.tables.references.ADMIN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class AdminRepo(
    private val dsl: DSLContext,
    private val query: AdminQuery
) {

    suspend fun searchPage(params: AdminQueryParams?,pageQuery: PageQuery): PageResult<AdminDto> {

        val condition = query.buildCondition(params)
        val dto = withContext(Dispatchers.IO) {
            dsl.selectActiveFrom(ADMIN)
                .and(condition)
                .pageInto(dsl, pageQuery,AdminDto::class.java)
        }

        return dto
    }

    suspend fun selectByUsername(username: String): AdminRecord? {
        val record = withContext(Dispatchers.IO) {
            dsl.selectActiveFrom(ADMIN)
                .and(ADMIN.USERNAME.eq(username))
                .fetchOne()
        }
        return record
    }

    suspend fun selectById(id: UUID): AdminDto? {
        val dto = withContext(Dispatchers.IO) {
            dsl.selectActiveFrom(ADMIN)
                .and(ADMIN.ID.eq(id))
                .fetchOneInto(AdminDto::class.java)
        }
        return dto
    }

    suspend fun create(): PageResult<AdminDto> {
        val query = PageQuery(page = 1, size = 10) // 假设你的分页入参对象

        val dto = withContext(Dispatchers.IO) {
            dsl.selectActiveFrom(ADMIN)
                .pageInto(dsl, query,AdminDto::class.java)
        }

        return dto
    }

    suspend fun updateById(id: UUID): PageResult<AdminDto> {
        val query = PageQuery(page = 1, size = 10) // 假设你的分页入参对象

        val dto = withContext(Dispatchers.IO) {
            dsl.selectActiveFrom(ADMIN)
                .pageInto(dsl, query,AdminDto::class.java)
        }

        return dto
    }

    suspend fun deleteById(id: UUID): PageResult<AdminDto> {
        val query = PageQuery(page = 1, size = 10) // 假设你的分页入参对象

        val dto = withContext(Dispatchers.IO) {
            dsl.selectActiveFrom(ADMIN)
                .pageInto(dsl, query,AdminDto::class.java)
        }

        return dto
    }

}