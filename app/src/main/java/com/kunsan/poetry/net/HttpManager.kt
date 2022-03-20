package com.kunsan.poetry.net

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HttpManager {

    // 连接时长
    val DEFAULT_CONNECT_TIMEOUT_MILLIS = 20 * 1000
    // 写入时长
    val DEFAULT_WIRTE_TIMEOUT_MILLS = 20 * 1000
    // 读取时长
    val DEFAULT_READ_TIMEOUOT_MILLS = 20 * 1000

    val logging =  HttpLoggingInterceptor();


    @Volatile
    private var mOkHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS.toLong(), TimeUnit.MILLISECONDS)
        .writeTimeout(DEFAULT_WIRTE_TIMEOUT_MILLS.toLong(), TimeUnit.MILLISECONDS)
        .readTimeout(DEFAULT_READ_TIMEOUOT_MILLS.toLong(), TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    fun getOkHttpClient(): OkHttpClient = mOkHttpClient


    private val webservice =
        Retrofit.Builder()
            .baseUrl("https://v2.jinrishici.com/")
            .client(mOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(API::class.java)

    fun getWebservice() = webservice

    fun newCall(request: Request) = mOkHttpClient.newCall(request)
}