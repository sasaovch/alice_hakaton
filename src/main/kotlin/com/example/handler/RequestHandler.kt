package com.example.handler

import com.example.api_service.InfoHandler
import com.example.models.*
import java.sql.Date

class RequestHandler {
    val infoHandler : InfoHandler = InfoHandler;
    fun makeRequest() {
        infoHandler.checkInstance()
        val ans = infoHandler.getFreeRooms(Place.KRONVERSKY, "15:30-16:00", "25.03.2023", RoomType.MEETINGROOM)
        println(ans)
    }
    fun handleRequest(place: String?, time: String?, date: String?, type: String?): RoomResponce {
        val placeToBook: Place = if (place==null) Place.NONE else Place.parseVal(place)
        val timeToBook: Date = Date.valueOf(time)
        val dateToBook: Date = Date.valueOf(date)
        if (placeToBook != Place.NONE && timeToBook != null && dateToBook != null) {
//            val roomList = InfoHandler.getFreeRoomByDateAndTime(Room(placeToBook, timeToBook, dateToBook))
//            return RoomResponce(roomList, ErrorTypeResponse.SUCCESS)
        } else if (placeToBook == Place.NONE) {
            return RoomResponce(emptyList(), ErrorTypeResponse.NO_PLACE)
        } else if (timeToBook == null) {
            return RoomResponce(emptyList(), ErrorTypeResponse.NO_TIME)
        } else if (dateToBook == null) {
            return RoomResponce(emptyList(), ErrorTypeResponse.NO_DATE)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_PLACE);
    }

    fun handleRequestPlace(place: String?): RoomResponce {
        val placeToBook: Place = if (place==null) Place.NONE else Place.parseVal(place)
        if (placeToBook != Place.NONE) {
            return RoomResponce(listOf(Room(placeToBook, null, null)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_PLACE)
    }

    fun handleRequestTime(time: String?): RoomResponce {
        val timeToBook: Date = Date.valueOf(time)
        if (timeToBook != null) {
            return RoomResponce(listOf(Room(null, timeToBook, null)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_TIME)
    }

    fun handleRequestDate(date: String?): RoomResponce {
        val dateToBook: Date = Date.valueOf(date)
        if (dateToBook != null) {
            return RoomResponce(listOf(Room(null, null, dateToBook)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_TIME)
    }

    fun handleRequestType(type: String?): RoomResponce {
        val typeToBook: RoomType = RoomType.valueOf(type.let { "none" })
        if (typeToBook != null) {
            return RoomResponce(listOf(Room(null, null, null, null, typeToBook)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_TYPE)
    }

    fun handleRequestNumber(number: Int?): RoomResponce {
        if (number != null) {
            return RoomResponce(listOf(Room(null, null, null, number)), ErrorTypeResponse.SUCCESS)
        }
        return RoomResponce(emptyList(), ErrorTypeResponse.NO_TYPE)
    }
}