package com.store.services

import com.store.entities.Product
import com.store.entities.ProductDetails
import com.store.entities.ProductType
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
        products[1] = Product(1, "Product 1", ProductType.book, 10, 10)
        products[2] = Product(2, "Product 2", ProductType.gadget, 20, 20)
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
        products[1] = Product(1, "Product 1", ProductType.book, 10, 10)
        products[2] = Product(2, "Product 2", ProductType.gadget, 20, 20)
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
        val productDetails = ProductDetails("Product 1", ProductType.book, 10, 10)

        val productId = productService.createProduct(productDetails)

        assertNotNull(productId)
        assertEquals(1, productId.id)
        val product = productService.getProductsByType(null).first()
        assertEquals(productDetails.name, product.name)
        assertEquals(productDetails.type, product.type)
        assertEquals(productDetails.inventory, product.inventory)
    }
}
