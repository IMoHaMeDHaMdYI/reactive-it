package com.mohamed.reactiveit.common.db.repository

import com.mohamed.reactiveit.common.db.UserDao
import com.mohamed.reactiveit.common.models.Result
import com.mohamed.reactiveit.common.models.User
import io.reactivex.Maybe

class UserRepository(private val userDao: UserDao) {
    fun getUser(id: Long): Maybe<User> {
        return userDao.getUser(id)
    }

    fun getUser(name: String): Maybe<Result<User>> {
        return userDao.getUser(name)
            .map { Result(null, it) }
    }

    fun insertUser(user: User): Maybe<Long> {
        return userDao.addUser(user)
    }

    fun deleteUser(user: User): Maybe<Int> {
        return Maybe.fromCallable { userDao.deleteUser(user) }
    }

    fun updateUser(user: User): Maybe<Int> {
        return Maybe.fromCallable { userDao.updateUser(user) }
    }

    fun searchForUser(name: String): Maybe<Array<User>> {
        return Maybe.fromCallable { userDao.searchUserByName(name) }
    }
}
