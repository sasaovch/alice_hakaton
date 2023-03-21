package com.example

import InfoHandler
import com.example.api_service.AuthorizationHandler
import com.example.booking.BookingHandler
import com.example.cookie.CookieHandler
import com.example.models.Place
import com.example.models.RoomType


/**
 * Используйте этот файл, чтобы тестировать ваш навык локально с использованием ngrok
 */
/*
fun main() {
    embeddedServer(Netty, 8080) {
        routing {
            httpBotRouting("/" to channel)
        }
    }.start(wait = true)
}*/


fun main() {
    val cookies = CookieHandler();
    val au = AuthorizationHandler(cookies)
    val test1 = au.loginAndGetApCookie("336439", "Jivoglot=))0")
    println(test1)
    val infoHandler = InfoHandler("ISU_AP_COOKIE=$test1")
    infoHandler.checkInstance()
    val res2 =
        infoHandler.getFreeRoomsByPeopleNum(Place.LOMONOSOVA, listOf("21:00-21:30", "21:30-22:00"), "28.03.2023", RoomType.MEETINGROOM, 30)
    println(res2)
    return
}