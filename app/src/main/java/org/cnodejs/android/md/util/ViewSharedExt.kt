package org.cnodejs.android.md.util

import android.view.View

fun View.setSharedName(who: String, name: String) {
    transitionName = "${who}:${name}"
}
