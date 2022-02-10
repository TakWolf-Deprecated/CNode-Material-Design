package org.cnodejs.android.md.util

import androidx.lifecycle.MutableLiveData

fun <T : Any> MutableLiveData<T>.notifyDataChanged() {
    value = value
}

fun <T : Any> MutableLiveData<T>.postNotifyDataChanged() {
    postValue(value)
}
