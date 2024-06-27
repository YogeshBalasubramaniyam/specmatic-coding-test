package com.store.exceptions

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.store.entities.ErrorResponse
import com.store.entities.ProductType
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import kotlin.test.assertEquals

class ExceptionHandlerTest {

    private val exceptionHandler = ExceptionHandler()

    @Test
    fun `test handleTypeMismatchException with ProductType`() {
        val ex = Mockito.mock(MethodArgumentTypeMismatchException::class.java)
        Mockito.`when`(ex.requiredType).thenReturn(ProductType::class.java)
        Mockito.`when`(ex.value).thenReturn("invalidType")

        val response: ResponseEntity<ErrorResponse> = exceptionHandler.handleTypeMismatchException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Invalid product type: invalidType", response.body?.error)
        assertEquals("/products", response.body?.path)
    }

    @Test
    fun `test handleIllegalInputException`() {
        val ex = IllegalInputException(400, "Illegal input", "/products")

        val response: ResponseEntity<ErrorResponse> = exceptionHandler.handleIllegalInputException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Illegal input", response.body?.error)
        assertEquals("/products", response.body?.path)
    }

    @Test
    fun `test handleInvalidFormatException with JsonMappingException for Product name required`() {
        val cause = Mockito.mock(JsonMappingException::class.java)
        Mockito.`when`(cause.message).thenReturn("ProductDetails.<init>, parameter name")

        val ex = HttpMessageNotReadableException("Invalid input", cause)

        val response: ResponseEntity<ErrorResponse> = exceptionHandler.handleInvalidFormatException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Product name is required", response.body?.error)
        assertEquals("/products", response.body?.path)
    }

    @Test
    fun `test handleInvalidFormatException with JsonMappingException for Product type required`() {
        val cause = Mockito.mock(JsonMappingException::class.java)
        Mockito.`when`(cause.message).thenReturn("ProductDetails.<init>, parameter type")

        val ex = HttpMessageNotReadableException("Invalid input", cause)

        val response: ResponseEntity<ErrorResponse> = exceptionHandler.handleInvalidFormatException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Product type is required", response.body?.error)
        assertEquals("/products", response.body?.path)
    }

    @Test
    fun `test handleInvalidFormatException with JsonMappingException for Product cost as null`() {
        val cause = Mockito.mock(JsonMappingException::class.java)
        Mockito.`when`(cause.message).thenReturn("value failed for JSON property cost due to missing (therefore NULL) value for creator parameter cost which is a non-nullable type")

        val ex = HttpMessageNotReadableException("Invalid input", cause)

        val response: ResponseEntity<ErrorResponse> = exceptionHandler.handleInvalidFormatException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Product cost cannot be null", response.body?.error)
        assertEquals("/products", response.body?.path)
    }

    @Test
    fun `test handleInvalidFormatException with JsonMappingException for empty product type`() {
        val cause = Mockito.mock(JsonMappingException::class.java)
        Mockito.`when`(cause.message).thenReturn("Cannot coerce empty String (\"\") to `com.store.entities.ProductType`")

        val ex = HttpMessageNotReadableException("Invalid input", cause)

        val response: ResponseEntity<ErrorResponse> = exceptionHandler.handleInvalidFormatException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Product type is missing or invalid", response.body?.error)
        assertEquals("/products", response.body?.path)
    }

    @Test
    fun `test handleInvalidFormatException with JsonMappingException for Invalid product type`() {
        val cause = Mockito.mock(JsonMappingException::class.java)
        Mockito.`when`(cause.message).thenReturn("Cannot deserialize value of type `com.store.entities.ProductType`")

        val ex = HttpMessageNotReadableException("Invalid input", cause)

        val response: ResponseEntity<ErrorResponse> = exceptionHandler.handleInvalidFormatException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Invalid product type", response.body?.error)
        assertEquals("/products", response.body?.path)
    }

    @Test
    fun `test handleInvalidFormatException with JsonParseException`() {
        val cause = Mockito.mock(JsonParseException::class.java)

        val ex = HttpMessageNotReadableException("Invalid input", cause)

        val response: ResponseEntity<ErrorResponse> = exceptionHandler.handleInvalidFormatException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Invalid JSON format", response.body?.error)
        assertEquals("/products", response.body?.path)
    }

    @Test
    fun `test handleAllOtherExceptions`() {
        val ex = Exception("Unexpected error")

        val response: ResponseEntity<ErrorResponse> = exceptionHandler.handleAllOtherExceptions(ex)

        assertEquals(500, response.statusCodeValue)
        assertEquals("Unexpected error", response.body?.error)
        assertEquals("Not Known", response.body?.path)
    }
}
