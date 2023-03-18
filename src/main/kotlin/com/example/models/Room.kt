package com.example.models

import java.time.Month
data class Room(
    var place: Place? = null,
    var time: Pair<Int, Int>? = null,
    var day: Int? = null,
    var month: Month? = null,
    var roomId: Int? = 0,
    var type: RoomType = RoomType.NONE,
    var today: DayOfWeek = DayOfWeek.NONE
)