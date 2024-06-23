package com.store.services

import com.store.entities.Product
import com.store.entities.ProductDetails
import com.store.entities.ProductType
import com.store.exceptions.IllegalInputException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class ProductServiceTest {

    private lateinit var productService: ProductService

    @BeforeEach
    fun setup() {
        productService = ProductService()
    }

    @Test
    fun `test getProductsByType with null type returns all products`() {
        val products = ConcurrentHashMap<Int, Product>()
        val idCounter = AtomicInteger()
        products[1] = Product(1, "Product 1", ProductType.book, 10)
        products[2] = Product(2, "Product 2", ProductType.gadget, 20)
        productService.javaClass.getDeclaredField("products").apply {
            isAccessible = true
            set(productService, products)
        }
        productService.javaClass.getDeclaredField("idCounter").apply {
            isAccessible = true
            set(productService, idCounter)
        }

        val result = productService.getProductsByType(null)

        assertEquals(2, result.size)
        assertTrue(result.contains(products[1]))
        assertTrue(result.contains(products[2]))
    }

    @Test
    fun `test getProductsByType with specific type returns filtered products`() {
        val products = ConcurrentHashMap<Int, Product>()
        val idCounter = AtomicInteger()
        products[1] = Product(1, "Product 1", ProductType.book, 10)
        products[2] = Product(2, "Product 2", ProductType.gadget, 20)
        productService.javaClass.getDeclaredField("products").apply {
            isAccessible = true
            set(productService, products)
        }
        productService.javaClass.getDeclaredField("idCounter").apply {
            isAccessible = true
            set(productService, idCounter)
        }

        val result = productService.getProductsByType(ProductType.book)

        assertEquals(1, result.size)
        assertTrue(result.contains(products[1]))
        assertFalse(result.contains(products[2]))
    }

    @Test
    fun `test createProduct with valid details`() {
        val productDetails = ProductDetails("Product 1", ProductType.book, 10)

        val productId = productService.createProduct(productDetails)

        assertNotNull(productId)
        assertEquals(1, productId.id)
        val product = productService.getProductsByType(null).first()
        assertEquals(productDetails.name, product.name)
        assertEquals(productDetails.type, product.type)
        assertEquals(productDetails.inventory, product.inventory)
    }

    @Test
    fun `test createProduct with empty name throws exception`() {
        val productDetails = ProductDetails("", ProductType.book, 10)

        val exception = assertThrows(IllegalInputException::class.java) {
            productService.createProduct(productDetails)
        }
        assertEquals(400, exception.status)
        assertEquals("Product name cannot be empty", exception.message)
        assertEquals("/products", exception.path)
    }

    @Test
    fun `test createProduct with inventory out of bounds throws exception`() {
        val productDetailsLow = ProductDetails("Product 1", ProductType.book, 0)
        val productDetailsHigh = ProductDetails("Product 1", ProductType.book, 10000)

        val exceptionLow = assertThrows(IllegalInputException::class.java) {
            productService.createProduct(productDetailsLow)
        }
        assertEquals(400, exceptionLow.status)
        assertEquals("Inventory must be present and should be between 0 and 999", exceptionLow.message)
        assertEquals("/products", exceptionLow.path)

        val exceptionHigh = assertThrows(IllegalInputException::class.java) {
            productService.createProduct(productDetailsHigh)
        }
        assertEquals(400, exceptionHigh.status)
        assertEquals("Inventory must be present and should be between 0 and 999", exceptionHigh.message)
        assertEquals("/products", exceptionHigh.path)
    }
}
