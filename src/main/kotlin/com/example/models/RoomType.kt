package com.example.models

enum class RoomType {
    AUDIENCE, MEETINGROOM, NONE;
    companion object {
        fun parseVal(roomTypeName: String): RoomType {
            return when(roomTypeName.toLowerCase()) {
                "auditorium" -> AUDIENCE
                "meeting_room" -> MEETINGROOM
                else -> NONE
            }
        }
    }
}
