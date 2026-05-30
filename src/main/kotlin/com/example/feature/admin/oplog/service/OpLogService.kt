package com.example.feature.admin.oplog.service

import com.example.common.dto.PageResult
import com.example.feature.admin.oplog.dto.OpLogDto
import com.example.feature.admin.oplog.dto.OpLogPageReq
import com.example.feature.admin.oplog.excel.OpLogExcelStreamWriter
import com.example.feature.admin.oplog.repo.OpLogRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import java.io.File
import kotlin.uuid.Uuid

@Single
class OpLogService(
    private val opLogRepo: OpLogRepo,
    private val opLogExcelStreamWriter: OpLogExcelStreamWriter
) {
    suspend fun page(req: OpLogPageReq): PageResult<OpLogDto> {
        val res = opLogRepo.searchPage(req.params, req.page)
        return res
    }

}