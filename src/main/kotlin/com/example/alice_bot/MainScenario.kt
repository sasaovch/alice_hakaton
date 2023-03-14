package com.example.alice_bot

import com.example.api_service.InfoHandler
import com.example.handler.RequestHandler
import com.example.models.ErrorTypeResponce
import com.example.models.Place
import com.example.models.Room
import com.example.models.RoomResponce
import com.justai.jaicf.api.BotRequest
import com.justai.jaicf.channel.yandexalice.activator.alice
import com.justai.jaicf.channel.yandexalice.alice
import com.justai.jaicf.channel.yandexalice.api.alice
import com.justai.jaicf.channel.yandexalice.model.AliceEvent
import com.justai.jaicf.context.ActivatorContext
import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.reactions.Reactions
import io.ktor.http.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.json.JSONObject
import java.sql.Date

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
                reactions.sayRandom("Привет! Хотите забронировать аудиторию?",
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
                switcher(activator, reactions, request)
            }

            state("ask_place") {
                activators {
                    intent("ask_place")
                }

                action {
                    println("ask_place state")
                    val place = activator.alice?.slots?.get("place")?.value

                    println(place.toString().substring(1, place.toString().length - 1))

                    val response = requestHandler.handleRequestPlace(place.toString().substring(1, place.toString().length - 1))
                    when (response.error) {
                        ErrorTypeResponce.NO_PLACE -> reactions.say("Не верный адрес. Доступно только Ломоносова 9 и Кронва 49")
                        ErrorTypeResponce.SUCCESS -> {
                            saveToSession("place", response.roomList.get(0).place.toString(), reactions, request)
                            reactions.say(
                                "Хорошо, подскажите дату для бронирования ${place?.toString() ?: "null"} "
                            )
                        }
                    }
                }
            }

            state("ask_date") {
                activators {
                    intent("ask_date")
                }

                action {
                    val month = activator.alice?.slots?.get("month")?.value
                    val day = activator.alice?.slots?.get("day")?.value

                    val response = requestHandler.handleRequestDate(month.toString().substring(1, month.toString().length - 1), day.toString())
                    println(response)
                    when (response.error) {
                        ErrorTypeResponce.NO_DATE -> reactions.say("Неправильная дата")
                        ErrorTypeResponce.SUCCESS -> {
                            saveToSession("month", month.toString().substring(1, month.toString().length - 1), reactions, request)
                            saveToSession("day", day.toString(), reactions, request)
                            reactions.say(
                                "На какое время? ${month?.toString() ?: "null"}  ${day?.toString() ?: "null"}"
                            )
                        }
                    }
                }
            }

            state("ask_time") {
                activators {
                    intent("ask_time")
                }

                action {

                    val time = activator.alice?.slots?.get("time")?.value.toString()
                    println(time.toString().substring(1, time.toString().length - 1))

                    val response = requestHandler.handleRequestTime(time.toString().substring(1, time.toString().length - 1))
                    when (response.error) {
                        ErrorTypeResponce.NO_TIME -> reactions.say("Неверное время")
                        ErrorTypeResponce.SUCCESS -> {
                            saveToSession("time", time.toString(), reactions, request)
                            reactions.say(
                                "Вам нужна аудитория или переговорка ${time?.toString() ?: "null"} "
                            )
                        }
                    }
                }

            }

            state("ask_type") {
                activators {
                    intent("ask_type")
                }

                action {
                    val type = activator.alice?.slots?.get("type").toString()

                    val response = requestHandler.handleRequestType(type)
                    when (response.error) {
                        ErrorTypeResponce.NO_TYPE -> reactions.say("ask_place")
                        ErrorTypeResponce.SUCCESS -> {
                            saveToSession("type", type.toString(), reactions, request)
                            reactions.say(
                                "Поняла вас ${type?.toString() ?: "null"} "
                            )
                        }
                    }
                }
            }

            state("say_place") {
                action {
                    println("Say place")
                    reactions.sayRandom("Пожалуйста, уточните корпус",
                        "Окей, уточните тогда корпус, который вас интересует",
                        "Хорошо, какой корпус хотите рассмотреть?"
                    )
                    reactions.changeState("/main_book/ask_place")
                }
            }
            state("say_date") {
                action {
                    println("Say date")
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
                    println("Say time")
                    reactions.sayRandom("Пожалуйста, уточните время",
                        "Да, конечно, подскажите пожалуйста, на какое время в указанную ранее вами дату вам было бы удобно?",
                        "Хорошо, уточните тогда интересуемое время для вас на указанную ранее дату.",
                        "Хорошо, какое время вас интересует в указанную вами ранее дату?",
                        "Окей, какое время хотите рассмотреть на указанную ранее дату?",
                    )
                    reactions.changeState("/ask_time")
                }
            }
            state("say_type") {
                action {
                    println("Say type")
                    reactions.sayRandom("Пожалуйста, уточните время",
                        "Да, конечно, подскажите пожалуйста, на какое время в указанную ранее вами дату вам было бы удобно?",
                        "Хорошо, уточните тогда интересуемое время для вас на указанную ранее дату.",
                        "Хорошо, какое время вас интересует в указанную вами ранее дату?",
                        "Окей, какое время хотите рассмотреть на указанную ранее дату?",
                    )
                    reactions.changeState("/ask_type")
                }
            }

        }
        fallback {
            reactions.say("Не распарсилось")
        }
    }

    private fun switcher(activator: ActivatorContext, reactions: Reactions, request: BotRequest) {
        val place = request.alice?.state?.session?.get("place")?: activator.alice?.slots?.get("place")?.value
        val month = request.alice?.state?.session?.get("month")?: activator.alice?.slots?.get("month")?.value
        val day = request.alice?.state?.session?.get("day")?: activator.alice?.slots?.get("day")?.value
        val time = request.alice?.state?.session?.get("time")?: activator.alice?.slots?.get("time")?.value
        val type = request.alice?.state?.session?.get("type")?: activator.alice?.slots?.get("type")?.value
        println(time)
        val placeReq = requestHandler.handleRequestPlace(place?.toString()?.substring(1, place.toString().length - 1))
        if (placeReq.error != ErrorTypeResponce.NO_PLACE) {
            saveToSession("place", placeReq.roomList.get(0).place.toString(), reactions, request)
        }
        println(placeReq)
        var dateReq: RoomResponce? = null
        if (month != null && day != null) {
            dateReq = requestHandler.handleRequestDate(month.toString().substring(1, month.toString().length - 1), day.toString()?: "")
            if (dateReq.error != ErrorTypeResponce.NO_DATE) {
                saveToSession("day", dateReq.roomList.get(0).date!!.day.toString(), reactions, request)
                saveToSession("month", dateReq.roomList.get(0).date!!.month.toString(), reactions, request)
            }
        }
        println(dateReq)
        val timeReq = requestHandler.handleRequestTime(time?.toString()?.substring(1, time.toString().length - 1)?: "")
        if (timeReq.error != ErrorTypeResponce.NO_TIME) {
            saveToSession("time", timeReq.roomList.get(0).time!!.first.toString() + " " + timeReq.roomList.get(0).time!!.second.toString(), reactions, request)
        }
        println(timeReq)
        println(type)
        val typeReq = requestHandler.handleRequestType(type.toString().substring(1, month.toString().length - 1) ?: "")
        if (typeReq.error != ErrorTypeResponce.NO_TYPE) {
            saveToSession("type", typeReq.roomList.get(0).type.toString(), reactions, request)
        }
        if (placeReq.error == ErrorTypeResponce.NO_PLACE) {
            reactions.go("say_place")
        } else if (month == null || day == null || dateReq!!.error == ErrorTypeResponce.NO_DATE) {
            reactions.go("say_date")
        } else if (timeReq.error == ErrorTypeResponce.NO_TIME) {
            reactions.go("say_time")
        } else if (typeReq.error == ErrorTypeResponce.NO_TYPE) {
            reactions.go("say_type")
        } else {
            reactions.say("Окей, бронирую")
            val response =
                requestHandler.handleRequest(place.toString(), time.toString(), month.toString(), day.toString(), type.toString())
            when (response.error) {
                ErrorTypeResponce.NO_PLACE -> reactions.go("/main_book/say_place")
                ErrorTypeResponce.NO_DATE -> reactions.go("/main_book/say_date")
                ErrorTypeResponce.NO_TIME -> reactions.go("/main_book/say_time")
                ErrorTypeResponce.NO_TYPE -> reactions.go("/main_book/say_type")
                ErrorTypeResponce.SUCCESS -> reactions.say(
                    "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} " +
                            "во время ${time?.toString() ?: "null"} " +
                            "вот это: ${type?.toString() ?: "null"}"
                )
            }
        }
        reactions.say(
            "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} " +
                    "во время ${time?.toString() ?: "null"} " +
                    "вот это: ${type?.toString() ?: "null"}"
        )
    }

    private fun saveToSession(parametr: String, value: String, reactions: Reactions, request: BotRequest) {
        val mapJson = HashMap<String, JsonElement>()
        val par = JsonPrimitive(value)
        mapJson[parametr] = par
        val objectJ = request.alice?.state?.session
        if (!objectJ.isNullOrEmpty()) for (key in objectJ.keys) {
            mapJson[key] = objectJ.get(key)!!
        }
        println(parametr + value)
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
