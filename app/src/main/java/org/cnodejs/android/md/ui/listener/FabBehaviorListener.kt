package org.cnodejs.android.md.ui.listener

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.cnodejs.android.md.ui.widget.WebView

private const val SCROLL_THRESHOLD = 4

fun FloatingActionButton.listenToRecyclerView(recyclerView: RecyclerView, isShowOnBottom: Boolean = false) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (isShowOnBottom && !recyclerView.canScrollVertically(1)) {
                show()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > SCROLL_THRESHOLD) {
                hide()
            } else if (dy < -SCROLL_THRESHOLD) {
                show()
            }
        }
    })
}

fun FloatingActionButton.listenToWebView(webView: WebView, isShowOnBottom: Boolean = false) {
    webView.addOnScrollChangedListener { _, t, _, oldt ->
        if (isShowOnBottom && !webView.canScrollVertically(1)) {
            show()
        } else {
            val dy = t - oldt
            if (dy > SCROLL_THRESHOLD) {
                hide()
            } else if (dy < -SCROLL_THRESHOLD) {
                show()
            }
        }
    }
}
