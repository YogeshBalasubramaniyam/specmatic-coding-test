package com.store.exceptions

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.store.entities.ErrorResponse
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        if (ex.requiredType?.name == "com.store.entities.ProductType") {
            val error = ErrorResponse(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                status = 400,
                error = "Invalid product type: ${ex.value}",
                path = "/products"
            )
            return ResponseEntity.status(400).body(error)
        }
        return handleAllOtherExceptions(ex)
    }

    @ExceptionHandler(IllegalInputException::class)
    fun handleIllegalInputException(ex: IllegalInputException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            status = ex.status,
            error = ex.message,
            path = ex.path
        )
        return ResponseEntity.status(400).body(error)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidFormatException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val cause = ex.cause
        var error: ErrorResponse? = null

        if (cause is JsonMappingException) {
            val message = cause.message
            val customMessage = when {
                message?.contains("ProductDetails.<init>, parameter name") == true -> "Product name is required"
                message?.contains("ProductDetails.<init>, parameter type") == true -> "Product type is required"
                message?.contains("Cannot deserialize value of type `com.store.entities.ProductType`") == true -> "Invalid product type"
                message?.contains("value failed for JSON property cost due to missing (therefore NULL) value for creator parameter cost which is a non-nullable type") == true -> "Product cost cannot be null"
                message?.contains("Cannot coerce empty String (\"\") to `com.store.entities.ProductType`") == true -> "Product type is missing or invalid"
                else -> message ?: "Invalid input format"
            }
            error = ErrorResponse(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                status = 400,
                error = customMessage,
                path = "/products"
            )
        } else if (cause is JsonParseException) {
            error = ErrorResponse(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                status = 400,
                error = "Invalid JSON format",
                path = "/products"
            )
        }

        return ResponseEntity.status(400).body(error)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllOtherExceptions(ex: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            status = 500,
            error = ex.message ?: "Internal Server Error",
            path = "Not Known"
        )
        return ResponseEntity.status(500).body(error)
    }
}