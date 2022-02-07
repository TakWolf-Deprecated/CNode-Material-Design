package org.cnodejs.android.md.util

import android.view.View

abstract class OnDoubleClickListener(private val timeout: Long) : View.OnClickListener {
    private var lastClickTime = 0L

    final override fun onClick(v: View) {
        val nowClickTime = System.currentTimeMillis()
        if (nowClickTime - lastClickTime > timeout) {
            lastClickTime = nowClickTime
        } else {
            onDoubleClick(v)
        }
    }

    abstract fun onDoubleClick(v: View)
}
