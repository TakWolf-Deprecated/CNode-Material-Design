package org.cnodejs.android.md.ui.listener

import android.widget.ImageView
import org.cnodejs.android.md.model.entity.UrlString
import org.cnodejs.android.md.util.Navigator

interface OnImageClickListener {
    fun onImageClick(images: List<UrlString>, index: Int)

    fun onImageClick(images: List<UrlString>, index: Int, views: List<ImageView>)
}

class ImagePreviewNavigateListener(private val navigator: Navigator) : OnImageClickListener {
    override fun onImageClick(images: List<UrlString>, index: Int) {
        // TODO
        println("1")
    }

    override fun onImageClick(images: List<UrlString>, index: Int, views: List<ImageView>) {
        // TODO
        println("2")
    }
}
