package com.example.models

import java.time.Month

fun translateAndGetMonth(russianName: String): Month? {
    when(russianName.toLowerCase()) {
        "январь" -> return Month.JANUARY
        "февраль" -> return Month.FEBRUARY
        "март" -> return Month.MARCH
        "апрель" -> return Month.APRIL
        "май" -> return Month.MAY
        "июнь" -> return Month.JUNE
        "июль" -> return Month.JULY
        "август" -> return Month.AUGUST
        "сентябрь" -> return Month.SEPTEMBER
        "октябрь" -> return Month.OCTOBER
        "ноябрь" -> return Month.NOVEMBER
        "декабрь" -> return Month.DECEMBER
        else -> return null
    }
}
