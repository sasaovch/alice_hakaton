package com.example.models

enum class Place(val names: List<String>) {
    LOMONOSOVA(listOf("Ломо", "Ломоносова 9")),
    KRONVERSKY(listOf("Кронва", "Кронверский 49")),
    NONE(emptyList());

    companion object {
        fun parseVal(placeName: String): Place {
            return when (placeName.toLowerCase()) {
                "lomo" -> LOMONOSOVA
                "kronva" -> KRONVERSKY
                "kronversky" -> KRONVERSKY
                "lomonosova" -> LOMONOSOVA
                else -> NONE
            }
        }
    }

    public fun getNominativeCase(): String {
        when (this) {
            Place.KRONVERSKY -> return "Кронверский"
            Place.LOMONOSOVA -> return "Ломоносова"
            else -> return "корпус"
        }
    }
    public fun getRepositionalCase(): String {
        when (this) {
            Place.KRONVERSKY -> return "Кронверском"
            Place.LOMONOSOVA -> return "Ломоносова"
            else -> return "корпус"
        }
    }
}
