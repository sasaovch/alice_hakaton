package com.example.models

data class TimeToBook(
    val month: Int?,
    val day: Long?,
    val hour: Long?,
    val minute: Long?,
    val month_is_relative: Boolean = false,
    val day_is_relative: Boolean = false,
    val hour_is_relative: Boolean = false,
    val minute_is_relative: Boolean = false
)