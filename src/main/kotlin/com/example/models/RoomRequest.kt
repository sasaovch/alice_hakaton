package com.example.models

import com.example.util.removeQuotations
import kotlinx.serialization.json.JsonElement

data class RoomRequest(
    var place: String,
    var month: String,
    var day: String,
    var time: String,
    var type: String
) {
    constructor(
        place: JsonElement?,
        month: JsonElement?,
        day: JsonElement?,
        time: JsonElement?,
        type: JsonElement?
    ) : this(
        removeQuotations(place?: ""),
        removeQuotations(month?: ""),
        removeQuotations(day?: ""),
        removeQuotations(time?: ""),
        removeQuotations(type?: "")
    )

    public fun dateIsCorrect(): Boolean {
        return month.isNotEmpty() && day.isNotEmpty()
    }
}
