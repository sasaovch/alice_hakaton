package com.example.api_service

import cookies.CookieHandler
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.util.*

class AuthorizationHandler (cookies : CookieHandler) {
    //cookieJar(new SessionCookieJar())
    private val cookieStore = cookies.cookieStore
    private val cookieJar = cookies.cookies
    private var client =
        OkHttpClient.Builder().cookieJar(cookieJar).addInterceptor(LoggingInterceptor()).followRedirects(false)
            .followSslRedirects(false).build()
    private val retrofitIdItmo = Retrofit.Builder()
        .baseUrl("https://id.itmo.ru/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val retrofitIsu = Retrofit.Builder()
        .baseUrl("https://isu.ifmo.ru/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    fun loginAndGetApCookie(login: String, password: String): String {
        //todo NULL HANDLING
        val html = getLoginPage()!!
        val regex = "<form\\s+.*?\\s+action=\"(.*?)\"".toRegex()
        var action = regex.find(html)!!.groups[1]?.value!!
        //Why amp;?
        action = action.replace("amp;", "")

        login(login, password, action)

        return getIsuApCookie()
    }

    private fun getLoginPage(): String? {
        val request = retrofitIdItmo.create(AuthorizationApi::class.java)
        val call = request.getBasicCookies(code_challenge = generateCodeVerifier())
        val response = call.execute()
        return response.body()?.string()
    }

    private fun login(login: String, password: String, url: String) {
        val request = retrofitIdItmo.create(AuthorizationApi::class.java)
        val call = request.getRoomInfo(url, login, password, "")

        var response = call.execute()
        println(response.code())
        while (response.code() == 302) {
            response = request.redirectHandler(response.headers().get("LOCATION")!!).execute()
        }
    }

    private fun getIsuApCookie(): String {
        //todo NULL HANDLING
        var url = "https://isu.ifmo.ru/"
        val request = retrofitIdItmo.create(AuthorizationApi::class.java)
        var queryNumber = 8;
        var call = request.redirectHandler("https://isu.ifmo.ru/")
        var response = call.execute()
        for (i in 2..queryNumber) {
            if (response.headers().get("LOCATION")!!.startsWith("http")) {
                call = request.redirectHandler(response.headers().get("LOCATION")!!)
                response = call.execute()
            } else {
                call =
                    request.redirectHandler("https://isu.ifmo.ru/pls/apex/" + response.headers().get("LOCATION")!!)
                response = call.execute()

            }
        }
        for (i in cookieStore["isu.ifmo.ru"]!!) {
            if(i.name() == "ISU_AP_COOKIE") {
                return i.value()
            }
        }
        return ""

    }

    private fun generateCodeVerifier(): String {
        val random = SecureRandom()
        val codeVerifierBytes = ByteArray(40)
        random.nextBytes(codeVerifierBytes)
        val codeVerifier = Base64.getUrlEncoder().encodeToString(codeVerifierBytes)
        return codeVerifier.replace("[^a-zA-Z0-9]+".toRegex(), "")
    }

}