package com.mohamed.reactiveit.common.utils

import android.app.Activity
import android.content.Intent
import android.os.Looper

fun checkMainThread() = Looper.myLooper() == Looper.getMainLooper()

// To Avoid Creating new TAG variable in each Class, just use this extension value
val Any.TAG
    get() = this::class.java.simpleName!!

fun <T> T.whenNull(`do`: () -> Unit) {
    if (this == null) {
        `do`()
    }
}

fun <T> Activity.startActivityFinishThis(target: Class<T>) {
    val intent = Intent(this, target)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}