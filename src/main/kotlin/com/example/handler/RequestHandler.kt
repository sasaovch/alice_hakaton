package com.example.handler

import InfoHandler
import booking.BookingHandler
import com.example.api_service.AuthorizationHandler
import com.example.constants.Constants.Companion.idToNum
import com.example.cookie.CookieHandler
import com.example.models.*
import com.example.util.TimeParser
import com.example.util.checkDay
import com.example.util.convertDateToDDMMYYYYFormat
import com.example.util.convertTimeToHHMMFormat
import com.google.gson.Gson
import java.text.DateFormatSymbols
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month


object RequestHandler {
    var UsernameToCookies = HashMap<String, CookieHandler>()
    var UserToPInstance = HashMap<String, String>()

    private fun setPhone(phone: String): String {
        if (!"^[8].{10}\$".toRegex().matches(phone)) return ""
        var newPhone = ""
        newPhone = "+7 ("
        newPhone += phone.substring(1, 4) + ") "
        newPhone += phone.substring(4)
        println(newPhone)
        return newPhone
    }

    fun auth(user: User): User {
//        return user
        var newPhone = setPhone(user.phone)
        if (newPhone == "") return User("", "", "")
        val cookies = CookieHandler()
        val au = AuthorizationHandler(cookies)
        val IsuApCookie = au.loginAndGetApCookie(user.login, user.password)
        if (IsuApCookie == "") {
            return User("", "", "")
        }
        UsernameToCookies[user.login] = cookies
        return User(user.login, user.password, newPhone)
    }

    private fun addDurationToStart(room: Room): Pair<Int, Int> {
        if ((room.duration!! % 60) + room.minute!! < 60) {
            return Pair((room.hour!! + room.duration!! / 60), (room.minute!! + room.duration!! % 60))
        } else {
            return Pair(
                (room.hour!! + room.duration!! / 60 + 1),
                ((room.minute!! + room.duration!! % 60) % 60)
            )
        }
    }

    fun getFreeRooms(room: Room, user: User): List<Room> {
        val cookies = UsernameToCookies[user.login]
        val infoHandler = InfoHandler(cookies!!)
        infoHandler.checkInstance()
        UserToPInstance[user.login] = infoHandler.pInstance
        val begin = convertTimeToHHMMFormat(room.hour!!, room.minute!!)
        val endTime: Pair<Int, Int> = addDurationToStart(room)
        val end = convertTimeToHHMMFormat(endTime)
        println(begin)
        println(end)
        val dateList = TimeParser.parseTime(begin, end)
        val dateString = convertDateToDDMMYYYYFormat(room.day!!, room.month!!)
        println(dateString)
        if (room.numberMem == 0) {
            val listOfId = infoHandler.getFreeRooms(room.place!!, dateList, dateString, room.type)
            return listOfId.stream().map { it -> Room(room.place, room.time, room.day, room.month, room.hour, room.minute, idToNum(it), room.type, 0) }
                .limit(3)
                .toList()
        } else {
            val listOfId =
                infoHandler.getFreeRoomsByPeopleNum(room.place!!, dateList, dateString, room.type, room.numberMem)
            return listOfId.stream()
                .map { it -> Room(room.place, room.time, room.day, room.month,room.hour, room.minute, idToNum(it), room.type, room.duration) }
                .limit(3)
                .toList()
        }
//        if (room.numberMem == 0) {
//            return listOf(
//                Room(room.place, room.time, room.day, room.month, room.hour, room.minute, 1221, room.type, 0),
//                Room(room.place, room.time, room.day, room.month, room.hour, room.minute, 1312, room.type, 0),
//                Room(room.place, room.time, room.day, room.month, room.hour, room.minute, 1432, room.type, 0)
//            )
//            val listOfId = infoHandler.getFreeRooms(room.place!!, dateList, dateString, room.type)
//            return listOfId.stream().map { it -> Room(room.place, room.time, room.day, room.month, room.hour, room.minute, it, room.type, 0) }
//                .limit(3)
//                .toList()
//        } else {
//            return listOf(
//                Room(room.place, room.time, room.day, room.month, room.hour, room.minute, 1212, room.type, 0, DayOfWeek.NONE, room.numberMem),
//                Room(room.place, room.time, room.day, room.month, room.hour, room.minute, 1225, room.type, 0, DayOfWeek.NONE, room.numberMem),
//                Room(room.place, room.time, room.day, room.month, room.hour, room.minute, 1554, room.type, 0, DayOfWeek.NONE, room.numberMem),
//            )
//            val listOfId =
//                infoHandler.getFreeRoomsByPeopleNum(room.place!!, dateList, dateString, room.type, room.numberMem)
//            return listOfId.stream()
//                .map { it -> Room(room.place, room.time, room.day, room.month, room.hour, room.minute, it, room.type, room.duration) }
//                .limit(3)
//                .toList()
        }

    fun bookRoom(room: Room, user: User): Boolean {

        val cookies = UsernameToCookies[user.login]
        val bookingHandler = BookingHandler(cookies!!, UserToPInstance[user.login]!!)
        val begin = convertTimeToHHMMFormat(room.time!!)
        val endTime: Pair<Int, Int> = addDurationToStart(room)
        val end = convertTimeToHHMMFormat(endTime)
        val dateString = convertDateToDDMMYYYYFormat(room.day!!, room.month!!)
        println(begin)
        println(end)
        return bookingHandler.book(room.roomId!!, dateString, begin, end, room.type, room.place!!, user.phone)


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
            if (hour != "null" && hour != "") {
                room.hour = hour.toInt()
            }
            if (minute != "null" && minute != "") {
                room.minute = minute.toInt()
            }
            if (month != "null" && month != "") {
                room.month = Month.valueOf(month)
            }
            if (day != "null" && day != "") {
                room.day = day.toInt()
            }
        } else {
            if (timeToBook.hour != null) {
                if (timeToBook.hour_is_relative) {
                    room.hour = LocalTime.now().plusHours(timeToBook.hour).hour
                } else {
                    room.hour = timeToBook.hour.toInt()
                }
                room.minute = 0
            } else if (hour != "null" && hour != "") {
                room.hour = hour.toInt()
            }
            if (timeToBook.minute != null) {
                if (timeToBook.minute_is_relative) {
                    room.minute = LocalTime.now().plusMinutes(timeToBook.minute).minute
                    room.hour = LocalTime.now().plusMinutes(timeToBook.minute).hour
                } else {
                    room.minute = timeToBook.minute.toInt()
                }
            } else if (minute != "null" && minute != "") {
                room.minute = minute.toInt()
            }
            if (timeToBook.month != null) {
                room.month = Month.of(timeToBook.month)
            } else if (month != "null" && month != "") {
                room.month = Month.valueOf(month)
            }
            if (timeToBook.day != null) {
                if (timeToBook.day_is_relative) {
                    room.day = LocalDate.now().plusDays(timeToBook.day).dayOfMonth
                    room.month = LocalDate.now().plusDays(timeToBook.day).month
                } else {
                    room.day = timeToBook.day.toInt()
                }
            } else if (day != "null" && day != "") {
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
    fun isRightDateToBook(month: Month, day: Int): Boolean {
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