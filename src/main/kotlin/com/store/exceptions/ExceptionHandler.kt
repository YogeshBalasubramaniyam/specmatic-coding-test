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
            val error = createErrorResponse("Invalid product type")
            return ResponseEntity.status(400).body(error)
        }
        return handleAllOtherExceptions(ex)
    }

    @ExceptionHandler(JsonParseException::class)
    fun handleJsonParseException(ex: JsonParseException): ResponseEntity<ErrorResponse> {
        val error = createErrorResponse("Invalid JSON format")
        return ResponseEntity.status(400).body(error)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidFormatException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val cause = ex.cause
        var error: ErrorResponse? = null

        error = when (cause) {
            is JsonMappingException -> handleJsonMappingExceptionCause(cause)
            is JsonParseException -> handleJsonParseException()
            is IllegalInputException -> handleIllegalInputException(cause)
            else -> null
        }

        return ResponseEntity.status(400).body(error)
    }

    private fun handleJsonMappingExceptionCause(cause: JsonMappingException): ErrorResponse {
        return if (cause is com.fasterxml.jackson.databind.exc.ValueInstantiationException) {
            handleValueInstantiationException(cause)
        } else {
            handleJsonMappingException(cause)
        }
    }

    private fun handleValueInstantiationException(ex: com.fasterxml.jackson.databind.exc.ValueInstantiationException): ErrorResponse {
        val message = ex.message
        val errorMessages = mapOf(
            "Cannot construct instance of `com.store.entities.ProductDetails`, problem: Product name cannot be empty or blank" to "Product name cannot be empty or blank",
            "Cannot construct instance of `com.store.entities.ProductDetails`, problem: Inventory must be between 1 and 9999" to "Inventory must be between 1 and 9999"
        )
        val customMessage = errorMessages.entries.find { message?.contains(it.key) == true }?.value ?: message ?: "Invalid input"
        return createErrorResponse(customMessage)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllOtherExceptions(ex: Exception): ResponseEntity<ErrorResponse> {
        val error = createErrorResponse(ex.message ?: "Something went wrong! Please try again or contact support.")
        return ResponseEntity.status(400).body(error)
    }

    private fun handleIllegalInputException(ex: IllegalInputException): ErrorResponse {
        return createErrorResponse(ex.message ?: "Invalid input")
    }

    private fun handleJsonMappingException(cause: JsonMappingException): ErrorResponse {
        val message = cause.message
        val errorMessages = mapOf(
            "ProductDetails.<init>, parameter name" to "Product name is required",
            "ProductDetails.<init>, parameter type" to "Product type is required",
            "Cannot deserialize value of type `com.store.entities.ProductType`" to "Invalid product type",
            "value failed for JSON property cost due to missing (therefore NULL) value for creator parameter cost which is a non-nullable type" to "Product cost cannot be null",
            "Cannot coerce empty String (\"\") to `com.store.entities.ProductType`" to "Product type is missing or invalid",
        )

        val customMessage = errorMessages.entries.find { message?.contains(it.key) == true }?.value ?: message ?: "Invalid input format"
        return createErrorResponse(customMessage)
    }

    private fun handleJsonParseException(): ErrorResponse {
        return createErrorResponse("Invalid JSON format")
    }

    private fun createErrorResponse(error: String): ErrorResponse {
        return ErrorResponse(
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            status = 400,
            error = error,
            path = "/products"
        )
    }
}