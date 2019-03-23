package com.mohamed.reactiveit.common.db.repository

import com.mohamed.reactiveit.common.db.ProductDao
import com.mohamed.reactiveit.common.models.Product
import io.reactivex.Maybe

class ProductRepository(private val productDao: ProductDao) {
    fun insertProduct(product: Product): Maybe<Long> {
        return productDao.insertProduct(product)
    }

    fun getProduct(id: Long): Maybe<Product> {
        return productDao.getProduct(id)
    }

    fun getUserProduct(uid: Long, limit: Int = 10, offset: Int = 0): Maybe<Array<Product>> {
        return Maybe.fromCallable { productDao.getUserProduct(uid, limit, offset) }
    }

    fun searchProduct(query: String, limit: Int = 10, offset: Int = 0): Maybe<Array<Product>> {
        return Maybe.fromCallable { productDao.searchProduct(query, limit, offset) }
    }

    fun deleteProduct(product: Product): Maybe<Int> {
        return Maybe.fromCallable { productDao.deleteProduct(product) }
    }

    fun deleteProduct(id: Int): Maybe<Int> {
        return Maybe.fromCallable { productDao.deleteProduct(id) }
    }
}