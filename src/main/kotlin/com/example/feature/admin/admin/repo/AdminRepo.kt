package com.example.feature.admin.admin.repo

import com.example.infra.database.selectActiveFrom
import com.example.jooq.generate.tables.records.AdminRecord
import com.example.jooq.generate.tables.references.ADMIN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class AdminRepo(
    private val dsl: DSLContext
) {
    suspend fun getById(id: UUID): AdminRecord? {
        val record = withContext(Dispatchers.IO) {
            dsl.selectActiveFrom(ADMIN)
                //.and(ADMIN.ID.eq(id))
                .fetchOne()
        }
        return record
    }

}