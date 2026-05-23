package com.example.feature.user.service

import com.example.feature.user.repo.AppUserRepo

class AppUserService(
    private val appUserRepo: AppUserRepo,
) {
    fun getUser(id: Long) {
        appUserRepo.getUser()
    }
}