package com.example.util

fun removeQuotations (obj: Any): String {
    return obj.toString().toString().replace("\"", "")
}