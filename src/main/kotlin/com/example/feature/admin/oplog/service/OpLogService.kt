package com.example.feature.admin.oplog.service

import com.example.common.dto.PageResult
import com.example.feature.admin.oplog.dto.OpLogDto
import com.example.feature.admin.oplog.dto.OpLogPageReq
import com.example.feature.admin.oplog.repo.OpLogRepo
import org.koin.core.annotation.Single

@Single
class OpLogService(
    private val opLogRepo: OpLogRepo,
) {
    suspend fun page(req: OpLogPageReq): PageResult<OpLogDto> {
        val res = opLogRepo.searchPage(req.params, req.page)
        return res
    }

}