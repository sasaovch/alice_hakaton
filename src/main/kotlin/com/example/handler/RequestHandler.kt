package com.example.handler

import com.example.api_service.InfoHandler
import com.example.models.*
import java.sql.Date

class RequestHandler {
    fun handleRequest(place: String, time: String, date: String, type: String): RoomResponce {
        val placeToBook: Place = Place.parseVal(place)
        val dateToBook: Date? = parseDate(date)
        val timeToBook: Pair<Int, Int>? = parseTime(time)
        val typeToBook: RoomType = RoomType.parseVal(type)
        if (placeToBook != Place.NONE && timeToBook != null && dateToBook != null && typeToBook != RoomType.NONE) {
            val roomList = InfoHandler.getFreeRoomByDateAndTime(Room(placeToBook, timeToBook, dateToBook))
            return RoomResponce(roomList, ErrorTypeResponce.SUCCESS)
        } else if (placeToBook == Place.NONE) {
            return RoomResponce(emptyList(), ErrorTypeResponce.NO_PLACE)
        } else if (timeToBook == null) {
            return RoomResponce(emptyList(), ErrorTypeResponce.NO_TIME)
        } else if (dateToBook == null) {
            return RoomResponce(emptyList(), ErrorTypeResponce.NO_DATE)
        } else if (typeToBook == RoomType.NONE) {
            return RoomResponce(emptyList(), ErrorTypeResponce.NO_TYPE)
        }
        return RoomResponce(emptyList(), ErrorTypeResponce.NO_PLACE);
    }

    private fun parseTime(strTime: String): Pair<Int, Int>? {
        val timeArray = strTime.split(" ")
        return if (timeArray[0].toIntOrNull() != null && timeArray[1].toIntOrNull() != null) {
            Pair(timeArray[0].toInt(), timeArray[1].toInt())
        } else {
            null
        }
    }

    private fun parseDate(strDate: String): Date? {
        val dateArray = strDate.split(" ")
        return if (dateArray[0].toIntOrNull() != null) {
            val month = translateAndGetMonth(dateArray[1])
            val date = dateArray[0].toInt()
            Date(2023, month!!.value, date)
        } else if (dateArray[1].toIntOrNull() != null) {
            val month = translateAndGetMonth(dateArray[0])
            val date = dateArray[1].toInt()
            Date(2023, month!!.value, date)
        } else {
            return null
        }
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

    fun handleRequestDate(date: String?): RoomResponce {
        val dateToBook: Date? = if (date != null) parseDate(date) else null
        if (dateToBook != null) {
            return RoomResponce(listOf(Room(null, null, dateToBook)), ErrorTypeResponce.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponce.NO_TIME)
    }

    fun handleRequestType(type: String?): RoomResponce {
        val typeToBook: RoomType = RoomType.valueOf(type.let { "none" })
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