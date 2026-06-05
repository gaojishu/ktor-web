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

    suspend fun streamExportData(params: OpLogQueryParams?, channel: SendChannel<OpLogDto>) = withContext(Dispatchers.IO) {
        val condition = query.buildCondition(params)

        // 1. 获取底层的 Java 游标/流
        val stream = dsl.selectFrom(OP_LOG)
            .where(condition)
            .orderBy(OP_LOG.ID.desc())
            .fetchStream()

        stream.use { stream ->
            // 2. 将流的指针拿到，直接在当前的 IO 挂起协程作用域下进行最纯粹的 while 循环
            val iterator = stream.iterator()
            while (iterator.hasNext()) {
                val record = iterator.next()
                val dto = record.into(OpLogDto::class.java)

                // ✨ 此时处于最纯粹的 suspend 函数体第一层，没有任何 Java Lambda 阻隔，
                // channel.send 绝对可以正常编译并安全运行！
                channel.send(dto)
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