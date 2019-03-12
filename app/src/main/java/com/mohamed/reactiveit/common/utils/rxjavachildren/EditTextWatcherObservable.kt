package com.mohamed.reactiveit.common.utils.rxjavachildren

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.mohamed.reactiveit.common.utils.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class EditTextWatcherObservable(private val editText: EditText) : Observable<String>() {
    override fun subscribeActual(observer: Observer<in String>) {
        if (!checkMainThread()) return
        val listener = Listener(editText, observer)
        observer.onSubscribe(listener)
        editText.addTextChangedListener(listener)
    }

    private class Listener(
        private val editText: EditText,
        private val observer: Observer<in String>
    ) : MainThreadDisposable(), TextWatcher {
        override fun onDispose() {
            editText.removeTextChangedListener(this)
        }

        override fun afterTextChanged(s: Editable?) {
            if (!isDisposed) {
                try {
                    s?.let { observer.onNext(it.toString()) }
                } catch (e: Exception) {
                    observer.onError(e)
                    dispose()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }
}