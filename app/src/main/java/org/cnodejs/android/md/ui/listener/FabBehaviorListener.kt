package org.cnodejs.android.md.ui.listener

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val SCROLL_THRESHOLD = 4

fun FloatingActionButton.listenToRecyclerView(recyclerView: RecyclerView) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > SCROLL_THRESHOLD) {
                hide()
            } else if (dy < -SCROLL_THRESHOLD) {
                show()
            }
        }
    })
}
