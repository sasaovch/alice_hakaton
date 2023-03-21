package com.example.handler

import com.example.api_service.AuthorizationHandler
import com.example.api_service.InfoHandler
import com.example.booking.BookingHandler
import com.example.cookie.CookieHandler
import com.example.models.*
import com.example.util.checkDay
import com.example.util.convertDateToDDMMYYYYFormat
import com.example.util.convertTimeToHHMMSSFormat
import com.google.gson.Gson
import java.text.DateFormatSymbols
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.util.Collections
import java.util.Date



class RequestHandler {
    private val infoHandler : InfoHandler = InfoHandler;
    fun getFreeRooms(room: Room): List<Room> {
        //FIXME: change time Pair to minute and hour
        val timeString = convertTimeToHHMMSSFormat(room.time!!)
        println(timeString)
        val dateString = convertDateToDDMMYYYYFormat(room.day!!, room.month!!)
        println(dateString)
        val listOfId = infoHandler.getFreeRooms(room.place!!, timeString, dateString, room.type)
        return listOfId.stream().map { it -> Room(place = room.place, time = room.time, day = room.day, month= room.month, roomId = it, type = room.type) }.limit(3).toList()
    }

    fun authUser(login: String, password: String, phone: String): Boolean {
    return true
    }

    fun bookRoom(room: Room): List<Room> {
        val cookies = CookieHandler();
        val au = AuthorizationHandler(cookies)
        val test1 = au.loginAndGetApCookie("LOGIN", "PASSWORD")
        println(test1)
        val infoHandler = info.InfoHandler("ISU_AP_COOKIE=$test1")
        infoHandler.checkInstance()
        val res2 =
            infoHandler.getFreeRooms(info.InfoHandler.Place.KRONVA, "15:00-15:30", "25.03.2023", info.InfoHandler.Type.MEETING_ROOM)
        println(res2)
        val bookingHandler = BookingHandler(cookies, infoHandler.p_instance)
        bookingHandler.test(room.roomId, convertDateToDDMMYYYYFormat(room.day!!, room.month!!), convertTimeToHHMMSSFormat(room.time!!), convertTimeToHHMMSSFormat(room.time!!))
        return emptyList()
//        val timeString = convertTimeToHHMMSSFormat(room.time!!)
//        println(timeString)
//        val dateString = convertDateToDDMMYYYYFormat(room.day!!, room.month!!)
//        println(dateString)
//        val listOfId = infoHandler.(room.place!!, timeString, dateString, room.type)
//        return listOfId.stream().map { it -> Room(room.place, room.time, room.day, room.month, it, room.type) }.limit(3).toList()
    }

    private fun parseTime(strTime: String): Pair<Int, Int>? {
        val timeArray = strTime.split(" ")
        return if (timeArray[0].toIntOrNull() != null && timeArray[1].toIntOrNull() != null) {
            Pair(timeArray[0].toInt(), timeArray[1].toInt())
        } else {
            null
        }
    }

