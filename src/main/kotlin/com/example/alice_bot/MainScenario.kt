package com.example.alice_bot

import com.example.handler.RequestHandler
import com.example.models.ErrorTypeResponse
import com.justai.jaicf.channel.yandexalice.activator.alice
import com.justai.jaicf.channel.yandexalice.model.AliceEvent
import com.justai.jaicf.model.scenario.Scenario

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
                    reactions.say("Окей, бронирую")
                    println(activator.alice?.slots)

                    val responce = requestHandler.handleRequest(place.toString(), time.toString(), date.toString(), type.toString())
                    when (responce.error) {
                        ErrorTypeResponse.NO_PLACE -> reactions.go("ask_place")
                        ErrorTypeResponse.NO_DATE -> reactions.go("ask_date")
                        ErrorTypeResponse.NO_TIME -> reactions.go("ask_time")
                        ErrorTypeResponse.NO_TYPE -> reactions.go("ask_type")
                        ErrorTypeResponse.SUCCESS -> reactions.say(
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
                        ErrorTypeResponse.NO_PLACE -> reactions.say("ask_place")
                        ErrorTypeResponse.SUCCESS -> reactions.say(
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
                    reactions.say("стейт спросить про дату") // TODO remove
                    val date = activator.alice?.slots?.get("date")?.value

                    val responce = requestHandler.handleRequestDate(date.toString())
                    when (responce.error) {
                        ErrorTypeResponse.NO_DATE -> reactions.say("ask_place")
                        ErrorTypeResponse.SUCCESS -> reactions.say(
                            "Юзер хочет забронировать на месте ${date?.toString() ?: "null"} "
                        )
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
                        ErrorTypeResponse.SUCCESS -> reactions.say(
                            "Юзер хочет забронировать на месте ${time?: "null"} "
                        )
                        else -> {reactions.say("ask_place")}
                    }

                    reactions.go("/main_book")
                }

            }

            state("ask_type") {
                activators {
                    intent("ask_type")
                }

                action {
                    val type = activator.alice?.slots?.get("place").toString()

                    val response = requestHandler.handleRequestType(type)
                    when (response.error) {
                        ErrorTypeResponse.SUCCESS -> reactions.say(
                            "Юзер хочет забронировать на месте ${type} "
                        )
                        else -> {reactions.say("ask_place")}
                    }

                    reactions.go("/main_book")
                }
            }

        }

        fallback {
            reactions.say("Не распарсилось")
        }
    }
}
