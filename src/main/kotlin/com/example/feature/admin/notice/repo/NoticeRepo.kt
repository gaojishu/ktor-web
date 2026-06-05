package com.example.feature.admin.notice.repo

import com.example.common.dto.PageQuery
import com.example.common.dto.PageResult
import com.example.feature.admin.notice.dto.NoticeDto
import com.example.feature.admin.notice.dto.NoticeQueryParams
import com.example.feature.admin.notice.query.NoticeQuery
import com.example.infra.database.pageInto
import com.example.infra.database.selectActiveFrom
import com.example.jooq.generate.tables.references.NOTICE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class NoticeRepo(
    private val dsl: DSLContext,
    private val query: NoticeQuery
) {

    suspend fun searchPage(params: NoticeQueryParams?,pageQuery: PageQuery): PageResult<NoticeDto> {

        val condition = query.buildCondition(params)
        val dto = withContext(Dispatchers.IO) {
            dsl.selectActiveFrom(NOTICE)
                .and(condition)
                .pageInto(dsl, pageQuery, NoticeDto::class.java)
        }

        return dto
    }

    suspend fun create(
        adminId: Uuid? = null,
        title: String,
        content: String,
        attachments: List<String>
    ) {
        withContext(Dispatchers.IO) {
            val record = dsl.newRecord(NOTICE)
            record.adminId = adminId
            record.title = title
            record.content = content
            record.attachments = attachments.toTypedArray()
            record.store()
        }
    }
}