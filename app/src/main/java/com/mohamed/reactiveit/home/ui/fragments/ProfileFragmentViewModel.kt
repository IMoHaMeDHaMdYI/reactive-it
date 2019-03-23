package com.mohamed.reactiveit.home.ui.fragments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamed.reactiveit.common.baseadapter.Displayable
import com.mohamed.reactiveit.common.db.repository.ProductRepository
import com.mohamed.reactiveit.common.models.Product
import com.mohamed.reactiveit.common.utils.TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ProfileFragmentViewModel(private val productRepository: ProductRepository) : ViewModel() {

    // Create Subject to get and observe
    val comingProductListSubject = PublishSubject.create<Array<Product>>()
    val deletedProductSubject = PublishSubject.create<Int>()
    val dataList = ArrayList<Displayable>()
    private val compositeDisposable = CompositeDisposable()
    //******DISPOSABLES*******///
    private var productDisposable: Disposable? = null
    private var deleteDisposable: Disposable? = null
    //***********************///

    fun getProducts(uid: Long, offset: Int = dataList.size) {
        checkDisposable(productDisposable)
        productDisposable =
                productRepository.getUserProduct(uid = uid, offset = offset)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        it.forEach {
                            Log.d(TAG, "Found something $it")
                        }
                        comingProductListSubject.onNext(it)
                        Log.d(TAG, "onNextTriggered")
                    }, {
                        Log.d(TAG, "Error $it")
                        comingProductListSubject.onNext(arrayOf())
                    }) {
                        Log.d(TAG, "Didn't Find anything")
                        comingProductListSubject.onNext(arrayOf())
                    }?.apply {
                        compositeDisposable.add(this)
                    }
    }

    fun deleteProduct(product: Product, pos: Int) {
        checkDisposable(deleteDisposable)
        deleteDisposable =
                productRepository.deleteProduct(product)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        // pos is the position of the deleted item in the dataList
                        deletedProductSubject.onNext(pos)
                        deleteDisposable?.dispose()
                    }, {
                        deletedProductSubject.onNext(-1)
                        deleteDisposable?.dispose()
                    }) {
                        deletedProductSubject.onNext(-1)
                        deleteDisposable?.dispose()
                    }.apply { compositeDisposable.add(this) }
    }

    fun deleteProduct(id: Int, pos: Int) {
        checkDisposable(deleteDisposable)
        deleteDisposable =
                productRepository.deleteProduct(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        // pos is the position of the deleted item in the dataList
                        deletedProductSubject.onNext(pos)
                    }, {
                        deletedProductSubject.onNext(-1)
                    }) {
                        deletedProductSubject.onNext(-1)
                    }.apply { compositeDisposable.add(this) }
    }

    private fun checkDisposable(disposable: Disposable?) {
        disposable?.apply {
            if (!isDisposed) {
                compositeDisposable.delete(this)
                dispose()
            }
        }
    }

    class Factory(private val productRepository: ProductRepository) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ProfileFragmentViewModel(productRepository) as T
        }
    }
}

//TODO See the Flowable.scan