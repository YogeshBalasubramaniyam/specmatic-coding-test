package com.store.exceptions

class IllegalInputException(
    val status: Int,
    override val message: String,
    val path: String
) : RuntimeException(message)