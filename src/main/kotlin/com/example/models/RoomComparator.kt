package com.example.models

class RoomComparator : Comparator<Room> {
    override fun compare(room1: Room, room2: Room): Int {
        if (room1.month?.value != room2.month?.value) {
            return room1.month?.value!! - room2.month?.value!!
        }
        if (room1.day != room2.day) {
            return room1.day!! - room2.day!!
        }
        if (room1.hour != room2.hour) {
            return room1.hour!! - room2.hour!!
        }
        return room1.minute!! - room2.minute!!

    }
}