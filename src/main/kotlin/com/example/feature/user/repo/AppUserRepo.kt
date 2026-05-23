package com.example.feature.user.repo

import com.example.jooq.generate.tables.references.ADMIN
import org.jooq.DSLContext

class AppUserRepo(
    private val dsl: DSLContext
) {

    fun getUser(){
        dsl.selectFrom(ADMIN)
            .limit(1)
            .fetchOne()
    }
}