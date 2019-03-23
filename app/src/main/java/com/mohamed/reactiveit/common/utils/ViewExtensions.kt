package com.mohamed.reactiveit.common.utils

import android.view.View
import android.widget.EditText
import com.mohamed.reactiveit.common.utils.rxjavachildren.EditTextWatcherObservable
import com.mohamed.reactiveit.common.utils.rxjavachildren.ViewClickObservable
import io.reactivex.Observable

fun View.click(): Observable<Unit> {
    return ViewClickObservable(this)
}

fun View.onPreDraw(action: () -> Unit) {
    viewTreeObserver.addOnPreDrawListener {
        action()
        return@addOnPreDrawListener true
    }
}

fun EditText.afterTextChanged(): Observable<String> {
    return EditTextWatcherObservable(this)
}