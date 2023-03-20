package com.example.util

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer

class EncodingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body()?.let { body ->
            val contentType = body.contentType()
            val newContent = bodyToString(request)
                .replace("%26", "&")
                .replace("%3D", "=")
            RequestBody.create(contentType, newContent)
        }
        val newRequestBuilder = Request.Builder()
            .url(request.url())
            .method(request.method(), requestBody)
            .headers(request.headers())

        val newRequest = newRequestBuilder.build()

        return chain.proceed(newRequest)

    }
    private fun bodyToString(request: Request): String {
        val buffer = Buffer()
        request.body()?.writeTo(buffer)
        return buffer.readUtf8()
    }
}