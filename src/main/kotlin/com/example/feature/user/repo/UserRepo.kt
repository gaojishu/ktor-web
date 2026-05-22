package com.example.feature.user.repo

import com.example.jooq.generate.admin.tables.references.ADMIN_
import org.jooq.DSLContext

class UserRepo(
    private val dsl: DSLContext
) {

    fun getUser(){
        dsl.selectFrom(ADMIN_)
            .limit(1)
            .fetchOne()
    }
}