package com.example
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

class LoggingInterceptor : Interceptor {
    private val charset = Charset.forName("UTF-8")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // Log the request headers
        val requestBody = request.body()
        val requestBodyString = requestBody?.let { bodyToString(it) } ?: ""

        println(
            "Sending request to ${request.url()} with body: ${requestBodyString}")
            // Proceed with the request
            val response = chain.proceed (request)
            return response
    }

    private fun bodyToString(request: RequestBody): String {
        val buffer = Buffer()
        request.writeTo(buffer)
        return buffer.readString(charset)
    }
}