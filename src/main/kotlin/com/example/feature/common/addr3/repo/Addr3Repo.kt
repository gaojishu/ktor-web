package com.example.feature.common.addr3.repo

import com.example.feature.common.addr3.dto.Addr3Dto
import com.example.jooq.generate.tables.references.ADDR3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.koin.core.annotation.Single

@Single
class Addr3Repo(
    private val dsl: DSLContext,
) {
    suspend fun list(): List<Addr3Dto> {
        return withContext(Dispatchers.IO) {
            dsl.selectFrom(ADDR3)
                .orderBy(ADDR3.ID.asc())
                .fetchInto(Addr3Dto::class.java)
        }
    }
}
