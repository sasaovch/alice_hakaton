package com.example.models

import java.time.Month
data class Room(
    val place: Place? = null,
    val time: Pair<Int, Int>? = null,
    val day: Int? = null,
    val month: Month? = null,
    var roomId: Int? = 0,
    var type: RoomType = RoomType.NONE
)