package com.example.alice_bot

import com.example.handler.RequestHandler
import com.example.models.*
import com.example.util.convertTimeToRussion
import com.example.util.convertTimeToString
import com.example.util.removeQuotations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.justai.jaicf.api.BotRequest
import com.justai.jaicf.channel.yandexalice.activator.alice
import com.justai.jaicf.channel.yandexalice.alice
import com.justai.jaicf.channel.yandexalice.api.alice
import com.justai.jaicf.channel.yandexalice.model.AliceEvent
import com.justai.jaicf.context.ActivatorContext
import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.reactions.Reactions
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.content
import java.time.LocalDate
import java.time.Month

class MainScenario(
    private val requestHandler: RequestHandler
) : Scenario() {
    init {
        state("start") {
            activators {
                event(AliceEvent.START)
                regex("/start")
            }

            action {
                reactions.sayRandom(
                    "Привет! Мы поможем подать заявку на бронь помещения в корпусах университета ИТМО. Для этого вы можете просто сказать слово забронировать",
                    "Привет! Мы поможем вам в бронировании помещения в корпусах университета ИТМО. Вы можете сказать слово забронировать для начала или использовать запрос с параметрами. Например: забронируй аудитории на Ломоносова на дату во время"
                )
                reactions.buttons("Помощь")
                reactions.buttons("Что ты умеешь")
            }
        }

        state("help") {
            activators {
                regex("/help")
                intent("YANDEX.HELP")
            }

            action {
                reactions.sayRandom(
                    "Вы можете сказать: Забронируй аудиторию на Ломоносова на 8 марта на 15 00, или Забронируй переговорку на Кронверском проспекте на 10 февраля на 20 00, или просто слово забронируй и мы спросим вас о всех параметрах. Если вы указали неправильный параметр, можете просто повторить его или сказать слово назад"
                )
                reactions.buttons("Помощь")
                reactions.buttons("Что ты умеешь")
            }
        }

        state("what") {
            activators {
                regex("/what")
                intent("YANDEX.WHAT_CAN_YOU_DO")
            }

            action {
                reactions.sayRandom(
                    "Это навык для бронирования помещений в корпусах университета ИМТО. На данный момент мы можем забронировать аудитории и переговорки на Ломоносова и Кронверкском проспекте. Чтобы начать скажите забронировать. Если вы укажите неправильный параметр, можете просто повторить его или сказать слово назад"
                )
                reactions.buttons("Помощь")
                reactions.buttons("Что ты умеешь")
            }
        }

        state("main_book") {
            activators {
                intent("book")
            }
            action {
                try {
                    switcher(activator, reactions, request)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            state("ask_place") {
                activators {
                    intent("ask_place")
                }
                action {
                    val place = activator.alice?.slots?.get("place")?.value
                    val response = requestHandler.getPlaceFromRequest(removeQuotations(place!!))
                    saveToSession("place", response.roomList[0].place.toString(), reactions, request)
                    reactions.go("/main_book")
                }
            }

            state("ask_date") {
                activators {
                    intent("ask_date")
                }

                action {
                    val month = activator.alice?.slots?.get("month")?.value
                    val day = activator.alice?.slots?.get("day")?.value
                    val today = activator.alice?.slots?.get("today")?.value
                    val responseForToday = requestHandler.getTodayFromRequest(removeQuotations(today?: ""))
                    if (responseForToday.error == ErrorTypeResponse.SUCCESS) {
                        val month = getMonth(responseForToday.roomList[0].today)
                        val day = getDate(responseForToday.roomList[0].today)
                        saveToSession(
                            mapOf(
                                "month" to month.toString(),
                                "day" to day.toString(),
                                "today" to responseForToday.roomList[0].today.toString()
                            ),
                            reactions,
                            request
                        )
                    } else {
                        val response = requestHandler.getMonthAndDayFromRequest(removeQuotations(month!!), day.toString())
                        when (response.error) {
                            ErrorTypeResponse.SUCCESS -> {
                                saveToSession(
                                    mapOf(
                                        "month" to response.roomList[0].month.toString(),
                                        "day" to response.roomList[0].day.toString()
                                    ),
                                    reactions,
                                    request
                                )
                            }
                            else -> {
                                reactions.say("Неправильная дата. Мы можем забронировать только на будущую дату не дальше семи дней. Повторите, пожалуйста, на какую дату забронировать")
                                saveToSession("state", "say_date", reactions, request)
                            }
                        }
                    }
                    reactions.go("/main_book")
                }
            }

            state("ask_time") {
                activators {
                    intent("ask_time")
                }

                action {
                    val time = activator.alice?.slots?.get("time")?.value.toString()

                    val response = requestHandler.getTimeFromRequest(removeQuotations(time))
                    when (response.error) {
                        ErrorTypeResponse.SUCCESS -> saveToSession("time", convertTimeToRussion(response.roomList[0].time!!), reactions, request)
                        else -> {
                            reactions.say("Вы выбрали не правильное время. Можно бронировать с 8 утра до 22 вечера")
                            saveToSession("state", "say_time", reactions, request)
                        }
                    }
                    reactions.go("/main_book")
                }

            }

            state("ask_duration") {
                activators {
                    intent("ask_duration")
                }

                action {
                    val time = activator.alice?.slots?.get("duration")?.value.toString()

                    val response = requestHandler.getTimeFromRequest(removeQuotations(time))
                    when (response.error) {
                        ErrorTypeResponse.SUCCESS -> saveToSession("time", convertTimeToRussion(response.roomList[0].time!!), reactions, request)
                        else -> {
                            reactions.say("Вы выбрали не правильное время. Можно бронировать с 8 утра до 22 вечера")
                            saveToSession("state", "say_time", reactions, request)
                        }
                    }
                    reactions.go("/main_book")
                }

            }

            state("ask_type") {
                activators {
                    intent("ask_type")
                }

                action {
                    val type = activator.alice?.slots?.get("room")?.value
                    val response = requestHandler.getTypeFromRequest(removeQuotations(type!!))
                    when (response.error) {
                        ErrorTypeResponse.SUCCESS -> saveToSession("type", response.roomList[0].type.toString(), reactions, request)
                        else -> {
                            reactions.say("Извините, на данный момент мы можем забронировать аудиторию или переговорку")
                            saveToSession("state", "say_type", reactions, request)
                        }
                    }
                    reactions.go("/main_book")
                }
            }

            state("ask_phone") {
                activators {
                    intent("ask_phone")
                }

                action {
                    val phone = activator.alice?.slots?.get("phone")?.value
                    println("Phone ${phone}")
                    saveToApplication("phone", removeQuotations(phone.toString()), reactions, request)
                    reactions.go("/main_book")
                }
            }

            state("ask_password") {
                activators {
                    intent("ask_password")
                }

                action {
                    val password = activator.alice?.slots?.get("password")?.value
                    println("Paswwor ${password}")
                    saveToApplication("password", removeQuotations(password.toString()), reactions, request)
                    saveToSession(emptyMap(), reactions, request)
                    reactions.go("/main_book/say_phone")
                }
            }

            state("ask_login") {
                activators {
                    intent("ask_login")
                    regex("[0-9]{6}")
                }

                action {
                    val login = activator.alice?.slots?.get("login")?.value
                    println("Login ${login}")
                    saveToApplication("login", login.toString(), reactions, request)
                    saveToSession(emptyMap(), reactions, request)
                    reactions.go("/main_book/say_password")
                }
            }

            state("ask_id") {
                activators {
                    intent("ask_id")
                }

                action {
                    val roomId = activator.alice?.slots?.get("id")?.value.toString().toInt()
                    println("$roomId this")
                    val list = getListRoom(reactions, request)
//                    saveToSession("state", "say", reactions, request)

                    if (list.filter { it.roomId!!.equals(roomId) }.isNotEmpty()) {
                        println("Success")
                        saveToSession(emptyMap(), reactions, request)
                        if (bookRoom(list.first { it.roomId == roomId }, reactions, request)) {
                            saveToApplication(list.first { it.roomId == roomId }, reactions, request)
                            reactions.go("/main_book/congratulations")
                        }
                    } else {
                        reactions.go("/main_book/problem_id")
                    }
                }
            }

            state("congratulations") {
                action {
                    reactions.say("Аудиторию забронирована")
                }
            }

            state("problem_id") {
                action {
                    val list = getListRoom(reactions, request)
                    reactions.sayRandom("Неправильный номер помещения\n")
                    sayRooms(list, reactions, request)
                }
            }

            state("back") {
                activators {
                    intent("back")
                    intent("YANDEX.BOOK.NAVIGATION.PREVIOUS")
                }
                action {
                    val state: String = removeQuotations(request.alice?.state?.session?.get("state")?: "")
                    if (state != "") {
                        when (state) {
                            "say_place" -> {
                                saveToSession("state", "help", reactions, request)
                                reactions.go("/help")
                            }
                            "say_date" -> {
                                saveToSession("state", "say_place", reactions, request)
                                reactions.go("/main_book/say_place")
                            }
                            "say_login" -> {
                                saveToSession("state", "say_time", reactions, request)
                                reactions.go("/main_book/say_time")
                            }
                            "say_password" -> {
                                saveToSession("state", "say_login", reactions, request)
                                reactions.go("/main_book/say_login")
                            }
                            "say_number" -> {
                                saveToSession("state", "help", reactions, request)
                                reactions.go("/main_book/say_number")
                            }
                            "say_time" -> {
                                saveToSession("state", "say_type", reactions, request)
                                reactions.go("/main_book/say_type")
                            }
                            "say_type" -> {
                                saveToSession("state", "say_date", reactions, request)
                                reactions.go("/main_book/say_date")
                            }
                            "say_time_for_room" -> {
                                saveToSession("state", "say_type", reactions, request)
                                reactions.go("/main_book/say_type")
                            }
                            else -> reactions.say("Вы еще не начали бронировать помещение")
                        }
                    }
                }
            }

            state("say_login") {
                action {
                    reactions.sayRandom(
                        "Пожалуйста, уточните свой логин от ису. Это нужно для системы регистрации"
                    )
                    reactions.changeState("/main_book/ask_login")
                }
            }

            state("say_password") {
                action {
                    reactions.sayRandom(
                        "Пожалуйста, уточните свой пароль от ису. Это нужно для системы регистрации"
                    )
                    reactions.changeState("/main_book/ask_password")
                }
            }

            state("say_phone") {
                action {
                    reactions.sayRandom(
                        "Пожалуйста, уточните свой номер телефона от ису. Это нужно для системы регистрации"
                    )
                    reactions.changeState("/main_book/ask_phone")
                }
            }



            state("say_place") {
                action {
                    reactions.sayRandom("Пожалуйста, уточните корпус",
                        "Хорошо, уточните корпус, который вас интересует",
                        "Хорошо, какой корпус вы хотите рассмотреть?"
                    )
                    reactions.buttons("Ломоносова")
                    reactions.buttons("Кронверкский")
                    reactions.buttons("Назад")
                    reactions.changeState("/main_book/ask_place")
                }
            }
            state("say_date") {
                action {
                    reactions.sayRandom("Пожалуйста, уточните дату",
                        "Хорошо, уточните дату, которая вас интересует",
                        "Подскажите, пожалуйста, дату брони",
                        "Хорошо, какая дата вам нужна?",
                    )
                    reactions.buttons("Назад")
                    reactions.changeState("/main_book/ask_date")
                }
            }
            state("say_time") {
                action {
                    reactions.sayRandom(
                        "Пожалуйста, уточните время",
                        "Подскажите, пожалуйста, на какое время вам было бы удобно?",
                        "Хорошо, уточните интересуемое время",
                        "Хорошо, какое время вам нужно?",
                        "На какое время вы хотите забронировать?",
                    )
                    reactions.buttons("Назад")
                    reactions.changeState("/main_book/ask_time")
                }
            }
            state("say_time_for_room") {
                action {

                    val type = activator.alice?.slots?.get("room")?.value?: request.alice?.state?.session?.get("room")
                    val response = requestHandler.getTypeFromRequest(removeQuotations(type!!))
                    when (response.error) {
                        ErrorTypeResponse.SUCCESS -> {
//                            saveToSession("type", response.roomList[0].type.toString(), reactions, request)
                            if (response.roomList[0].type == RoomType.MEETINGROOM) {
                                reactions.say("Пожалуйста, уточните время. Учтите, что переговорку можно забронировать на время кратное 30 минутам")
                            } else {
                                reactions.say("Выберите удобное время для бронирования аудитории")
                                reactions.buttons("8 20")
                                reactions.buttons("10 00")
                                reactions.buttons("11 40")
                                reactions.buttons("13 30")
                                reactions.buttons("15 20")
                                reactions.buttons("17 00")
                                reactions.buttons("18 40")
                                reactions.buttons("20 20")
                            }
                        }
                        else -> {
                            reactions.sayRandom("Пожалуйста, уточните время",
                                "Подскажите, пожалуйста, на какое время вам было бы удобно?",
                                "Хорошо, уточните интересуемое время",
                                "Хорошо, какое время вам нужно?",
                                "На какое время вы хотите забронировать?",
                            )
                        }
                    }
                    reactions.buttons("Назад")
                    reactions.changeState("/main_book")
                }
            }
            state("say_type") {
                action {
                    reactions.sayRandom("Пожалуйста, уточните вам нужна аудитория или переговорка",
                        "Подскажите, пожалуйста, вам нужна аудитория или переговорка"
                    )
                    reactions.buttons("Аудитория")
                    reactions.buttons("Переговорка")
                    reactions.buttons("Назад")
                    reactions.changeState("/main_book/ask_type")
                }
            }
            state("problem_place") {
                action {
                    saveToSession(emptyMap(), reactions, request)
                    reactions.sayRandom("Неправильный адрес. Можно выбрать Ломоносова или Кронверкский"
                    )
                    reactions.buttons("Ломоносова")
                    reactions.buttons("Кронверкский")
                    reactions.buttons("Назад")
                    reactions.changeState("/main_book")
                }
            }
            state("problem_time") {
                action {
                    saveToSession(emptyMap(), reactions, request)
                    reactions.sayRandom("Неверное время. Пожалуйста, скажите числом час и минуты")
                    reactions.changeState("/main_book")
                }
            }
            state("problem_date") {
                action {
                    saveToSession(emptyMap(), reactions, request)
                    reactions.sayRandom("Вы указали неправильную дату. Пожалуйста, скажите месяц и число")
                    reactions.changeState("/main_book")
                }
            }
            state("problem_type") {
                action {
                    saveToSession(emptyMap(), reactions, request)
                    reactions.sayRandom("Неверный тип помещения. Можно забронировать аудиторию или переговорку")
                    reactions.buttons("Аудитория")
                    reactions.buttons("Переговорка")
                    reactions.buttons("Назад")
                    reactions.changeState("/main_book")
                }
            }
        }

        fallback {
            val state: String = removeQuotations(request.alice?.state?.session?.get("state")?: "")
            if (state != "") {
                goToPreviousState(reactions, state)
            } else {
                reactions.say("К сожалению, мы вас не поняли. Для начала бронирования скажите забронировать")
            }
        }
    }

    private fun getListRoom(reactions: Reactions, request: BotRequest): List<Room> {
        val strList = request.alice?.state?.session?.get("rooms").toString()
        println(strList)
        println(strList.substring(1, strList.length - 1))
        println(strList.substring(1, strList.length - 1).replace("\\", ""))
        val listRooms = Gson().fromJson(strList.substring(1, strList.length - 1).replace("\\", ""), Array<Room>::class.java).asList()
        println("$listRooms that")
        return listRooms
    }

    private fun goToPreviousState(reactions: Reactions, state: String) {
        when (state) {
            "say_place" -> reactions.go("/main_book/problem_place")
            "say_time" -> reactions.go("/main_book/problem_time")
            "say_date" -> reactions.go("/main_book/problem_date")
            "say_type" -> reactions.go("/main_book/problem_type")
        }
    }
    private fun getRoomFromRequest(activator: ActivatorContext, request: BotRequest): RoomRequest {
        val slots = activator.alice?.slots
        val state = request.alice?.state?.session

        val place = slots?.get("place")?.value?: state?.get("place")
        val month = slots?.get("month")?.value?: state?.get("month")
        val day = slots?.get("day")?.value?: state?.get("day")
        val time = slots?.get("time")?.value?: state?.get("time")
        val type = slots?.get("room")?.value?: state?.get("room")
        val today = slots?.get("today")?.value?: state?.get("today")
        val numMembers = slots?.get("number")?.value?: state?.get("number")
        return RoomRequest(place, month, day, time, type, today, numMembers)
    }
    private fun handleRoomRequest(roomRequest: RoomRequest, mapToSaveSession: MutableMap<String, String>): RoomResponce {
        var error: ErrorTypeResponse = ErrorTypeResponse.SUCCESS
        val room = Room()

        val numberReq = requestHandler.getNumberFromRequest(roomRequest.numberMembers)
        if (numberReq.error != ErrorTypeResponse.NO_NUMBER) {
            mapToSaveSession["number"] = numberReq.roomList[0].numberMem.toString()
            room.numberMem = numberReq.roomList[0].numberMem
        } else {
            error = ErrorTypeResponse.NO_NUMBER
        }

        val timeReq = requestHandler.getTimeFromRequest(roomRequest.time)
        val typeReq = requestHandler.getTypeFromRequest(roomRequest.type)
        if (typeReq.error != ErrorTypeResponse.NO_TYPE && timeReq.error != ErrorTypeResponse.NO_TIME) {
            val timeType = checkTimeForType(typeReq.roomList[0].type, timeReq.roomList[0].time!!)
            if (timeType.error != ErrorTypeResponse.WRONG_TIME_FOR_TYPE) {
                mapToSaveSession["time"] = convertTimeToString(timeReq)
                mapToSaveSession["room"] = typeReq.roomList[0].type.toString()
                room.type = typeReq.roomList[0].type
                room.time = timeReq.roomList[0].time
            } else {
                mapToSaveSession["room"] = typeReq.roomList[0].type.toString()
                mapToSaveSession["time"] = ""
                room.type = typeReq.roomList[0].type
                error = ErrorTypeResponse.WRONG_TIME_FOR_TYPE
            }
        } else if (typeReq.error != ErrorTypeResponse.NO_TYPE) {
            mapToSaveSession["room"] = typeReq.roomList[0].type.toString()
            room.type = typeReq.roomList[0].type
            error = ErrorTypeResponse.WRONG_TIME_FOR_TYPE
        } else if (timeReq.error != ErrorTypeResponse.NO_TIME){
            mapToSaveSession["time"] = convertTimeToString(timeReq)
            room.time = timeReq.roomList[0].time
            error = ErrorTypeResponse.NO_TYPE
        } else {
            error = ErrorTypeResponse.NO_TYPE
        }

        val todayReq = requestHandler.getTodayFromRequest(roomRequest.today)
        if (todayReq.error != ErrorTypeResponse.EMPTY) {
            mapToSaveSession["today"] = todayReq.roomList[0].today.toString()
            room.month = getMonth(todayReq.roomList[0].today)
            room.day = getDate(todayReq.roomList[0].today)
            mapToSaveSession["day"] = room.day.toString()
            mapToSaveSession["month"] = room.month.toString()
        } else {
            if (roomRequest.dateIsCorrect()) {
                val dateReq = requestHandler.getMonthAndDayFromRequest(roomRequest.month, roomRequest.day)
                if (dateReq.error != ErrorTypeResponse.NO_DATE) {
                    mapToSaveSession["day"] = dateReq.roomList[0].day.toString()
                    mapToSaveSession["month"] = dateReq.roomList[0].month.toString()
                    room.day = dateReq.roomList[0].day
                    room.month = dateReq.roomList[0].month
                } else {
                    error = ErrorTypeResponse.NO_DATE
                }
            } else {
                error = ErrorTypeResponse.NO_DATE
            }
        }


        val placeReq = requestHandler.getPlaceFromRequest(roomRequest.place)
        if (placeReq.error != ErrorTypeResponse.NO_PLACE) {
            mapToSaveSession["place"] = placeReq.roomList[0].place.toString()
            room.place = placeReq.roomList[0].place
        } else {
            error = ErrorTypeResponse.NO_PLACE
        }
        return RoomResponce(listOf(room), error)
    }

    private fun checkTimeForType(type: RoomType, time: Pair<Int, Int>): RoomResponce {
        val setOfTime = setOf<Pair<Int, Int>>(
            Pair(8, 20),
            Pair(10, 0),
            Pair(11, 40),
            Pair(13, 30),
            Pair(15, 20),
            Pair(17, 0),
            Pair(18, 40),
            Pair(20, 20)
        )
        return if (type == RoomType.MEETINGROOM) {
            if (time.first in 8..21 && time.second % 30 == 0) {
                RoomResponce(listOf(Room(type = type, time = time)), ErrorTypeResponse.SUCCESS)
            } else {
                RoomResponce(emptyList(), ErrorTypeResponse.WRONG_TIME_FOR_TYPE)
            }
        } else {
            if (setOfTime.contains(time)) {
                RoomResponce(listOf(Room(type = type, time = time)), ErrorTypeResponse.SUCCESS)
            } else {
                RoomResponce(emptyList(), ErrorTypeResponse.WRONG_TIME_FOR_TYPE)
            }
        }
    }
    private fun getMonth(dayOfWeek: DayOfWeek): Month {
        return when (dayOfWeek) {
            DayOfWeek.TODAY -> LocalDate.now().month
            DayOfWeek.TOMORROW -> LocalDate.now().plusDays(1).month
            DayOfWeek.AFTER_TOMORROW -> LocalDate.now().plusDays(2).month
            else -> LocalDate.now().month
        }
    }

    private fun getDate(dayOfWeek: DayOfWeek): Int {
        return when (dayOfWeek) {
            DayOfWeek.TODAY -> LocalDate.now().dayOfMonth
            DayOfWeek.TOMORROW -> LocalDate.now().plusDays(1).dayOfMonth
            DayOfWeek.AFTER_TOMORROW -> LocalDate.now().plusDays(2).dayOfMonth
            else -> LocalDate.now().dayOfMonth
        }
    }
    private fun switcher(activator: ActivatorContext, reactions: Reactions, request: BotRequest) {
        val mapToSaveSession = mutableMapOf<String, String>()
        val roomFromRequest = getRoomFromRequest(activator, request)
        val handleRequest = handleRoomRequest(roomFromRequest, mapToSaveSession)

        when (handleRequest.error) {
            ErrorTypeResponse.NO_PLACE -> mapToSaveSession["state"] = "say_place"
            ErrorTypeResponse.NO_DATE -> mapToSaveSession["state"] = "say_date"
            ErrorTypeResponse.NO_TIME -> mapToSaveSession["state"] = "say_time"
            ErrorTypeResponse.NO_TYPE -> mapToSaveSession["state"] = "say_type"
            ErrorTypeResponse.NO_LOGIN -> mapToSaveSession["state"] = "say_login"
            ErrorTypeResponse.NO_PASSWORD -> mapToSaveSession["state"] = "say_password"
            ErrorTypeResponse.NO_PHONE ->  mapToSaveSession["state"] = "say_phone"

            ErrorTypeResponse.WRONG_TIME_FOR_TYPE -> mapToSaveSession["state"] = "say_time_for_room"
            else -> mapToSaveSession["state"] = ""
        }
        saveToSession(mapToSaveSession, reactions, request)
        if (mapToSaveSession["state"].isNullOrEmpty()) {
            val user = checkUserDateFromAppState(reactions, request)
            if (user.login != "pass") {
                createRequestToBookRoom(handleRequest.roomList[0], reactions, request)
            }
        } else {
            reactions.go(mapToSaveSession["state"]!!)
        }
    }

    private fun checkUserDateFromAppState(reactions: Reactions, request: BotRequest): User {
        val loginR = request.alice?.state?.user?.get("login")
        val passworR = request.alice?.state?.user?.get("password")
        val phone = request.alice?.state?.user?.get("phone")
        if ((loginR == null || loginR.content == "null")
            || passworR == null || passworR.content == "null"
            || phone == null || phone.content == "null") {
            reactions.go("say_login")
            return User("pass", "pass", "pass")
        } else {
            return User(loginR.content, passworR?.content?: "", phone?.content?: "")
        }
    }
    private fun bookRoom(roomToBook: Room, reactions: Reactions, request: BotRequest): Boolean {
//        requestHandler.bookRoom(roomToBook)
        return true
    }

    private fun createRequestToBookRoom(roomToBook: Room, reactions: Reactions, request: BotRequest) {
        val listOfRoom = requestHandler.getFreeRooms(roomToBook, user = User("", "", ""))
        if (listOfRoom.isEmpty()) {
            reactions.say(
                "Не удалось забронировать ${roomToBook.type.getParentCase()}, так как нет свободного помещения"
            )
            reactions.alice?.endSession()
        } else {
            sayRooms(listOfRoom, reactions, request)
//            reactions.say(
//                "Отправили запрос на бронирование ${roomToBook.type.getParentCase()} номер ${listOfRoom[0]}" +
//                        " на ${roomToBook.place!!.getRepositionalCase()} " +
//                    "во время ${convertTimeToRussion(roomToBook.time!!)} "
//            )
//            reactions.alice?.endSession()
        }
    }

    private fun sayRooms(listOfRoom: List<Room>, reactions: Reactions, request: BotRequest) {
        var strSay = ""
        for (i in 0..(listOfRoom.size - 2)) {
            strSay += "${listOfRoom[i].roomId}, "
        }
        strSay += "${listOfRoom[listOfRoom.size - 1].roomId}"
        reactions.say("Нашлись следующие аудитории: ${strSay}. Скажите номер аудитории")
        listOfRoom.forEach { reactions.buttons(it.roomId.toString()) }
        saveToSession(listOfRoom, reactions, request)
        reactions.changeState("/main_book/ask_id")
    }
    private fun saveToSession(map: Map<String, Any>, reactions: Reactions, request: BotRequest) {
        val mapJson = HashMap<String, JsonElement>()
        val objectJ = request.alice?.state?.session
        map.forEach{
            val par = JsonPrimitive(it.value.toString())
            mapJson[it.key] = par
        }
        if (!objectJ.isNullOrEmpty()) for (key in objectJ.keys) {
            if (!mapJson.containsKey(key)) {
                mapJson[key] = objectJ[key]!!
            }
        }
        val json = JsonObject(mapJson)
        reactions.alice?.sessionState(json)
    }

    private fun saveToSession(parameter: String, value: String, reactions: Reactions, request: BotRequest) {
        val mapJson = HashMap<String, JsonElement>()
        val par = JsonPrimitive(value)
        mapJson[parameter] = par
        val objectJ = request.alice?.state?.session
        if (!objectJ.isNullOrEmpty()) for (key in objectJ.keys) {
            mapJson[key] = objectJ[key]!!
        }
        val json = JsonObject(mapJson)
        reactions.alice?.sessionState(json)
    }

    private fun saveToSession(list: List<Room>, reactions: Reactions, request: BotRequest) {
        val mapJson = HashMap<String, JsonElement>()
        val objectJ = request.alice?.state?.session
        val strJson = Gson().toJson(list)
        mapJson["rooms"] = JsonPrimitive(strJson)
//        list.forEach{
//            val par = JsonObject(mapOf(
//                "place" to JsonPrimitive(it.place.toString()),
//                "day" to JsonPrimitive(it.day),
//                "month" to JsonPrimitive(it.month.toString()),
//                "time" to JsonPrimitive(it.time.toString()),
//                "type" to JsonPrimitive(it.type.toString()),
//                "numberMem" to JsonPrimitive(it.numberMem)))
//            mapJson[it.roomId.toString()] = par
//        }
        if (!objectJ.isNullOrEmpty()) for (key in objectJ.keys) {
            if (!mapJson.containsKey(key)) {
                mapJson[key] = objectJ[key]!!
            }
        }
        val json = JsonObject(mapJson)
        reactions.alice?.sessionState(json)
    }

    private fun saveToApplication(parameter: String, value: String, reactions: Reactions, request: BotRequest) {
        val par = JsonPrimitive(value)
        reactions.alice?.updateUserState(parameter, par)
    }

    private fun saveToApplication(room: Room, reactions: Reactions, request: BotRequest) {
        val strJson = Gson().toJson(room)
        reactions.alice?.updateUserState("room", JsonPrimitive(strJson))
    }
}
