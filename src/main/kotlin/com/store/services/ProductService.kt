package com.store.services

import com.store.entities.Product
import com.store.entities.ProductDetails
import com.store.entities.ProductId
import com.store.entities.ProductType
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger
import com.store.exceptions.IllegalInputException
import com.store.services.interfaces.IProductService
import java.util.concurrent.ConcurrentHashMap

@Service
class ProductService: IProductService {

    private val products = ConcurrentHashMap<Int, Product>()
    private val idCounter = AtomicInteger()

    override fun getProductsByType(type: ProductType?): List<Product> {
        return if (type == null) {
            products.values.toList()
        } else {
            products.values.filter { it.type == type }
        }
    }

    override fun createProduct(productDetails: ProductDetails): ProductId {
        val id = idCounter.incrementAndGet()
        val product = Product(id, productDetails.name, productDetails.type, productDetails.inventory, productDetails.cost)
        products[id] = product
        return ProductId(id)
    }
}