package com.example.cookie

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieHandler {
    val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    val cookies = object : CookieJar {


        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            if(cookieStore[url.host()] == null) {
                cookieStore[url.host()] = cookies.toMutableList()
            } else {
                for(cookie in cookies) {
                    cookieStore[url.host()]?.removeAll{it.name() == cookie.name()}
                    cookieStore[url.host()]?.add(cookie)

                }
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            println(cookieStore[url.host()])
            return cookieStore[url.host()] ?: emptyList()
        }
    }


}