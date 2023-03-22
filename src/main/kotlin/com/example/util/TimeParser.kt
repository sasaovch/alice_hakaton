package com.example.util

import com.example.models.RoomType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TimeParser {
    companion object {
        var pattern = "hh:mm"
        var timeFormat = SimpleDateFormat(pattern)

        fun parseTime(begin: String, end: String): List<String> {
            timeFormat.timeZone = TimeZone.getTimeZone("UTC")
            val parsed = ArrayList<String>()
                var timeBegin : Date = timeFormat.parse(begin)
                val timeEnd : Date = timeFormat.parse(end)
                val delta : Date = timeFormat.parse("00:30")
                var cur : Long = timeBegin.time +  delta.time
                while(cur <= timeEnd.time) {
                    parsed.add("${timeFormat.format(timeBegin)}-${timeFormat.format(cur)}")
                    timeBegin = Date(cur)
                    cur += delta.time

                }
                return parsed
        }
    }
}