package com.example.models

enum class DayOfWeek {
    TODAY(),
    TOMORROW(),
    NONE();
    companion object {
        fun parseVal(todayName: String): DayOfWeek {
            return when (todayName.toLowerCase()) {
                "tomorrow" -> TODAY
                "today" -> TOMORROW
                else -> NONE
            }
        }
    }
}