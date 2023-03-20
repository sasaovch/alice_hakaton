package com.example.handler

import com.example.api_service.InfoHandler
import com.example.models.*
import com.example.util.checkDay
import com.example.util.convertDateToDDMMYYYYFormat
import com.example.util.convertTimeToHHMMSSFormat
import java.text.DateFormatSymbols
import java.time.Duration
import java.time.LocalDate
import java.time.Month
import java.util.Date



class RequestHandler {
    private val infoHandler : InfoHandler = InfoHandler;
    fun bookRoom(room: Room): List<Int> {
        val timeString = convertTimeToHHMMSSFormat(room.time!!)
        println(timeString)
        val dateString = convertDateToDDMMYYYYFormat(room.day!!, room.month!!)
        println(dateString)
        return infoHandler.getFreeRooms(room.place!!, timeString, dateString, room.type)
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