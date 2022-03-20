package com.kunsan.poetry.net

import com.kunsan.poetry.data.PoetryResponse
import com.kunsan.poetry.data.TokenResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface API {

    @GET("token")
    suspend fun token(): TokenResponse

    @GET("sentence")
    suspend fun poetry(@Header("X-User-Token") token: String): PoetryResponse


}