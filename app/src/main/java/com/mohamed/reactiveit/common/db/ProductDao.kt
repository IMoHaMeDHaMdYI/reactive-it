package com.mohamed.reactiveit.common.db

import androidx.room.*
import com.mohamed.reactiveit.common.models.Product
import io.reactivex.Maybe

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(product: Product): Maybe<Long>

    @Query("SELECT * FROM product WHERE id = :id")
    fun getProduct(id: Long): Maybe<Product>

    @Query("SELECT * FROM product WHERE user_id = :uid LIMIT :limit OFFSET :offset")
    fun getUserProduct(uid: Long, limit: Int = 10, offset: Int = 0): Array<Product>

    @Query("SELECT * FROM product WHERE name LIKE :query LIMIT :limit OFFSET :offset")
    fun searchProduct(query: String, limit: Int = 10, offset: Int = 0): Array<Product>

    @Delete
    fun deleteProduct(product: Product): Int

    @Query("DELETE FROM product WHERE id = :id")
    fun deleteProduct(id: Int): Int
}