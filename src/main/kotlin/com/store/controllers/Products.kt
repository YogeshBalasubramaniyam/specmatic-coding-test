package com.store.controllers

import com.store.entities.Product
import com.store.entities.ProductDetails
import com.store.entities.ProductId
import com.store.entities.ProductType
import com.store.services.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/products")
class Products (private val productService: ProductService) {

    @GetMapping
    fun getProducts(@RequestParam(required = false) type: ProductType?): ResponseEntity<List<Product>> {
        val products = productService.getProductsByType(type)
        return ResponseEntity.ok(products)
    }

    @PostMapping
    fun createProduct(@RequestBody productDetails: ProductDetails): ResponseEntity<ProductId> {
        val productId = productService.createProduct(productDetails)
        val location: URI = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(productId)
            .toUri()
        return ResponseEntity.created(location).body(productId)
    }
}