package com.example.models

import java.util.Date
data class Room(
    val place: Place?,
    val time: Date?,
    val date: Date?,
) {
    var roomId: Int? = 0
    var type: RoomType = RoomType.NONE
    constructor(
        place: Place?,
        time: Date?,
        date: Date?,
        roomId: Int?,
    ) : this(place, time, date) {
        this.roomId = roomId
    }
    constructor(
        place: Place?,
        time: Date?,
        date: Date?,
        roomId: Int?,
        type: RoomType
    ) : this(place, time, date) {
        this.roomId = roomId
        this.type = type
    }
}