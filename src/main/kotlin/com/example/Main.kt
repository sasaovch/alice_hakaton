package com.example
import com.example.api_service.AuthorizationHandler
import com.example.booking.BookingHandler
import com.example.cookie.CookieHandler
import info.InfoHandler
import java.net.URLEncoder

fun main() {
    val cookies = CookieHandler();
    val au = AuthorizationHandler(cookies)
    val test1 = au.loginAndGetApCookie("LOGIN", "PASSWORD")
    println(test1)
    val infoHandler = InfoHandler("ISU_AP_COOKIE=$test1")
    infoHandler.checkInstance()
    val res2 =
        infoHandler.getFreeRooms(InfoHandler.Place.KRONVA, "15:00-15:30", "25.03.2023", InfoHandler.Type.MEETING_ROOM)
    println(res2)
    val bookingHandler = BookingHandler(cookies, infoHandler.p_instance)
    bookingHandler.test(18863, "25.03.2023", "15:00", "15:30")
    return
}

