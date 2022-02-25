package org.cnodejs.android.md.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

private var currentToast: WeakReference<Toast>? = null

fun Context.showToast(message: String) {
    currentToast?.get()?.cancel()
    val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).apply { show() }
    currentToast = WeakReference(toast)
}

fun Context.showToast(@StringRes resId: Int) {
    showToast(getString(resId))
}

fun Fragment.showToast(message: String) {
    context?.showToast(message)
}

fun Fragment.showToast(@StringRes resId: Int) {
    context?.showToast(resId)
}
