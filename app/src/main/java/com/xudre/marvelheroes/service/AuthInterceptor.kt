package com.xudre.marvelheroes.service

import com.xudre.marvelheroes.config.ApiConfig
import com.xudre.marvelheroes.extension.hash
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*
import kotlin.math.roundToInt

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = signedRequest(chain.request())

        return chain.proceed(request)
    }

    private fun signedRequest(request: Request): Request {
        val timestamp = (Calendar.getInstance().timeInMillis / 1000f).roundToInt().toString()
        val hash = "$timestamp${ApiConfig.SECRET}${ApiConfig.KEY}".hash(ApiConfig.DIGEST)
        val url = request.url()
            .newBuilder()
            .addQueryParameter("ts", timestamp)
            .addQueryParameter("hash", hash)
            .addQueryParameter("apikey", ApiConfig.KEY)
            .build()

        return request.newBuilder()
            .url(url)
            .build()
    }
}
