package com.mohamed.reactiveit.common.db

import androidx.room.*
import com.mohamed.reactiveit.common.models.User
import io.reactivex.Maybe

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User): Maybe<Long>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Long): Maybe<User>

    @Query("SELECT * FROM user WHERE name = :name")
    fun getUser(name:String): Maybe<User>

    @Query("SELECT * FROM user WHERE name LIKE :name LIMIT :limit OFFSET :offset")
    fun searchUserByName(name: String, offset: Int = 0, limit: Int = 10): Array<User>

    @Query("SELECT * FROM user LIMIT :limit OFFSET :offset")
    fun getUsers(offset: Int = 0, limit: Int = 10) : Array<User>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User): Int

    @Delete
    fun deleteUser(user: User): Int
}