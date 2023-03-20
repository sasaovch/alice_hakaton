package com.example.alice_bot

import com.example.handler.RequestHandler
import com.example.models.*
import com.example.util.convertTimeToRussion
import com.example.util.convertTimeToString
import com.example.util.removeQuotations
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
import java.time.LocalDate

class MainScenario (
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
//                intent("help")
                intent("YANDEX.HELP")
            }

            action {
                reactions.sayRandom(
                    "Вы можете сказать: Забронируй аудиторию на Ломоносова на 8 марта на 15 00, или Забронируй переговорку на Кронверском проспекте на 10 февраля на 20 00, или просто слово забронируй и мы спросим вас о всех параметрах. Если вы указали неправильный параметр, можете просто повторить его или сказать слово назад"
                )
            }
        }

        state("what") {
            activators {
                regex("/what")
//                intent("what")
                intent("YANDEX.WHAT_CAN_YOU_DO")
            }

            action {
                reactions.sayRandom(
                    "Это навык для бронирования помещений в корпусах университета ИМТО. На данный момент мы можем забронировать аудитории и переговорки на Ломоносова и Кронверкском проспекте. Чтобы начать скажите забронировать. Если вы укажите неправильный параметр, можете просто повторить его или сказать слово назад"
                )
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
                //FIXME: не парситься если говорить отдельно

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
                    //TodayAndTomorrow
                    val responseForToday = requestHandler.getTodayFromRequest(removeQuotations(today?: ""))
                    if (responseForToday.error == ErrorTypeResponse.SUCCESS) {
                        val month = if (responseForToday.roomList[0].today == DayOfWeek.TODAY)  LocalDate.now().month else LocalDate.now().plusDays(1).month
                        val day = if (responseForToday.roomList[0].today == DayOfWeek.TODAY) LocalDate.now().dayOfMonth else LocalDate.now().plusDays(1).dayOfMonth
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

            state("back") {
                activators {
                    intent("back")
                    intent("YANDEX.BOOK.NAVIGATION.PREVIOUS")
                }
                action {
                    val state: String = removeQuotations(request.alice?.state?.session?.get("state")?: "")
                    if (state != "") {
                        when (state) {
                            "say_place" -> reactions.go("/help")
                            "say_date" -> reactions.go("/main_book/say_place")
                            "say_time" -> reactions.go("/main_book/say_date")
                            "say_type" -> reactions.go("/main_book/say_time")
                            else -> reactions.say("Вы еще не начали бронировать помещение")
                        }
                    }
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
                    reactions.sayRandom("Пожалуйста, уточните время",
                        "Подскажите, пожалуйста, на какое время вам было бы удобно?",
                        "Хорошо, уточните интересуемое время",
                        "Хорошо, какое время вам нужно?",
                        "На какое время вы хотите забронировать?",
                    )
                    reactions.buttons("Назад")
                    reactions.changeState("/main_book/ask_time")
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
                    reactions.changeState("/main_book/ask_place")
                }
            }
            state("problem_time") {
                action {
                    saveToSession(emptyMap(), reactions, request)
                    reactions.sayRandom("Неверное время. Пожалуйста, скажите числом час и минуты")
                    reactions.changeState("/main_book/ask_time")
                }
            }
            state("problem_date") {
                action {
                    saveToSession(emptyMap(), reactions, request)
                    reactions.sayRandom("Вы указали неправильную дату. Пожалуйста, скажите месяц и число")
                    reactions.changeState("/main_book/ask_date")
                }
            }
            state("problem_type") {
                action {
                    saveToSession(emptyMap(), reactions, request)
                    reactions.sayRandom("Неверный тип помещения. Можно забронировать аудиторию или переговорку")
                    reactions.changeState("/main_book/ask_type")
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
        return RoomRequest(place, month, day, time, type, today)
    }
    private fun handleRoomRequest(roomRequest: RoomRequest, mapToSaveSession: MutableMap<String, String>): RoomResponce {
        var error: ErrorTypeResponse = ErrorTypeResponse.SUCCESS
        val room = Room()

        val typeReq = requestHandler.getTypeFromRequest(roomRequest.type)
        if (typeReq.error != ErrorTypeResponse.NO_TYPE) {
            mapToSaveSession["room"] = typeReq.roomList[0].type.toString()
            room.type = typeReq.roomList[0].type
        } else {
            error = ErrorTypeResponse.NO_TYPE
        }

        val timeReq = requestHandler.getTimeFromRequest(roomRequest.time)
        if (timeReq.error != ErrorTypeResponse.NO_TIME) {
            mapToSaveSession["time"] = convertTimeToString(timeReq)
            room.time = timeReq.roomList[0].time
        } else {
            error = ErrorTypeResponse.NO_TIME
        }
        val todayReq = requestHandler.getTodayFromRequest(roomRequest.today)
        if (todayReq.error != ErrorTypeResponse.EMPTY) {
            mapToSaveSession["today"] = todayReq.roomList[0].today.toString()
            room.month = if (todayReq.roomList[0].today == DayOfWeek.TODAY)  LocalDate.now().month else LocalDate.now().plusDays(1).month
            room.day = if (todayReq.roomList[0].today == DayOfWeek.TODAY) LocalDate.now().dayOfMonth else LocalDate.now().plusDays(1).dayOfMonth
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
    private fun switcher(activator: ActivatorContext, reactions: Reactions, request: BotRequest) {
        val mapToSaveSession = mutableMapOf<String, String>()
        val roomFromRequest = getRoomFromRequest(activator, request)
        //Test is value save in map
        val handleRequest = handleRoomRequest(roomFromRequest, mapToSaveSession)

        when (handleRequest.error) {
            ErrorTypeResponse.NO_PLACE -> mapToSaveSession["state"] = "say_place"
            ErrorTypeResponse.NO_DATE -> mapToSaveSession["state"] = "say_date"
            ErrorTypeResponse.NO_TIME -> mapToSaveSession["state"] = "say_time"
            ErrorTypeResponse.NO_TYPE -> mapToSaveSession["state"] = "say_type"
            else -> mapToSaveSession["state"] = ""
        }
        saveToSession(mapToSaveSession, reactions, request)
        if (mapToSaveSession["state"].isNullOrEmpty()) {
            createRequestToBookRoom(handleRequest.roomList[0], reactions)
        } else {
            reactions.go(mapToSaveSession["state"]!!)
        }
    }
    private fun createRequestToBookRoom(roomToBook: Room, reactions: Reactions) {
        val listOfRoom = requestHandler.bookRoom(roomToBook)
        if (listOfRoom.isEmpty()) {
            reactions.say(
                "Не удалось забронировать ${roomToBook.type.getParentCase()}, так как нет свободного помещения"
            )
            reactions.alice?.endSession()
        } else {
            reactions.say(
                "Отправили запрос на бронирование ${roomToBook.type.getParentCase()} номер ${listOfRoom[0]}" +
                        " на ${roomToBook.place!!.getRepositionalCase()} " +
                    "во время ${convertTimeToRussion(roomToBook.time!!)} "
            )
            reactions.alice?.endSession()
        }
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

//    private fun saveToApplication(parameter: String, value: String, reactions: Reactions) {
//        val mapJson = HashMap<String, JsonElement>()
//        val par = JsonPrimitive(value)
//        mapJson[parameter] = par
//        reactions.alice?.updateUserState(parameter, par)
//    }
}
