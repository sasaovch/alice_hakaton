package com.example.alice_bot

import com.example.api_service.InfoHandler
import com.example.handler.RequestHandler
import com.example.models.ErrorTypeResponce
import com.example.models.Place
import com.example.models.Room
import com.example.models.RoomResponce
import com.justai.jaicf.channel.yandexalice.activator.alice
import com.justai.jaicf.channel.yandexalice.alice
import com.justai.jaicf.channel.yandexalice.model.AliceEvent
import com.justai.jaicf.model.scenario.Scenario
import io.ktor.http.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import java.sql.Date

/**
 * Этот файл содержит сценарий вашего навыка.
 * Читайте подробнее о том, как создавать диалоговые сценарии на https://github.com/just-ai/jaicf-kotlin/wiki
 */
class MainScenario (
    private val requestHandler: RequestHandler
) : Scenario() {
    init {

        /**
         * Стартовое сообщение, когда пользователь запускает ваш навык
         */
        state("start") {
            activators {
                event(AliceEvent.START)
                regex("/start")
            }

            action {
                reactions.say("Привет! Хотите забронировать аудиторию?")
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
                    reactions.say("Пожалуйста, уточните корпус")
                } else if (date == null) {
                    reactions.say("Пожалуйста, уточните дату")
                } else if (time == null) {
                    reactions.say("Пожалуйста, уточните время")
                } else if (type == null) {
                    reactions.say("Пожалуйста, уточните: вам нужна переговорка или аудитория?")
                } else {
                    println(activator.alice?.slots)

                    val responce = requestHandler.handleRequest(place.toString(), time.toString(), date.toString(), type.toString())
                    when (responce.error) {
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
                    reactions.say(
                        "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} " +
                                "во время ${time?.toString() ?: "null"} " +
                                "вот это: ${type?.toString() ?: "null"}"
                    )
//                val rootObject= JsonObject(mapOf({"place", place}))
//                rootObject.("place", responce.roomList.get(0).place)
//                reactions.alice?.sessionState(rootObject)
                }
            }

            state("ask_place") {
                activators {
                    intent("ask_place")
                }

                action {
                    println("ask_place state")
                    val place = activator.alice?.slots?.get("place")?.value

                    println(activator.alice?.slots)

                    val responce = requestHandler.handleRequestPlace(place.toString())
                    when (responce.error) {
                        ErrorTypeResponce.NO_PLACE -> reactions.say("ask_place")
                        ErrorTypeResponce.SUCCESS -> reactions.say(
                            "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} "
                        )
                    }

                    reactions.go("/main_book")
                }
            }

            state("ask_date") {
                activators {
                    intent("ask_date")
                }

                action {
                    val date = activator.alice?.slots?.get("date").toString()

                    val responce = requestHandler.handleRequestDate(date)
                    when (responce.error) {
                        ErrorTypeResponce.NO_DATE -> reactions.say("ask_place")
                        ErrorTypeResponce.SUCCESS -> reactions.say(
                            "Юзер хочет забронировать на месте ${date?.toString() ?: "null"} "
                        )
                    }
                }
            }


        }







        state("ask_time") {
            activators {
                intent("time")
            }

            action {
                val time = activator.alice?.slots?.get("time").toString()

                val responce = requestHandler.handleRequestTime(time)
                when (responce.error) {
                    ErrorTypeResponce.NO_TIME -> reactions.say("ask_place")
                    ErrorTypeResponce.SUCCESS -> reactions.say(
                        "Юзер хочет забронировать на месте ${time?.toString() ?: "null"} "
                    )
                }
            }
        }

        state("ask_type") {
            activators {
                intent("type")
            }

            action {
                val type = activator.alice?.slots?.get("place").toString()

                val responce = requestHandler.handleRequestType(type)
                when (responce.error) {
                    ErrorTypeResponce.NO_TYPE -> reactions.say("ask_place")
                    ErrorTypeResponce.SUCCESS -> reactions.say(
                        "Юзер хочет забронировать на месте ${type?.toString() ?: "null"} "
                    )
                }
            }
        }

        fallback {
            reactions.say("Не распарсилось")
        }
    }
}
