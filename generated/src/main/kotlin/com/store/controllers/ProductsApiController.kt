package com.store.controllers

import com.store.entities.ErrorResponseBody
import com.store.entities.Product
import com.store.entities.ProductDetails
import com.store.entities.ProductId
import com.store.entities.ProductType
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.enums.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.security.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.Valid
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

import kotlin.collections.List
import kotlin.collections.Map

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class ProductsApiController() {

    @Operation(
        summary = "GET Products based on type",
        operationId = "productsGet",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "List of products in the response", content = [Content(array = ArraySchema(schema = Schema(implementation = Product::class)))]),
            ApiResponse(responseCode = "400", description = "Bad Request", content = [Content(schema = Schema(implementation = ErrorResponseBody::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/products"],
        produces = ["application/json"]
    )
    fun productsGet(@Parameter(description = "", schema = Schema(allowableValues = ["book", "food", "gadget", "other"])) @Valid @RequestParam(value = "type", required = false) type: ProductType?): ResponseEntity<List<Product>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "POST /products",
        operationId = "productsPost",
        description = """""",
        responses = [
            ApiResponse(responseCode = "201", description = "POST /products", content = [Content(schema = Schema(implementation = ProductId::class))]),
            ApiResponse(responseCode = "400", description = "Bad Request", content = [Content(schema = Schema(implementation = ErrorResponseBody::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/products"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun productsPost(@Parameter(description = "") @Valid @RequestBody(required = false) productDetails: ProductDetails?): ResponseEntity<ProductId> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
