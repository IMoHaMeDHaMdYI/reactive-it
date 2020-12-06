package com.mohamed.reactiveit.authentication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamed.reactiveit.common.db.UserDao
import com.mohamed.reactiveit.common.db.repository.UserRepository
import com.mohamed.reactiveit.common.models.Result
import com.mohamed.reactiveit.common.models.User
import com.mohamed.reactiveit.common.utils.TAG
import com.mohamed.reactiveit.common.utils.whenNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class AuthenticationViewModel(userDao: UserDao) : ViewModel() {
    val stateSubject = BehaviorSubject.create<AuthenticationViewState>()
    val userNameSubject = BehaviorSubject.create<String>()
    val userPasswordSubject = BehaviorSubject.create<String>()
    val signInSubject = PublishSubject.create<Result<Boolean>>()
    val userSubject = BehaviorSubject.create<User?>()

    init {
        stateSubject.onNext(AuthenticationViewState(loading = false, done = false, error = false))
    }

    private val repo = UserRepository(userDao)
    private val compositeDisposable = CompositeDisposable()

    fun signIn(userName: String, password: String) {
        // What is this shit ?
        compositeDisposable.clear()
        compositeDisposable.add(repo.getUser(userName).subscribeOn(Schedulers.io())
            .subscribe({
                it?.let { result ->
                    if (result.isSuccess()) {
                        if (result.data.password == password) {
                            signInSubject.onNext(Result(null, true))
                            userSubject.onNext(it.data)
                        } else {
                            signInSubject.onNext(Result("Wrong Password", false))
                        }
                    } else {
                        signInSubject.onNext(Result("Error Occurred!", false))
                    }
                }
                it.whenNull {
                    signInSubject.onNext(Result("Error Occurred! result is null !", false))
                }
            }, {
                signInSubject.onNext(Result("Error Occurred! ${it.localizedMessage}", false))
            }, {
                signInSubject.onNext(Result("No User Registered with this name", false))
            })
        )
    }

    fun signUp(userName: String, password: String) {
        compositeDisposable.clear()
        compositeDisposable.add(repo.insertUser(User(name = userName, password = password))
            .subscribeOn(Schedulers.io())
            .subscribe({ it ->
                repo.getUser(it).subscribeOn(Schedulers.io())
                    .subscribe { user ->
                        userSubject.onNext(user)
                        signInSubject.onNext(Result(null, true))
                    }
            }, {
                Log.d(TAG, "Error : $it")
                signInSubject.onNext(Result("Error Occurred! ${it.localizedMessage}", false))
            }) {
                Log.d(TAG, "back again")
                signInSubject.onNext(Result("Nothing Happened", false))
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        Log.d(TAG, "All disposed")
    }

    class Factory(private val userDao: UserDao) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AuthenticationViewModel(userDao) as T
        }
    }
}

data class AuthenticationViewState(
    val loading: Boolean,
    val done: Boolean,
    val error: Boolean,
    val source: String = "default"
) {
    companion object {
        fun reset(): AuthenticationViewState {
            return AuthenticationViewState(false, false, false)
        }
    }
}
