package com.example.feature.admin.notice.dto

import com.example.common.dto.PageQuery
import kotlinx.serialization.Serializable

@Serializable
data class NoticePageReq(
    var params: NoticeQueryParams? = null,
    var page: PageQuery = PageQuery()
)



