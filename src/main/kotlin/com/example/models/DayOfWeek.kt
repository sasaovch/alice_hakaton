package com.example.models

enum class DayOfWeek {
    TODAY(),
    TOMORROW(),
    AFTER_TOMORROW(),
    NONE();
    companion object {
        fun parseVal(todayName: String): DayOfWeek {
            return when (todayName.toLowerCase()) {
                "tomorrow" -> TOMORROW
                "today" -> TODAY
                "aftertomorrow" -> AFTER_TOMORROW
                else -> NONE
            }
        }
    }
}