package com.example.models

data class TimeToBook(
    val month: Int?,
    val day: Long?,
    val hour: Long?,
    val minute: Long?,
    val monthIsRelative: Boolean = false,
    val dayIsRelative: Boolean = false,
    val hourIsRelative: Boolean = false,
    val minuteIsRelative: Boolean = false
)