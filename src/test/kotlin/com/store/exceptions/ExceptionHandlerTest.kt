package com.store.exceptions

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.store.entities.ProductType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import kotlin.test.assertEquals

class ExceptionHandlerTest {

    private val exceptionHandler = ExceptionHandler()

    @Test
    fun `handleTypeMismatchException should return 400 status code and error message`() {
        val ex = MethodArgumentTypeMismatchException("name", ProductType::class.java, "value", null, null)

        val response = exceptionHandler.handleTypeMismatchException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Invalid input for parameter: value", response.body?.error)
    }

    @Test
    fun `handleJsonParseException should return 400 status code and error message`() {
        val ex = JsonParseException(null, "Invalid JSON format")

        val response = exceptionHandler.handleJsonParseException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Invalid JSON format", response.body?.error)
    }

    @Test
    fun `handleInvalidFormatException should return 400 status code and error message`() {
        val ex = HttpMessageNotReadableException("Invalid input", JsonMappingException(null, "Invalid input"))

        val response = exceptionHandler.handleInvalidFormatException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Invalid input", response.body?.error)
    }

    @Test
    fun `handleMethodArgumentNotValidException should return 400 status code and error message`() {
        val bindingResult = Mockito.mock(BindingResult::class.java)
        Mockito.`when`(bindingResult.fieldErrors).thenReturn(listOf(FieldError("objectName", "field", "defaultMessage")))
        val ex = Mockito.mock(MethodArgumentNotValidException::class.java)
        Mockito.`when`(ex.bindingResult).thenReturn(bindingResult)

        val response = exceptionHandler.handleMethodArgumentNotValidException(ex)

        assertEquals(400, response.statusCodeValue)
        Assertions.assertTrue(response.body?.error?.contains("defaultMessage") ?: false)
    }

    @Test
    fun `handleIllegalInputException should return 400 status code and error message`() {
        val ex = IllegalInputException(400,"Invalid input", "/products")

        val response = exceptionHandler.handleIllegalInputException(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Invalid input", response.body?.error)
    }

    @Test
    fun `handleAllOtherExceptions should return 400 status code and error message`() {
        val ex = Exception("Something went wrong! Please try again or contact support.")

        val response = exceptionHandler.handleAllOtherExceptions(ex)

        assertEquals(400, response.statusCodeValue)
        assertEquals("Something went wrong! Please try again or contact support.", response.body?.error)
    }
}
