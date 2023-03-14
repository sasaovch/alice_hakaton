package com.example

data class ScheduledRoom(
    val date: String,
    val schedule: HashMap<String, Boolean>,
    val roomId: Int,
    )