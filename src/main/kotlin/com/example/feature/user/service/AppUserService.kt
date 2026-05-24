package com.example.feature.user.service

import com.example.feature.user.repo.AppUserRepo
import org.koin.core.annotation.Single

@Single
class AppUserService(
    private val appUserRepo: AppUserRepo,
) {
    fun getUser(id: Long) {
        appUserRepo.getUser()
    }
}