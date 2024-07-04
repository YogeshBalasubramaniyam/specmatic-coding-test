package com.store.services.interfaces

import com.store.entities.Product
import com.store.entities.ProductDetails
import com.store.entities.ProductId
import com.store.entities.ProductType

interface IProductService {
    fun getProductsByType(type: ProductType?): List<Product>
    fun createProduct(productDetails: ProductDetails?): ProductId
}