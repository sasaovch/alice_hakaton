package com.example

import java.util.Date
data class Room(
    val place: Place?,
    val time: Date?,
    val date: Date?,
) {
    constructor(
        place: Place?,
        time: Date?,
        date: Date?,
        roomId: Int?,
    ) : this(place, time, date)
}