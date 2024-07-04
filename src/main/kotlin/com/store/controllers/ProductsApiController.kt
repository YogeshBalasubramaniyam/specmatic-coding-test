package com.store.controllers

import com.store.entities.*
import com.store.exceptions.IllegalInputException
import com.store.services.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
open class ProductsApiController(private val productService: ProductService) {

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
    open fun productsGet(@Parameter(description = "", schema = Schema(allowableValues = ["book", "food", "gadget", "other"])) @Valid @RequestParam(value = "type", required = false) type: ProductType?): ResponseEntity<List<Product>> {
        return ResponseEntity(productService.getProductsByType(type), HttpStatus.OK)
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
    open fun productsPost(@Parameter(description = "") @Valid @RequestBody(required = false) productDetails: ProductDetails?): ResponseEntity<ProductId> {
        productDetails ?: throw IllegalInputException(400, "Product details must be provided", "/products")
        return ResponseEntity(productService.createProduct(productDetails), HttpStatus.CREATED)
    }
}