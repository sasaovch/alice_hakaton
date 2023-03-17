package com.example.models

enum class RoomType {
    AUDIENCE, MEETINGROOM, NONE;
    companion object {
        fun parseVal(roomTypeName: String): RoomType {
            return when(roomTypeName.toLowerCase()) {
                "auditorium" -> AUDIENCE
                "meeting_room" -> MEETINGROOM
                "audience" -> AUDIENCE
                "meetingroom" -> MEETINGROOM
                else -> NONE
            }
        }
    }
    fun getNominativeCase(): String {
        when (this) {
            RoomType.AUDIENCE -> return "аудитория"
            RoomType.MEETINGROOM -> return "переговорка"
            else -> return "помещение"
        }
    }

    fun getRepositionalCase(): String {
        when (this) {
            RoomType.AUDIENCE -> return "аудитории"
            RoomType.MEETINGROOM -> return "переговорке"
            else -> return "помещении"
        }
    }

    fun getParentCase(): String {
        when (this) {
            RoomType.AUDIENCE -> return "аудитории"
            RoomType.MEETINGROOM -> return "переговорки"
            else -> return "помещения"
        }
    }
}
