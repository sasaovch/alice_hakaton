package com.example

enum class Place(names: List<String>) {
    LOMONOSOVA(listOf("Ломо", "Ломоносова 9")),
    KRONVERSKY(listOf("Кронва", "Кронверский 49")),
    NONE(emptyList());

    companion object {
        fun parseVal(placeName: String): Place {
            return Place.LOMONOSOVA
        }
    }
}
