package com.example.feature.app.user.repo

import org.jooq.DSLContext
import org.koin.core.annotation.Single

@Single
class AppUserRepo(
    private val dsl: DSLContext
) {


}