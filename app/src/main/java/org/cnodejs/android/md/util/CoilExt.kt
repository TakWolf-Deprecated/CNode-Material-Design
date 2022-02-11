package org.cnodejs.android.md.util

import android.widget.ImageView
import coil.load
import coil.request.Disposable
import org.cnodejs.android.md.R

fun ImageView.loadAvatar(uri: String?): Disposable {
    return load(uri) {
        placeholder(R.drawable.image_placeholder)
        error(R.drawable.image_placeholder)
    }
}