    fun getTimeToBook(time: String, hour: String = "null", minute: String = "null", month: String = "null", day: String = "null"): RoomResponce {
        val room = Room()
        var error = ErrorTypeResponse.SUCCESS
        val timeToBook = Gson().fromJson(time, TimeToBook::class.java)
        if (timeToBook == null) {
            if (hour != "null") {
                room.hour = hour.toInt()
            }
            if (minute != "null") {
                room.minute = minute.toInt()
            }
            if (month != "null") {
                room.month = Month.valueOf(month)
            }
            if (day != "null") {
                room.day = day.toInt()
            }
        } else {
            if (timeToBook.hour != null) {
                if (timeToBook.hourIsRelative) {
                    room.hour = LocalTime.now().plusHours(timeToBook.hour).hour
                } else {
                    room.hour = timeToBook.hour.toInt()
                }
                room.minute = 0
            } else if (hour != "null") {
                room.hour = hour.toInt()
            }
            if (timeToBook.minute != null) {
                if (timeToBook.minuteIsRelative) {
                    room.minute = LocalTime.now().plusMinutes(timeToBook.minute).minute
                    room.hour = LocalTime.now().plusMinutes(timeToBook.minute).hour
                } else {
                    room.minute = timeToBook.minute.toInt()
                }
            } else if (minute != "null") {
                room.minute = minute.toInt()
            }
            if (timeToBook.month != null) {
                room.month = Month.of(timeToBook.month)
            } else if (month != "null") {
                room.month = Month.valueOf(month)
            }
            if (timeToBook.day != null) {
                if (timeToBook.dayIsRelative) {
                    room.day = LocalDate.now().plusDays(timeToBook.day).dayOfMonth
                    room.month = LocalDate.now().plusDays(timeToBook.day).month
                } else {
                    room.day = timeToBook.day.toInt()
                }
            } else if (day != "null") {
                room.day = day.toInt()
            }
        }
        if (room.minute == null || room.hour == null) {
            error = ErrorTypeResponse.NO_TIME
        }
        if (room.day == null || room.month == null) {
            error = ErrorTypeResponse.NO_DATE
        }
        if ((room.minute == null || room.hour == null) && (room.day == null || room.month == null)) {
            error = ErrorTypeResponse.EMPTY
        }
        return RoomResponce(listOf(room), error)
    }

    fun getPlaceFromRequest(place: String): RoomResponce {
        val placeToBook: Place = if (place.isEmpty()) Place.NONE else Place.parseVal(place)
        if (placeToBook != Place.NONE) {
            return RoomResponce(listOf(Room(placeToBook, null, null)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_PLACE)
    }

    fun getTimeFromRequest(time: String): RoomResponce {
        val timeToBook: Pair<Int, Int>? = parseTime(time)
        if (timeToBook != null && isRightTimeToBook(timeToBook)) {
            return RoomResponce(listOf(Room(null, timeToBook, null)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_TIME)
    }

    private fun isRightTimeToBook(time: Pair<Int, Int>): Boolean {
        return time.first in 8..21 && time.second in 0..59
    }
//FIXME: check
    private fun isRightDateToBook(month: Month, day: Int): Boolean {
        val dateToBook = LocalDate.of(2023, month, day)
        return dateToBook.isAfter(LocalDate.now().minusDays(1)) && dateToBook.isBefore(LocalDate.now().plusDays(8))
    }

    fun getMonthAndDayFromRequest(month: String, day: String): RoomResponce {
        val monthToBook: Month? = if (month.isNotEmpty()) Month.valueOf(month.toUpperCase()) else null
        val dayToBook: Int? = if (day.toIntOrNull() != null) day.toInt() else null
        if (monthToBook != null
            && dayToBook != null
            && monthToBook.checkDay(dayToBook)
            && isRightDateToBook(monthToBook, dayToBook)) {
            return RoomResponce(listOf(Room(day = dayToBook, month = monthToBook)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_TIME)
    }

    fun getTypeFromRequest(type: String): RoomResponce {
        val typeToBook: RoomType = RoomType.parseVal(type)
        if (typeToBook != RoomType.NONE) {
            return RoomResponce(listOf(Room(type = typeToBook)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_TYPE)
    }

    fun getTodayFromRequest(today: String): RoomResponce {
        val todayToBook: DayOfWeek = DayOfWeek.parseVal(today)
        if (todayToBook != DayOfWeek.NONE) {
            return RoomResponce(listOf(Room(today = todayToBook)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.EMPTY)
    }

    fun getNumberFromRequest(number: String): RoomResponce {
        val numberToBook: Int? = number.toIntOrNull()
        if (numberToBook != null) {
            return RoomResponce(listOf(Room(numberMem = numberToBook)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_NUMBER)
    }

//    fun handleRequestNumber(number: Int?): RoomResponce {
//        if (number != null) {
//            return RoomResponce(listOf(Room(roomId = number)), ErrorTypeResponse.SUCCESS)
//        }
//        return RoomResponce(emptyList(), ErrorTypeResponse.NO_TYPE)
//    }
//
//    fun getMonth(month: Int): String? {
//        return DateFormatSymbols().months[month - 1]
//    }
}