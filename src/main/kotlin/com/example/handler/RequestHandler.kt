package com.example.handler

import com.example.api_service.InfoHandler
import com.example.models.*
import java.time.Month
import java.util.*

class RequestHandler {
    val infoHandler : InfoHandler = InfoHandler;
    fun makeRequest(place: Place, time: String, date: String, type: RoomType) {
        infoHandler.checkInstance()
        val ans = infoHandler.getFreeRooms(place, time, date, type)
        println(ans)
        infoHandler.register()
    }
    fun handleRequest(place: Place, date: Date, time: Pair<Int, Int>, type: RoomType): RoomResponce {
//        val placeToBook: Place = Place.parseVal(place)
//        val dateToBook: Date? = parseDate(month, day)
//        val timeToBook: Pair<Int, Int>? = parseTime(time)
//        val typeToBook: RoomType = RoomType.parseVal(type)
//        if (placeToBook != Place.NONE && timeToBook != null && dateToBook != null && typeToBook != RoomType.NONE) {
////            val roomList = InfoHandler.getFreeRoomByDateAndTime(Room(placeToBook, timeToBook, dateToBook, typeToBook))
//
////            return RoomResponce(roomList, ErrorTypeResponce.SUCCESS)
//            return RoomResponce(emptyList(), ErrorTypeResponce.SUCCESS)
//        } else if (placeToBook == Place.NONE) {
//            return RoomResponce(emptyList(), ErrorTypeResponce.NO_PLACE)
//        } else if (timeToBook == null) {
//            return RoomResponce(emptyList(), ErrorTypeResponce.NO_TIME)
//        } else if (dateToBook == null) {
//            return RoomResponce(emptyList(), ErrorTypeResponce.NO_DATE)
//        } else if (typeToBook == RoomType.NONE) {
//            return RoomResponce(emptyList(), ErrorTypeResponce.NO_TYPE)
//        }
//        return RoomResponce(emptyList(), ErrorTypeResponce.NO_PLACE);
        val timeString = time.first.toString() + ":" + time.second.toString() + "0-" + time.first.toString() + ":" + (time.second + 30).toString()
        println(timeString)
        val dateString = date.date.toString() + ".0" + date.month.toString() + "." + "2023"
        println(dateString)
        makeRequest(place, timeString, dateString, type)
        return RoomResponce(emptyList(), ErrorTypeResponce.SUCCESS)
    }

    private fun parseTime(strTime: String): Pair<Int, Int>? {
        val timeArray = strTime.split(" ")
        return if (timeArray[0].toIntOrNull() != null && timeArray[1].toIntOrNull() != null) {
            Pair(timeArray[0].toInt(), timeArray[1].toInt())
        } else {
            null
        }
    }

    private fun parseDate(month: String, day: String): Date? {
        val monthToBook: Month? = if (month != null) Month.valueOf(month.toUpperCase()) else null
        val dayToBook: Int? = if (day != null && day.toIntOrNull() != null) day.toInt() else null
        if (monthToBook != null && dayToBook != null) {
            return Date(2023, monthToBook.value, dayToBook)
        }
        return null
    }

    fun handleRequestPlace(place: String?): RoomResponce {
        val placeToBook: Place = if (place==null) Place.NONE else Place.parseVal(place)
        if (placeToBook != Place.NONE) {
            return RoomResponce(listOf(Room(placeToBook, null, null)), ErrorTypeResponce.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponce.NO_PLACE)
    }

    fun handleRequestTime(time: String?): RoomResponce {
        val timeToBook: Pair<Int, Int>? = if (time != null) parseTime(time) else null
        if (timeToBook != null) {
            return RoomResponce(listOf(Room(null, timeToBook, null)), ErrorTypeResponce.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponce.NO_TIME)
    }

    fun handleRequestDate(month: String?, day: String?): RoomResponce {
        val monthToBook: Month? = if (month != null) Month.valueOf(month.toUpperCase()) else null
        val dayToBook: Int? = if (day != null && day.toIntOrNull() != null) day.toInt() else null

        if (monthToBook != null && dayToBook != null) {
            return RoomResponce(listOf(Room(null, null, Date(2023, monthToBook.value, dayToBook))), ErrorTypeResponce.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponce.NO_TIME)
    }

    fun handleRequestType(type: String?): RoomResponce {
        val typeToBook: RoomType = RoomType.parseVal(type?: "none")
        if (typeToBook != RoomType.NONE) {
            return RoomResponce(listOf(Room(null, null, null, null, typeToBook)), ErrorTypeResponce.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponce.NO_TYPE)
    }

    fun handleRequestNumber(number: Int?): RoomResponce {
        if (number != null) {
            return RoomResponce(listOf(Room(null, null, null, number)), ErrorTypeResponce.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponce.NO_TYPE)
    }
}