package com.example.handler

import InfoHandler
import booking.BookingHandler
import com.example.api_service.AuthorizationHandler
import com.example.cookie.CookieHandler
import com.example.models.*
import com.example.util.TimeParser
import com.example.util.checkDay
import com.example.util.convertDateToDDMMYYYYFormat
import com.example.util.convertTimeToHHMMFormat
import java.time.LocalDate
import java.time.Month


class RequestHandler {
    val cookies = CookieHandler()
    lateinit var infoHandler: InfoHandler
    lateinit var phone: String

    private fun setPhone(phone : String) : Boolean {
        if(!"^[8].{10}\$".toRegex().matches(phone)) return false
        this.phone = "+7 ("
        this.phone += phone.substring(1, 4) + ") "
        this.phone += phone.substring(4)
        println(this.phone)
        return true
    }
    fun auth(login: String, password: String, phone: String): Boolean {
        if(!setPhone(phone)) return false
        val au = AuthorizationHandler(cookies)
        val IsuApCookie = au.loginAndGetApCookie(login, password)
        if (IsuApCookie == "") {
            return false
        }
        infoHandler = InfoHandler("ISU_AP_COOKIE=$IsuApCookie")
        infoHandler.checkInstance()

        return true
    }

    private fun addDurationToStart(room: Room): Pair<Int, Int> {
        if ((room.duration!! % 60) + room.time!!.second < 60) {
            return Pair((room.time!!.first + room.duration!! / 60), (room.time!!.second + room.duration!! % 60))
        } else {
            return Pair(
                (room.time!!.first + room.duration!! / 60 + 1),
                ((room.time!!.second + room.duration!! % 60) % 60)
            )

        }
    }

    fun getFreeRooms(room: Room): List<Room> {
        val begin = convertTimeToHHMMFormat(room.time!!)
        val endTime: Pair<Int, Int> = addDurationToStart(room)
        val end = convertTimeToHHMMFormat(endTime)
        println(begin)
        println(end)
        val dateList = TimeParser.parseTime(begin, end)
        val dateString = convertDateToDDMMYYYYFormat(room.day!!, room.month!!)
        println(dateString)
        if (room.numberMem == 0) {
            val listOfId = infoHandler.getFreeRooms(room.place!!, dateList, dateString, room.type)
            return listOfId.stream().map { it -> Room(room.place, room.time, room.day, room.month, it, room.type, 0) }
                .limit(3)
                .toList()
        } else {
            val listOfId =
                infoHandler.getFreeRoomsByPeopleNum(room.place!!, dateList, dateString, room.type, room.numberMem)
            return listOfId.stream()
                .map { it -> Room(room.place, room.time, room.day, room.month, it, room.type, room.duration) }
                .limit(3)
                .toList()
        }
    }

    private lateinit var bookingHandler: BookingHandler
    fun bookRoom(room: Room): Boolean {
        bookingHandler = BookingHandler(cookies, infoHandler.pInstance)
        val begin = convertTimeToHHMMFormat(room.time!!)
        val endTime: Pair<Int, Int> = addDurationToStart(room)
        val end = convertTimeToHHMMFormat(endTime)
        val dateString = convertDateToDDMMYYYYFormat(room.day!!, room.month!!)

        return bookingHandler.book(room.roomId!!, dateString, begin, end, room.type, room.place!!, phone)


    }

    private fun parseTime(strTime: String): Pair<Int, Int>? {
        val timeArray = strTime.split(" ")
        return if (timeArray[0].toIntOrNull() != null && timeArray[1].toIntOrNull() != null) {
            Pair(timeArray[0].toInt(), timeArray[1].toInt())
        } else {
            null
        }
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