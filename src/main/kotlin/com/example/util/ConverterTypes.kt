package com.example.util

import com.example.models.RoomResponce
import java.time.Month

fun convertTimeToString(time: RoomResponce): String {
    val minutes = time.roomList[0].time!!.second.toString()
    val hours = time.roomList[0].time!!.first.toString()
    if (minutes.length == 1) {
        return "$hours $minutes 0"
    }
    return "$hours $minutes"
}

fun convertTimeToRussion(timeRoom: Pair<Int, Int>): String {
    return timeRoom.first.toString() + ":" + timeRoom.second.toString()
}

fun convertTimeToHHMMFormat(time: Pair<Int, Int>): String {
    if (time.second < 10) {
        return time.first.toString() + ":0" + time.second.toString()
    } else if (time.second < 30) {
        return time.first.toString() + ":" + time.second.toString()
    } else if (time.second < 40){
        return time.first.toString() + ":" + time.second.toString()
    } else {
        return time.first.toString() + ":" + time.second.toString()
    }
}

fun convertDateToDDMMYYYYFormat(day: Int, month: Month): String {
    if (month.value < 10) {
        return day.toString() + ".0" + month.value + "." + "2023"
    }
    return day.toString() + "." + month.value + "." + "2023"
}
