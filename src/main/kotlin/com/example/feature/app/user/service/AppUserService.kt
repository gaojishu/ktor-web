package com.example.feature.app.user.service

import com.example.feature.app.user.repo.AppUserRepo
import org.koin.core.annotation.Single

@Single
class AppUserService(
    private val appUserRepo: AppUserRepo,
) {

}