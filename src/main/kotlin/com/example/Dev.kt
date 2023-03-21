package com.example

import InfoHandler
import booking.BookingHandler
import com.example.alice_bot.channel
import com.example.api_service.AuthorizationHandler
import com.example.cookie.CookieHandler
import com.example.handler.RequestHandler
import com.example.models.*
import com.example.util.TimeParser
import com.justai.jaicf.channel.http.httpBotRouting
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.time.Month


/**
 * Используйте этот файл, чтобы тестировать ваш навык локально с использованием ngrok
 */

fun main() {
    embeddedServer(Netty, 8080) {
        routing {
            httpBotRouting("/" to channel)
        }
    }.start(wait = true)
}


//fun main() {
//    val user = RequestHandler.auth(User("", "", ""))
//    val ans1 = RequestHandler.getFreeRooms(Room(Place.KRONVERSKY, Pair(10, 0), 30, Month.MARCH, null, null,1311, RoomType.MEETINGROOM, 30, DayOfWeek.NONE, 5), user)
//    println(ans1)
//    val ans2 = RequestHandler.bookRoom(Room(Place.KRONVERSKY, Pair(10, 0), 30, Month.MARCH, null, null,1311, RoomType.MEETINGROOM, 90, DayOfWeek.NONE, 5), user)

//    var place: Place? = null,
    //    var time: Pair<Int, Int>? = null,
    //    var day: Int? = null,
    //    var month: Month? = null,
    //    var roomId: Int? = 0,
    //    var type: RoomType = RoomType.NONE,
    //    var duration: Int? = 30,
    //    var today: DayOfWeek = DayOfWeek.NONE,
    //    var numberMem: Int = 0
//}