package org.cnodejs.android.md.util

import android.view.View

fun View.setSharedName(uniqueTag: String, name: String) {
    transitionName = "${uniqueTag}:${name}"
}
