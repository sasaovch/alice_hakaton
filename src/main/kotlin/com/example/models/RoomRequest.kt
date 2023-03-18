package com.example.models

import com.example.util.removeQuotations
import kotlinx.serialization.json.JsonElement

data class RoomRequest(
    var place: String,
    var month: String,
    var day: String,
    var time: String,
    var type: String,
    var today: String
) {
    constructor(
        place: JsonElement?,
        month: JsonElement?,
        day: JsonElement?,
        time: JsonElement?,
        type: JsonElement?,
        today: JsonElement?
    ) : this(
        removeQuotations(place?: ""),
        removeQuotations(month?: ""),
        removeQuotations(day?: ""),
        removeQuotations(time?: ""),
        removeQuotations(type?: ""),
        removeQuotations(today?: "")
    )

    public fun dateIsCorrect(): Boolean {
        return month.isNotEmpty() && day.isNotEmpty()
    }
}
