package com.example

import com.justai.jaicf.activator.intent.intent
import com.justai.jaicf.activator.regex.regex
import com.justai.jaicf.channel.yandexalice.activator.alice
import com.justai.jaicf.channel.yandexalice.alice
import com.justai.jaicf.channel.yandexalice.model.AliceEvent
import com.justai.jaicf.model.scenario.Scenario
import kotlinx.serialization.json.JsonObject
import java.sql.Date

/**
 * Этот файл содержит сценарий вашего навыка.
 * Читайте подробнее о том, как создавать диалоговые сценарии на https://github.com/just-ai/jaicf-kotlin/wiki
 */
object MainScenario : Scenario() {
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
                intent("book.exact")
            }

            action {
                val place = activator.alice?.slots?.get("bookdate").toString()
                val time = activator.alice?.slots?.get("where").toString()
                val thing = activator.alice?.slots?.get("what").toString()

                val responce = handleRequest(place, time, thing)
                when (responce.error) {
                    ErrorTypeResponce.NO_PLACE -> reactions.go("ask_place")
                    ErrorTypeResponce.NO_DATE -> reactions.go("ask_date")
                    ErrorTypeResponce.NO_TIME -> reactions.go("ask_time")
                    ErrorTypeResponce.SUCCESS -> reactions.say(
                        "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} " +
                                "во время ${time?.toString() ?: "null"} " +
                                "вот это: ${thing?.toString() ?: "null"}"
                    )
//                    reactions.alice.sessionState(JsonObject())
                }
            }
        }

        state("ask_place") {
            activators {
//                intent("book.exact")
            }

            action {
                val place = activator.alice?.slots?.get("bookdate").toString()

                val responce = handleRequest(place, null, null)
                when (responce.error) {
                    ErrorTypeResponce.NO_PLACE -> reactions.say("ask_place")
                    ErrorTypeResponce.SUCCESS -> reactions.say(
                        "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} "
                    )
                }
            }
        }

        fallback {
            reactions.say("Не распарсилось")
        }
    }

    fun handleRequest(place: String?, time: String?, date: String?): RoomResponce {
        val placeToBook: Place = if (place==null) Place.NONE else Place.parseVal(place)
        val timeToBook: Date = Date.valueOf(time)
        val dateToBook: Date = Date.valueOf(date)
        if (placeToBook != Place.NONE && timeToBook != null && dateToBook != null) {
            val infoHandler = InfoHandler
            val roomList = infoHandler.getFreeRoomByDateAndTime(Room(placeToBook, timeToBook, dateToBook))
            return RoomResponce(roomList, ErrorTypeResponce.SUCCESS)
        } else if (placeToBook == Place.NONE) {
            return RoomResponce(emptyList(), ErrorTypeResponce.NO_PLACE)
        } else if (timeToBook == null) {
            return RoomResponce(emptyList(), ErrorTypeResponce.NO_TIME)
        } else if (dateToBook == null) {
            return RoomResponce(emptyList(), ErrorTypeResponce.NO_DATE)
        }
        return RoomResponce(emptyList(), ErrorTypeResponce.NO_PLACE);
    }
}
