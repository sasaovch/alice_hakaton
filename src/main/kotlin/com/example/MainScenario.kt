package com.example

import com.justai.jaicf.activator.intent.intent
import com.justai.jaicf.activator.regex.regex
import com.justai.jaicf.channel.yandexalice.activator.alice
import com.justai.jaicf.channel.yandexalice.model.AliceEvent
import com.justai.jaicf.model.scenario.Scenario
import java.util.regex.Pattern

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
                val place = activator.alice?.slots?.get("bookdate")
                val time = activator.alice?.slots?.get("where")
                val thing = activator.alice?.slots?.get("what")


                reactions.say(
                    "Юзер хочет забронировать на месте ${place?.toString() ?: "null"} " +
                            "во время ${time?.toString() ?: "null"} " +
                            "вот это: ${thing?.toString() ?: "null"}"
                )

            }
        }

//        state("please_work") {
//            activators {
//                regex("Привет")
//                regex("Пока")
//            }
//
//            action {
//                reactions.say("заработало")
//            }
//        }
//
//        state("please_work2") {
//            activators {
//                regex("привет моя милая (?<name>алиса|кошкодевочка)")
//            }
//
//            action {
//                val name = activator.regex?.group("name")
//                reactions.say("заработало вот: $name")
//            }
//        }
//
//        state("intent_test") {
//            activators {
//                intent("turn.on")
//            }
//
//            action {
//                val what = activator.alice?.slots?.get("what")
//                reactions.say("Интент turn.on работает интент, хотят включить это: ${what?.value}")
//            }
//        }

        /**
         * Любое другое сообщение навык просто будет возвращать обратно пользователю
         */
        fallback {
            reactions.say("Не распарсилось")
        }
    }
}
