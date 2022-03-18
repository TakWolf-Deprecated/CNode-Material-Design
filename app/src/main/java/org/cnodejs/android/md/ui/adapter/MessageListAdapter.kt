package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.ItemMessageBinding
import org.cnodejs.android.md.model.entity.Message
import org.cnodejs.android.md.model.entity.MessageType
import org.cnodejs.android.md.ui.listener.OnImageClickListener
import org.cnodejs.android.md.ui.listener.OnTopicClickListener
import org.cnodejs.android.md.ui.listener.OnUserClickListener
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.util.loadThumb
import org.cnodejs.android.md.util.setSharedName
import org.cnodejs.android.md.util.timeSpanStringFromNow

class MessageListAdapter(
    private val layoutInflater: LayoutInflater,
    private val who: String,
) : ListAdapter<Message, MessageListAdapter.ViewHolder>(MessageDiffItemCallback) {
    var onMessageReadListener: ((messageId: String) -> Unit)? = null
    var onTopicClickListener: OnTopicClickListener? = null
    var onUserClickListener: OnUserClickListener? = null
    var onImageClickListener: OnImageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, who)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position == itemCount - 1)
    }

    class ViewHolder(
        private val binding: ItemMessageBinding,
        private val who: String,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val resources = itemView.resources
        private val imgThumbs = listOf(binding.imgThumb1, binding.imgThumb2, binding.imgThumb3)

        init {
            binding.btnItem.setOnClickListener {
                (bindingAdapter as? MessageListAdapter)?.let { adapter ->
                    val message = adapter.getItem(bindingAdapterPosition)
                    if (!message.hasRead) {
                        adapter.onMessageReadListener?.invoke(message.id)
                    }
                    adapter.onTopicClickListener?.onTopicClick(message.topic.id, message.reply.id)
                }
            }
            binding.imgAuthor.setOnClickListener {
                (bindingAdapter as? MessageListAdapter)?.let { adapter ->
                    adapter.onUserClickListener?.let { listener ->
                        val author = adapter.getItem(bindingAdapterPosition).author
                        listener.onUserClick(author, binding.imgAuthor)
                    }
                }
            }
            binding.imgThumb0.setOnClickListener {
                (bindingAdapter as? MessageListAdapter)?.let { adapter ->
                    adapter.onImageClickListener?.let { listener ->
                        val message = adapter.getItem(bindingAdapterPosition)
                        message.reply.content?.let { content ->
                            listener.onImageClick(content.images, 0, listOf(binding.imgThumb0))
                        }
                    }
                }
            }
            View.OnClickListener { view ->
                (bindingAdapter as? MessageListAdapter)?.let { adapter ->
                    adapter.onImageClickListener?.let { listener ->
                        val message = adapter.getItem(bindingAdapterPosition)
                        message.reply.content?.let { content ->
                            val index = imgThumbs.indexOf(view)
                            listener.onImageClick(content.images, index, imgThumbs)
                        }
                    }
                }
            }.apply {
                for (imgThumb in imgThumbs) {
                    imgThumb.setOnClickListener(this)
                }
            }
        }

        fun bind(message: Message, isLast: Boolean) {
            binding.imgAuthor.loadAvatar(message.author.avatarUrl)
            binding.imgAuthor.setSharedName(who, "imgAuthor-${bindingAdapterPosition}")
            binding.tvAuthor.text = message.author.loginName
            binding.tvCreateTime.text = message.createAt.timeSpanStringFromNow(resources)
            binding.dot.isVisible = !message.hasRead
            binding.tvTopicTitle.text = message.topic.title
            binding.navigationBar.isVisible = isLast
            binding.divider.isVisible = !isLast

            for (index in imgThumbs.indices) {
                imgThumbs[index].setSharedName(who, "imgThumb-${bindingAdapterPosition}-${index}")
            }

            if (message.type == MessageType.AT) {
                if (message.reply.id == null) {
                    binding.tvAction.setText(R.string.at_me_in_topic)
                    binding.tvReplySummary.isVisible = false
                } else {
                    binding.tvAction.setText(R.string.at_me_in_reply)
                    binding.tvReplySummary.isVisible = message.reply.content != null
                    binding.tvReplySummary.text = message.reply.content?.summary
                }
            } else {
                binding.tvAction.setText(R.string.reply_my_topic)
                binding.tvReplySummary.isVisible = message.reply.content != null
                binding.tvReplySummary.text = message.reply.content?.summary
            }

            val images = message.reply.content?.images ?: emptyList()
            if (images.isEmpty()) {
                binding.layoutThumb.isVisible = false
            } else {
                binding.layoutThumb.isVisible = true
                if (images.size == 1) {
                    binding.imgThumb0.isVisible = true
                    binding.imgThumb0.loadThumb(images[0])
                    imgThumbs.forEach { it.isVisible = false }
                } else {
                    binding.imgThumb0.isVisible = false
                    for (i in imgThumbs.indices) {
                        val imgThumb = imgThumbs[i]
                        if (i < images.size) {
                            imgThumb.visibility = View.VISIBLE
                            imgThumb.loadThumb(images[i])
                        } else {
                            imgThumb.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }
}

private object MessageDiffItemCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}
