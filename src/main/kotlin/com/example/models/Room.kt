package com.example.models

import java.time.Month
data class Room(
    var place: Place? = null,
    var time: Pair<Int, Int>? = null,
    var day: Int? = null,
    var month: Month? = null,
    var roomId: Int? = 0,
    var type: RoomType = RoomType.NONE,
    var duration: Int? = 30,
    var today: DayOfWeek = DayOfWeek.NONE,
    var numberMem: Int = 0

)