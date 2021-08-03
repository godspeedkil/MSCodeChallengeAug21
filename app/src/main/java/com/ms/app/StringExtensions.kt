package com.ms.app

fun String.nullIfBlank(): String? {
    return if (isBlank()) {
        null
    } else {
        this
    }
}