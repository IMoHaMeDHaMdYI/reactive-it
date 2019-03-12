package com.mohamed.reactiveit.common.api

import com.mohamed.reactiveit.common.models.Result
import com.mohamed.reactiveit.common.models.User
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

const val BASE_URL = "http://10.0.2.2:8000/db/"

interface ReactiveService {

    @GET("get-user")
    fun getUser(@Query("user") userName: String): Single<Result<User>>

    @POST("signup")
    fun signUp(@Body user: User): Single<Result<String>>

    companion object {
        val service = Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReactiveService::class.java)
    }
}
