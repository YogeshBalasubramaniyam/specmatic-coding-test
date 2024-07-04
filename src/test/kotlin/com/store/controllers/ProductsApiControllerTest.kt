package com.store.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.store.entities.Product
import com.store.entities.ProductDetails
import com.store.entities.ProductId
import com.store.entities.ProductType
import com.store.services.interfaces.IProductService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class ProductsControllerTest {

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var productService: IProductService

    @InjectMocks
    private lateinit var productsController: ProductsApiController

    private val objectMapper = jacksonObjectMapper()

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productsController).build()
    }

    @Test
    fun `test getProducts endpoint`() {
        val product1 = Product(1, "Product 1", ProductType.other, 100)
        val product2 = Product(2, "Product 2", ProductType.other, 50)
        val products = listOf(product1, product2)

        Mockito.`when`(productService.getProductsByType(ProductType.other)).thenReturn(products)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/products?type=other")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
        Mockito.verify(productService).getProductsByType(ProductType.other)
    }

    @Test
    fun `test getProducts endpoint without type`() {
        val product1 = Product(1, "Product 1", ProductType.book, 100)
        val product2 = Product(2, "Product 2", ProductType.gadget, 50)
        val product3 = Product(2, "Product 2", ProductType.food, 500)
        val product4 = Product(2, "Product 2", ProductType.other, 20)
        val products = listOf(product1, product2, product3, product4)

        Mockito.`when`(productService.getProductsByType(null)).thenReturn(products)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/products")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
        Mockito.verify(productService).getProductsByType(null)
    }

    @Test
    fun `test getProducts endpoint with invalid type`() {
        val invalidType = "invalidType"

        mockMvc.perform(
            MockMvcRequestBuilders.get("/products?type=$invalidType")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `test createProduct endpoint`() {
        val productDetails = ProductDetails("Product 1", ProductType.book, 100)
        val productId = ProductId(1)

        Mockito.`when`(productService.createProduct(productDetails)).thenReturn(productId)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDetails))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andReturn()

        val response = result.response
        val responseBody = response.contentAsString
        assert(responseBody.contains("\"id\":1"))
        Mockito.verify(productService).createProduct(productDetails)
    }


    @Test
    fun `test createProduct endpoint without product details`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `test createProduct endpoint with cost`() {
        val productDetails = ProductDetails("Product 1", ProductType.book, 100, BigDecimal(100))
        val productId = ProductId(1)

        Mockito.`when`(productService.createProduct(productDetails)).thenReturn(productId)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDetails))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andReturn()

        val response = result.response
        val responseBody = response.contentAsString
        assert(responseBody.contains("\"id\":1"))
        Mockito.verify(productService).createProduct(productDetails)
    }
}
