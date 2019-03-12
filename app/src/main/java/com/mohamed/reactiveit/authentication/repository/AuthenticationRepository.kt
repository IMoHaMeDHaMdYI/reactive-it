package com.mohamed.reactiveit.authentication.repository

import com.mohamed.reactiveit.common.api.ReactiveService
import com.mohamed.reactiveit.common.models.Result
import com.mohamed.reactiveit.common.models.User
import io.reactivex.Single

class AuthenticationRepository(val service: ReactiveService = ReactiveService.service) {

    fun getUser(userName: String): Single<Result<User>> {
        return service.getUser(userName)
    }
}