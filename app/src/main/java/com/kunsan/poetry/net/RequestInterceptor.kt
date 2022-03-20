package com.kunsan.poetry.net

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor:Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.header("X-User-Token")
        return chain.proceed(request)
    }
}