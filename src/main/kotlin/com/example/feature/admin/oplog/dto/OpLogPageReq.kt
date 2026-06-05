package com.example.feature.admin.oplog.dto

import com.example.common.dto.PageQuery
import kotlinx.serialization.Serializable

@Serializable
data class OpLogPageReq(
    var params: OpLogQueryParams? = null,
    var page: PageQuery = PageQuery()
)



