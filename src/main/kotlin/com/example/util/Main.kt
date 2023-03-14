package com.example.util

fun main(args: Array<String>) {
    val infoHandler : InfoHandler = InfoHandler;


     infoHandler.checkInstance()



    val ans =infoHandler.getFreeRooms(InfoHandler.Place.KRONVA, "15:30-16:00", "25.03.2023", InfoHandler.Type.MEETING_ROOM)
    println(ans)

    return
}