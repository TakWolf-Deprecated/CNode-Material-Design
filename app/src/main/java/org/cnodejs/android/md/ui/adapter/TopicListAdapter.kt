package org.cnodejs.android.md.ui.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.cnodejs.android.md.model.entity.ITopic
import org.cnodejs.android.md.ui.listener.OnTopicClickListener
import org.cnodejs.android.md.ui.listener.OnUserClickListener

abstract class TopicListAdapter<Topic : ITopic, VH : TopicListAdapter.ViewHolder>(diffCallback: DiffUtil.ItemCallback<Topic>) : ListAdapter<Topic, VH>(diffCallback) {
    var onTopicClickListener: OnTopicClickListener? = null
    var onUserClickListener: OnUserClickListener? = null

    abstract class ViewHolder(
        itemView: View,
        btnTopicContent: View,
        btnAuthor: View,
        imgAuthor: ImageView,
    ) : RecyclerView.ViewHolder(itemView) {
        init {
            btnTopicContent.setOnClickListener {
                (bindingAdapter as? TopicListAdapter<*, *>)?.let { adapter ->
                    adapter.onTopicClickListener?.let { listener ->
                        val topic = adapter.getItem(bindingAdapterPosition)
                        listener.onTopicClick(topic.id)
                    }
                }
            }

            btnAuthor.setOnClickListener {
                (bindingAdapter as? TopicListAdapter<*, *>)?.let { adapter ->
                    adapter.onUserClickListener?.let { listener ->
                        val author = adapter.getItem(bindingAdapterPosition).author
                        listener.onUserClick(author, imgAuthor)
                    }
                }
            }
        }
    }
}
