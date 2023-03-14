package com.example.alice_bot

import com.example.handler.RequestHandler
import com.justai.jaicf.BotEngine
import com.justai.jaicf.activator.regex.RegexActivator
import com.justai.jaicf.channel.yandexalice.AliceChannel
import com.justai.jaicf.channel.yandexalice.activator.AliceIntentActivator

val skill = BotEngine(
    model = MainScenario(RequestHandler()).model,
    activators = arrayOf(
        AliceIntentActivator,
        RegexActivator
    )
)

val channel = AliceChannel(
    botApi = skill
)