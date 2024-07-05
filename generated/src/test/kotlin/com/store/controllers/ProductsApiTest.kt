package com.store.controllers

import com.store.entities.ErrorResponseBody
import com.store.entities.Product
import com.store.entities.ProductDetails
import com.store.entities.ProductId
import com.store.entities.ProductType
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class ProductsApiTest {

    private val api: ProductsApiController = ProductsApiController()

    /**
     * To test ProductsApiController.productsGet
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun productsGetTest() {
        val type: ProductType? = TODO()
        val response: ResponseEntity<List<Product>> = api.productsGet(type)

        // TODO: test validations
    }

    /**
     * To test ProductsApiController.productsPost
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun productsPostTest() {
        val productDetails: ProductDetails? = TODO()
        val response: ResponseEntity<ProductId> = api.productsPost(productDetails)

        // TODO: test validations
    }
}
