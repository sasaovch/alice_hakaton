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
                val place = activator.alice?.slots?.get("place")?.value
                val date = activator.alice?.slots?.get("date")?.value
                val time = activator.alice?.slots?.get("time")?.value
                val type = activator.alice?.slots?.get("type")?.value

                if (place == null) {
                    reactions.sayRandom("Пожалуйста, уточните корпус",
                        "Окей, уточните тогда корпус, который вас интересует",
                        "Хорошо, какой корпус хотите рассмотреть?"
                    )
                    saveToSession("date", date.toString(), reactions, request)
                    saveToSession("time", time.toString(), reactions, request)
                    saveToSession("type", type.toString(), reactions, request)
                } else if (date == null) {
                    reactions.sayRandom("Пожалуйста, уточните дату",
                    "Хорошо, уточните тогда дату, которая вас интересует и время.",
                        "Да, конечно, подскажите пожалуйста интересуемую вас дату.",
                        "Хорошо, какая дата вас интересует?",
                        "Окей, какую дату хотите рассмотреть?",
                    )
                    saveToSession("place", place.toString(), reactions, request)
                    saveToSession("time", time.toString(), reactions, request)
                    saveToSession("type", type.toString(), reactions, request)
                } else if (time == null) {
                    reactions.sayRandom("Пожалуйста, уточните время",
                       "Да, конечно, подскажите пожалуйста, на какое время в указанную ранее вами дату вам было бы удобно?",
                      "Хорошо, уточните тогда интересуемое время для вас на указанную ранее дату.",
                     "Хорошо, какое время вас интересует в указанную вами ранее дату?",
                    "Окей, какое время хотите рассмотреть на указанную ранее дату?",
                    )
                    saveToSession("place", place.toString(), reactions, request)
                    saveToSession("date", date.toString(), reactions, request)
                    saveToSession("type", type.toString(), reactions, request)
                } else if (type == null) {
                    reactions.say("Пожалуйста, уточните: вам нужна переговорка или аудитория?")
                    saveToSession("place", place.toString(), reactions, request)
                    saveToSession("date", date.toString(), reactions, request)
                    saveToSession("time", time.toString(), reactions, request)
                } else {
                    reactions.say("Окей, бронирую")
                    val response = requestHandler.handleRequest(place.toString(), time.toString(), date.toString(), type.toString())
                    saveToSession("place", place.toString(), reactions, request)
                    saveToSession("date", date.toString(), reactions, request)
                    saveToSession("time", time.toString(), reactions, request)
                    saveToSession("type", type.toString(), reactions, request)

                    when (response.error) {
                        ErrorTypeResponce.NO_PLACE -> reactions.go("ask_place")
                        ErrorTypeResponce.NO_DATE -> reactions.go("ask_date")
                        ErrorTypeResponce.NO_TIME -> reactions.go("ask_time")
                        ErrorTypeResponce.NO_TYPE -> reactions.go("ask_type")
                        ErrorTypeResponce.SUCCESS -> reactions.say(
                            "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} " +
                                    "во время ${time?.toString() ?: "null"} " +
                                    "вот это: ${type?.toString() ?: "null"}"
                        )
//                    reactions.alice.sessionState(JsonObject())
                    }
//                val rootObject= JsonObject(mapOf({"place", place}))
//                rootObject.("place", responce.roomList.get(0).place)
//                reactions.alice?.sessionState(rootObject)
                }
                reactions.say(
                    "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} " +
                            "во время ${time?.toString() ?: "null"} " +
                            "вот это: ${type?.toString() ?: "null"}"
                )
            }

            state("ask_place") {
                activators {
                    intent("ask_place")
                }

                action {
                    println("ask_place state")
                    val place = activator.alice?.slots?.get("place")?.value

                    println(activator.alice?.slots)

                    val response = requestHandler.handleRequestPlace(place.toString())
                    when (response.error) {
                        ErrorTypeResponce.NO_PLACE -> reactions.say("ask_place")
                        ErrorTypeResponce.SUCCESS -> {
                            reactions.say(
                                "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} "
                            )
                            saveToSession("place", place.toString(), reactions, request)
                        }
                        }

                    reactions.go("/main_book")
                }
            }

            state("ask_date") {
                activators {
                    intent("ask_date")
                }

                action {
                    reactions.say("стейт спросить про дату") // TODO remove
                    val date = activator.alice?.slots?.get("date")?.value

                    val response = requestHandler.handleRequestDate(date.toString())
                    when (response.error) {
                        ErrorTypeResponce.NO_DATE -> reactions.say("ask_place")
                        ErrorTypeResponce.SUCCESS -> {
                            reactions.say(
                                "Юзер хочет забронировать на месте ${date?.toString() ?: "null"} "
                            )
                            saveToSession("date", date.toString(), reactions, request)
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
                    reactions.say("стейт спросить про время") // TODO remove
                    val time = activator.alice?.slots?.get("time").toString()

                    val response = requestHandler.handleRequestTime(time)
                    when (response.error) {
                        ErrorTypeResponce.NO_TIME -> reactions.say("ask_place")
                        ErrorTypeResponce.SUCCESS -> {
                            reactions.say(
                                "Юзер хочет забронировать на месте ${time?.toString() ?: "null"} "
                            )
                            saveToSession("time", time.toString(), reactions, request)
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
                    val type = activator.alice?.slots?.get("type").toString()

                    val response = requestHandler.handleRequestType(type)
                    when (response.error) {
                        ErrorTypeResponce.NO_TYPE -> reactions.say("ask_place")
                        ErrorTypeResponce.SUCCESS -> {
                            reactions.say(
                                "Юзер хочет забронировать на месте ${type?.toString() ?: "null"} "
                            )
                            saveToSession("type", type.toString(), reactions, request)
                        }
                    }

                    reactions.go("/main_book")
                }
            }

        }
        fallback {
            reactions.say("Не распарсилось")
        }
    }

    private fun saveToSession(parametr: String, value: String, reactions: Reactions, request: BotRequest) {
        val objectJ = request.alice?.state?.session
        val mapJson = HashMap<String, JsonElement>()
        val par = JsonPrimitive(value)
        mapJson[parametr] = par
        for (par in objectJ!!.keys) {
            mapJson[par] = objectJ.getObject(par)
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
