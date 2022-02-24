package org.cnodejs.android.md.util

import android.view.View
import android.widget.TextView

fun View.setSharedName(who: String, name: String) {
    transitionName = "${who}:${name}"
}

fun TextView.fixTextIsSelectable() {
    setTextIsSelectable(false)
    post { setTextIsSelectable(true) }
}
