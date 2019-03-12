package com.mohamed.reactiveit.authentication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mohamed.reactiveit.authentication.repository.AuthenticationRepository
import com.mohamed.reactiveit.common.models.Result
import com.mohamed.reactiveit.common.utils.TAG
import com.mohamed.reactiveit.common.utils.whenNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class AuthenticationViewModel : ViewModel() {
    val stateSubject = BehaviorSubject.create<AuthenticationViewState>()
    val userNameSubject = BehaviorSubject.create<String>()
    val userPasswordSubject = BehaviorSubject.create<String>()
    val signInSubject = PublishSubject.create<Result<Boolean>>()

    init {
        stateSubject.onNext(AuthenticationViewState(loading = false, done = false, error = false))
    }

    private val repo = AuthenticationRepository()
    private val compositeDisposable = CompositeDisposable()

    fun signIn(userName: String, password: String) {
        compositeDisposable.clear()
        compositeDisposable.add(repo.getUser(userName).subscribeOn(Schedulers.io())
            .subscribe({
                it?.let { result ->
                    if (result.isSuccess()) {
                        if (result.data.password == password) {
                            signInSubject.onNext(Result(null, true))
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
            }) {
                signInSubject.onNext(Result("Error Occurred! ${it.localizedMessage}", false))
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        Log.d(TAG,"All disposed")
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
