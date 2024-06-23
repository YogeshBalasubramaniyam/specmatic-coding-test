package com.store.entities

data class ErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val path: String
)