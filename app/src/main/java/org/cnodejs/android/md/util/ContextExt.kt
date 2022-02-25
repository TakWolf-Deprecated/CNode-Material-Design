package org.cnodejs.android.md.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.cnodejs.android.md.R

fun Context.openInBrowser(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW, uri)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        showToast(R.string.no_browser_installed)
    }
}

fun Context.openShare(title: String, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    startActivity(Intent.createChooser(intent, title))
}
