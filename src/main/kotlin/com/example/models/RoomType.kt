package com.example.models

enum class RoomType {
    AUDIENCE, MEETINGROOM, NONE;
    companion object {
        fun parseVal(roomTypeName: String): RoomType {
            return when(roomTypeName.toLowerCase()) {
                "аудитория" -> AUDIENCE
                "переговорка" -> MEETINGROOM
                else -> NONE
            }
        }
    }
}
