package com.example.feature.admin.oplog.repo

import com.example.common.dto.PageQuery
import com.example.common.dto.PageResult
import com.example.common.utils.log.log
import com.example.feature.admin.oplog.dto.OpLogDto
import com.example.feature.admin.oplog.dto.OpLogQueryParams
import com.example.feature.admin.oplog.query.OpLogQuery
import com.example.infra.database.pageInto
import com.example.infra.database.selectActiveFrom
import com.example.jooq.generate.tables.references.OP_LOG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class OpLogRepo(
    private val dsl: DSLContext,
    private val query: OpLogQuery
) {

    fun streamExportData(channel: SendChannel<OpLogDto>) {
        dsl.selectFrom(OP_LOG)
            .fetchStream().use { stream ->
                stream.forEach { record ->
                    val dto = record.into(OpLogDto::class.java)
                    log.info(dto.toString())
                    // 尝试发送，若通道满则挂起/阻塞
                    channel.trySend(dto)
                }
            }
    }

    suspend fun searchPage(params: OpLogQueryParams?,pageQuery: PageQuery): PageResult<OpLogDto> {

        val condition = query.buildCondition(params)
        val dto = withContext(Dispatchers.IO) {
            dsl.selectActiveFrom(OP_LOG)
                .and(condition)
                .orderBy(OP_LOG.ID.desc())
                .pageInto(dsl, pageQuery, OpLogDto::class.java)
        }

        return dto
    }

    suspend fun create(
        adminId: Uuid? = null,
        ip: String? = null,
        method: String? = null,
        uri: String? = null,
        params: String? = null,
        duration: Long? = null,
        remark: String? = null,
    ) {
        withContext(Dispatchers.IO) {
            dsl.insertInto(OP_LOG)
                .set(OP_LOG.ADMIN_ID, adminId)
                .set(OP_LOG.IP, ip)
                .set(OP_LOG.METHOD, method)
                .set(OP_LOG.URI, uri)
                .set(OP_LOG.DURATION, duration)
                .set(OP_LOG.REMARK, remark)
                .set(OP_LOG.PARAMS, params)
                .execute()
        }
    }
}