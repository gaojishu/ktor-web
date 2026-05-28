package com.example.feature.admin.oplog.repo

import com.example.common.dto.PageQuery
import com.example.common.dto.PageResult
import com.example.feature.admin.oplog.dto.OpLogDto
import com.example.feature.admin.oplog.dto.OpLogQueryParams
import com.example.feature.admin.oplog.query.OpLogQuery
import com.example.infra.database.pageInto
import com.example.infra.database.selectActiveFrom
import com.example.jooq.generate.tables.references.OP_LOG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.koin.core.annotation.Single

@Single
class OpLogRepo(
    private val dsl: DSLContext,
    private val query: OpLogQuery
) {

    suspend fun searchPage(params: OpLogQueryParams?,pageQuery: PageQuery): PageResult<OpLogDto> {

        val condition = query.buildCondition(params)
        val dto = withContext(Dispatchers.IO) {
            dsl.selectActiveFrom(OP_LOG)
                .and(condition)
                .pageInto(dsl, pageQuery, OpLogDto::class.java)
        }

        return dto
    }
}