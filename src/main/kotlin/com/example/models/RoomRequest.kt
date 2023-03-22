package com.example.models

import com.example.util.removeQuotations
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

data class RoomRequest(
    var place: String,
    var timeToBook: String,
    var month: String,
    var day: String,
    var hour: String,
    var minute: String,
    var time: String,
    var type: String,
    var today: String,
    val numberMembers: String
) {
    constructor(
        place: JsonElement?,
        timeToBook: JsonElement?,
        month: String?,
        day: String?,
        hour: String?,
        minute: String?,
        time: JsonElement?,
        type: JsonElement?,
        today: JsonElement?,
        numberMembers: JsonElement?
    ) : this(
        removeQuotations(place?: ""),
        timeToBook.toString(),
        month?: "",
        day?: "",
        hour?: "",
        minute?: "",
        removeQuotations(time?: ""),
        removeQuotations(type?: ""),
        removeQuotations(today?: ""),
        removeQuotations(numberMembers?: "")
    )

    public fun dateIsCorrect(): Boolean {
        return month.isNotEmpty() && day.isNotEmpty()
    }
}
