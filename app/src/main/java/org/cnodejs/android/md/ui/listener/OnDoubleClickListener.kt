package org.cnodejs.android.md.ui.listener

import android.view.View

abstract class OnDoubleClickListener(private val timeout: Long = 400) : View.OnClickListener {
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
