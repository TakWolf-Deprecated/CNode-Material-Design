package org.cnodejs.android.md.util

import android.widget.ImageView
import coil.load
import coil.request.Disposable
import org.cnodejs.android.md.R

fun ImageView.loadGracefully(uri: String?): Disposable {
    var repaired = uri ?: ""
    if (repaired.startsWith("//")) {
        repaired = "https:${repaired}"
    }
    return load(repaired) {
        placeholder(R.drawable.image_placeholder)
        error(R.drawable.image_placeholder)
    }
}
