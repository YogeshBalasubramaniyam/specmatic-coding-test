package com.store.exceptions

import com.fasterxml.jackson.core.JsonParseException
import com.store.entities.ErrorResponseBody
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponseBody> {
        val errorMessages = "Invalid input for parameter: ${ex.name}"
        val error = createErrorResponse(errorMessages)
        return ResponseEntity.status(400).body(error)
    }

    @ExceptionHandler(JsonParseException::class)
    fun handleJsonParseException(ex: JsonParseException): ResponseEntity<ErrorResponseBody> {
        val error = createErrorResponse("Invalid JSON format")
        return ResponseEntity.status(400).body(error)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidFormatException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponseBody> {
        val cause = ex.mostSpecificCause
        val error: ErrorResponseBody = createErrorResponse(cause.message ?: "Invalid input")
        return ResponseEntity.status(400).body(error)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponseBody> {
        val errorMessages = ex.bindingResult.fieldErrors.map { it.defaultMessage }.joinToString("; ")
        val error = createErrorResponse(errorMessages)
        return ResponseEntity.status(400).body(error)
    }

    @ExceptionHandler(IllegalInputException::class)
    fun handleIllegalInputException(e: IllegalInputException): ResponseEntity<ErrorResponseBody> {
        val error = createErrorResponse(e.message ?: "Invalid input")
        return ResponseEntity.status(400).body(error)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllOtherExceptions(ex: Exception): ResponseEntity<ErrorResponseBody> {
        val error = createErrorResponse(ex.message ?: "Something went wrong! Please try again or contact support.")
        return ResponseEntity.status(400).body(error)
    }

    private fun createErrorResponse(error: String): ErrorResponseBody {
        return ErrorResponseBody(
            timestamp = java.time.OffsetDateTime.now(),
            status = 400,
            error = error,
            path = "/products"
        )
    }
}