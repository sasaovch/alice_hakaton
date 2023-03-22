package com.example.util

import java.time.Month

fun Month.checkDay(day: Int): Boolean {
    val longMonth = setOf(Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY, Month.AUGUST, Month.OCTOBER, Month.DECEMBER)
    if (this == Month.FEBRUARY) {
        return day in 1..28
    } else if (this in longMonth){
        return day in 1..31
    } else {
        return day in 1..30
    }
}