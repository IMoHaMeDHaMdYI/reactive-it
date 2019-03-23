package com.mohamed.reactiveit.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mohamed.reactiveit.common.models.Product
import com.mohamed.reactiveit.common.models.User
import com.mohamed.reactiveit.common.utils.SingletonHolder

@Database(entities = [User::class, Product::class], version = 2, exportSchema = false)
abstract class ECommerceDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object : SingletonHolder<ECommerceDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            ECommerceDatabase::class.java,
            "ecommerce"
        ).build()
    })
}