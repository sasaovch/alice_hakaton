package com.example.alice_bot

import com.example.handler.RequestHandler
import com.example.models.ErrorTypeResponce
import com.example.models.RoomResponce
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
                    "Привет! Хотите забронировать аудиторию?",
                    "Я могу подать заявку на бронь аудитории, также рассказать о доступных вариантах для бронирования.",
                    "Я могу помочь вам в бронировании аудитории, и сказать список доступных на определенный момент времени",
                    "Я могу помочь вам с подбором аудитории"
                )
            }
        }

        state("main_book") {
            activators {
                intent("book")
            }
            action {
                println("main_book пошел")
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
                    println("ask_place")
                    val place = activator.alice?.slots?.get("place")?.value

                    val response = requestHandler.handleRequestPlace(place.toString().substring(1, place.toString().length - 1))
                    when (response.error) {
                        ErrorTypeResponce.NO_PLACE -> {
                            reactions.say("Не верный адрес. Доступно только Ломоносова 9 и Кронва 49")
                            reactions.go("/say_place")
                        }
                        ErrorTypeResponce.SUCCESS -> {
                            saveToSession("place", response.roomList[0].place.toString(), reactions, request)
                        }
                    }
                    println("возвращение в /main_book после ask_place")
                    reactions.go("/main_book")
                }
            }

            state("ask_date") {
                activators {
                    intent("ask_date")
                }

                action {
                    println("ask_date")
                    val month = activator.alice?.slots?.get("month")?.value
                    val day = activator.alice?.slots?.get("day")?.value

                    val response = requestHandler.handleRequestDate(month.toString().substring(1, month.toString().length - 1), day.toString())
                    when (response.error) {
                        ErrorTypeResponce.NO_DATE -> reactions.say("Неправильная дата")
                        ErrorTypeResponce.SUCCESS -> {
                            saveToSession(
                                mapOf("month" to response.roomList[0].month.toString(), "day" to response.roomList[0].day.toString()),
                                reactions,
                                request
                            )
                        }
                    }
                    println("back to main_book")
                    reactions.go("/main_book")
                }
            }

            state("ask_time") {
                activators {
                    intent("ask_time")
                }

                action {
                    println("ask_time")
                    val time = activator.alice?.slots?.get("time")?.value.toString()

                    val response = requestHandler.handleRequestTime(time.toString().substring(1, time.toString().length - 1))
                    when (response.error) {
                        ErrorTypeResponce.NO_TIME -> reactions.say("Неверное время")
                        ErrorTypeResponce.SUCCESS -> {
                            saveToSession("time", response.roomList.get(0).time!!.first.toString() + " " + response.roomList.get(0).time!!.second.toString(), reactions, request)
                        }
                    }
                    println("go to mai_book")
                    reactions.go("/main_book")
                }

            }

            state("ask_type") {
                activators {
                    intent("ask_type")
                }

                action {
                    println("ask_type")
                    val type = activator.alice?.slots?.get("room")?.value
                    println(type.toString() +" "+"sadsadsadasd")
                    val response = requestHandler.handleRequestType(type.toString().substring(1, type.toString().length - 1))
                    when (response.error) {
                        ErrorTypeResponce.NO_TYPE -> reactions.say("Неверный тип: аудитория или переговорка")
                        ErrorTypeResponce.SUCCESS -> {
                            saveToSession("type", response.roomList.get(0).type.toString(), reactions, request)
                        }
                    }
                    println("back to main_book")
                    reactions.go("/main_book")
                }
            }

            state("say_place") {
                action {
                    reactions.sayRandom("Пожалуйста, уточните корпус",
                        "Окей, уточните тогда корпус, который вас интересует",
                        "Хорошо, какой корпус хотите рассмотреть?"
                    )
                    reactions.changeState("/main_book/ask_place")
                }
            }
            state("say_date") {
                action {
                    reactions.sayRandom("Пожалуйста, уточните дату",
                        "Хорошо, уточните тогда дату, которая вас интересует и время.",
                        "Да, конечно, подскажите пожалуйста интересуемую вас дату.",
                        "Хорошо, какая дата вас интересует?",
                        "Окей, какую дату хотите рассмотреть?",
                    )
                    reactions.changeState("/main_book/ask_date")
                }
            }
            state("say_time") {
                action {
                    reactions.sayRandom("Пожалуйста, уточните время",
                        "Да, конечно, подскажите пожалуйста, на какое время в указанную ранее вами дату вам было бы удобно?",
                        "Хорошо, уточните тогда интересуемое время для вас на указанную ранее дату.",
                        "Хорошо, какое время вас интересует в указанную вами ранее дату?",
                        "Окей, какое время хотите рассмотреть на указанную ранее дату?",
                    )
                    reactions.changeState("/main_book/ask_time")
                }
            }
            state("say_type") {
                action {
                    reactions.sayRandom("Пожалуйста, уточните тип",
                        "Да, конечно, подскажите пожалуйста, на какое время в указанную ранее вами дату вам было бы удобно?",
                        "Хорошо, уточните тогда интересуемое время для вас на указанную ранее дату.",
                        "Хорошо, какое время вас интересует в указанную вами ранее дату?",
                        "Окей, какое время хотите рассмотреть на указанную ранее дату?",
                    )
                    reactions.changeState("/main_book/ask_type")
                }
            }

        }
        fallback {
            reactions.say("Не распарсилось")
        }
    }

    private fun switcher(activator: ActivatorContext, reactions: Reactions, request: BotRequest) {
            val slots = activator.alice?.slots
            val state = request.alice?.state?.session

            val mapToSaveSession = mutableMapOf<String, Any>()
            val place = slots?.get("place")?.value?: state?.get("place")
            val month = slots?.get("month")?.value?: state?.get("month")
            val day = slots?.get("day")?.value?: state?.get("day")
            val time = slots?.get("time")?.value?: state?.get("time")
            val type = slots?.get("room")?.value?: state?.get("room")

        println("Switcher")
        println(place)
        println(month)
        println(day)
        println(time)
        println(type)

            val placeReq = requestHandler.handleRequestPlace(place?.toString()?.substring(1, place.toString().length - 1))
            if (placeReq.error != ErrorTypeResponce.NO_PLACE) {
                mapToSaveSession["place"] = placeReq.roomList[0].place.toString()
            }

            var dateReq: RoomResponce? = null
            if (month != null && day != null) {
                dateReq = requestHandler.handleRequestDate(month.toString().substring(1, month.toString().length - 1), day.toString().replace("\"", ""))
                if (dateReq.error != ErrorTypeResponce.NO_TIME) {
                    mapToSaveSession["day"] = dateReq.roomList[0].day.toString()
                    mapToSaveSession["month"] = dateReq.roomList[0].month.toString()
                }
            }

            val timeReq = requestHandler.handleRequestTime(time?.toString()?.substring(1, time.toString().length - 1)?: "")
            if (timeReq.error != ErrorTypeResponce.NO_TIME) {
                mapToSaveSession["time"] = timeReq.roomList[0].time!!.first.toString() + " " + timeReq.roomList[0].time!!.second.toString()
            }

            val typeReq = requestHandler.handleRequestType(type?.toString()?.substring(1, type.toString().length - 1) ?: "")
            if (typeReq.error != ErrorTypeResponce.NO_TYPE) {
                mapToSaveSession["room"] = typeReq.roomList[0].type.toString()
            }

            saveToSession(mapToSaveSession, reactions, request)

            if (placeReq.error == ErrorTypeResponce.NO_PLACE) {
                println("корпуса нет")
                reactions.say("Не уточнен корпус")
                reactions.go("say_place")
            } else if (month == null || day == null || dateReq!!.error == ErrorTypeResponce.NO_DATE) {
                println("даты нет")
                reactions.say("Не уточнена дата")
                reactions.go("say_date")
            } else if (timeReq.error == ErrorTypeResponce.NO_TIME) {
                println("времени нет")
                reactions.say("Не уточнено время")
                reactions.go("say_time")
            } else if (typeReq.error == ErrorTypeResponce.NO_TYPE) {
                println("типа нет")
                reactions.say("Не уточнен тип")
                reactions.go("say_type")
            } else {
                println("все ок")
                val (s, s1) = reactions.say("Окей, бронирую")
                val listOfRoom =
                    requestHandler.handleRequest(
                        placeReq.roomList[0].place!!,
                        dateReq.roomList[0].month!!,
                        dateReq.roomList[0].day!!,
                        timeReq.roomList[0].time!!,
                        typeReq.roomList[0].type,
                    )
                if (listOfRoom.isEmpty()) {
                    reactions.say(
                        "Не удалось забронировать аудиторию, так как нет свободной"
                    )
                    reactions.alice?.endSession()
                } else {
                    reactions.say(
                        "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} " +
                                "во время ${time?.toString() ?: "null"} " +
                                "вот это: ${type?.toString() ?: "null"}"
                    )
                    reactions.alice?.endSession()
                }
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
            mapJson[key] = objectJ[key]!!
        }
        val json = JsonObject(mapJson)
        reactions.alice?.sessionState(json)
    }

    private fun saveToSession(parametr: String, value: String, reactions: Reactions, request: BotRequest) {
        val mapJson = HashMap<String, JsonElement>()
        val par = JsonPrimitive(value)
        mapJson[parametr] = par
        val objectJ = request.alice?.state?.session
        if (!objectJ.isNullOrEmpty()) for (key in objectJ.keys) {
            mapJson[key] = objectJ[key]!!
        }
        val json = JsonObject(mapJson)
        reactions.alice?.sessionState(json)
    }

    private fun saveToApplication(parametr: String, value: String, reactions: Reactions) {
        val mapJson = HashMap<String, JsonElement>()
        val par = JsonPrimitive(value)
        mapJson[parametr] = par
        reactions.alice?.updateUserState(parametr, par)
    }
}
