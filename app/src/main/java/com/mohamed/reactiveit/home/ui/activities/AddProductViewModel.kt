package com.mohamed.reactiveit.home.ui.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamed.reactiveit.common.db.repository.ProductRepository
import com.mohamed.reactiveit.common.models.Product
import com.mohamed.reactiveit.common.utils.TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class AddProductViewModel(private val productRepo: ProductRepository) : ViewModel() {


    val productNameSubject = BehaviorSubject.create<String>()
    val productUrlSubject = BehaviorSubject.create<String>()
    val stateSubject = BehaviorSubject.create<SubmitState>()
    private val compositeDisposable = CompositeDisposable()

    init {
        stateSubject.onNext(SubmitState(done = SubmitState.STATE_START, success = false))
    }

    fun insertProduct(product: Product) {
        compositeDisposable.add(
            productRepo.insertProduct(product)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    stateSubject.onNext(SubmitState(SubmitState.STATE_DONE, true))
                }, {
                    stateSubject.onNext(SubmitState(SubmitState.STATE_DONE, false))
                    Log.d(TAG, "Error $it")
                }, {
                    stateSubject.onNext(SubmitState(SubmitState.STATE_DONE, false))
                    Log.d(TAG, "No Answer!")
                })
        )
    }

    class Factory(private val productRepo: ProductRepository) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddProductViewModel(productRepo) as T
        }
    }

}

class SubmitState(val done: Int, val success: Boolean){
    companion object {
        val STATE_START = 0
        val STATE_LOADING = 1
        val STATE_DONE = 2
    }
}
