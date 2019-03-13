package com.mohamed.reactiveit.authentication.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.authentication.ui.viewmodels.AuthenticationViewModel
import com.mohamed.reactiveit.authentication.ui.viewmodels.AuthenticationViewState
import com.mohamed.reactiveit.common.utils.TAG
import com.mohamed.reactiveit.common.utils.afterTextChanged
import com.mohamed.reactiveit.common.utils.click
import com.mohamed.reactiveit.common.utils.startActivityFinishThis
import com.mohamed.reactiveit.home.ui.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthenticationViewModel
    private val uiCompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        viewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
        val ignored = viewModel.stateSubject.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.done) {
                    startActivityFinishThis(HomeActivity::class.java)
                    viewModel.stateSubject.onNext(AuthenticationViewState.reset())
                    return@subscribe
                }
                when (it.loading) {
                    true -> {
                        btnSignIn.isEnabled = false
                        btnSignUp.isEnabled = false
                        pb.visibility = View.VISIBLE
                        btnCancel.visibility = View.VISIBLE
                    }
                    false -> {
                        btnSignUp.isEnabled = true
                        btnSignIn.isEnabled = true
                        pb.visibility = View.INVISIBLE
                        btnCancel.visibility = View.INVISIBLE
                    }
                }
                when (it.error) {
                    true -> txtError.visibility = View.VISIBLE
                    false -> txtError.visibility = View.INVISIBLE
                }
            }
        val ignored2 = viewModel.signInSubject.subscribe {
            when (it.isSuccess()) {
                true -> when (it.data) {
                    true -> viewModel.stateSubject.onNext(
                        viewModel.stateSubject.value!!.copy(
                            done = true
                        )
                    )
                    false -> viewModel.stateSubject.onNext(
                        viewModel.stateSubject.value!!.copy(
                            loading = false,
                            error = true
                        )
                    )
                }
                false -> viewModel.stateSubject.onNext(
                    viewModel.stateSubject.value!!.copy(
                        loading = false,
                        error = true
                    )
                )
            }
        }
        uiCompositeDisposable.add(ignored)
        uiCompositeDisposable.add(ignored2)
        uiCompositeDisposable.add(
            editTextPassword.afterTextChanged().subscribe({
                viewModel.userPasswordSubject.onNext(it)
            }) {
                Log.d(TAG, "Password Error : ${it.localizedMessage}")
            }
        )
        uiCompositeDisposable.add(
            editTextUserName.afterTextChanged().subscribe({
                viewModel.userNameSubject.onNext(it)
            }) {
                Log.d(TAG, "Throwable : $it")
            }
        )

        uiCompositeDisposable.add(btnSignIn.click().subscribe({
            viewModel.stateSubject.onNext(
                viewModel.stateSubject.value!!.copy(
                    loading = true,
                    error = false,
                    source = "Click"
                )
            )
            viewModel.signIn(
                viewModel.userNameSubject.value ?: ""
                , viewModel.userPasswordSubject.value ?: ""
            )
        }) {
            Log.d(TAG, it.localizedMessage)
        })


    }

    override fun onStop() {
        super.onStop()
        uiCompositeDisposable.clear()
    }
}
