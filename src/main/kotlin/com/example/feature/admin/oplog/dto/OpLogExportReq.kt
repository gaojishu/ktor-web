package com.example.feature.admin.oplog.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpLogExportReq(
    var params: OpLogQueryParams? = null
)



