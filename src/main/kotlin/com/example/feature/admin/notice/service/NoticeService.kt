package com.example.feature.admin.notice.service

import com.example.common.dto.PageResult
import com.example.feature.admin.notice.dto.NoticePageReq
import com.example.feature.admin.notice.repo.NoticeRepo
import com.example.feature.admin.oplog.dto.OpLogDto
import org.koin.core.annotation.Single

@Single
class NoticeService(
    private val noticeRepo: NoticeRepo
) {
    suspend fun page(req: NoticePageReq): PageResult<OpLogDto> {
        val res = noticeRepo.searchPage(req.params, req.page)
        return res
    }
}