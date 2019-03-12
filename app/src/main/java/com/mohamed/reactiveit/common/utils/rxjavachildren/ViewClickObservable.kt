package com.mohamed.reactiveit.common.utils.rxjavachildren

import android.util.Log
import android.view.View
import com.mohamed.reactiveit.common.utils.TAG
import com.mohamed.reactiveit.common.utils.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class ViewClickObservable(private val view: View) :
    Observable<Unit>() {
    override fun subscribeActual(observer: Observer<in Unit>) {
        if (!checkMainThread()) return
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnClickListener(listener)
    }

    private class Listener(
        private val view: View,
        private val observer: Observer<in Unit>
    ) : MainThreadDisposable(), View.OnClickListener {
        override fun onDispose() {
            view.setOnClickListener(null)
            Log.d(TAG, "disposed")
        }

        override fun onClick(v: View?) {
            if (!isDisposed) {
                try {
                    observer.onNext(Unit)
                } catch (e: Exception) {
                    observer.onError(e)
                    dispose()
                }
            }
        }

    }
}

