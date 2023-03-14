package com.example

import com.justai.jaicf.BotEngine
import com.justai.jaicf.activator.intent.IntentActivator
import com.justai.jaicf.activator.regex.RegexActivator
import com.justai.jaicf.channel.yandexalice.AliceChannel
import com.justai.jaicf.channel.yandexalice.activator.AliceIntentActivator

val skill = BotEngine(
    model = MainScenario.model,
    activators = arrayOf(
        AliceIntentActivator,
        RegexActivator
    )
)

val channel = AliceChannel(
    botApi = skill
)