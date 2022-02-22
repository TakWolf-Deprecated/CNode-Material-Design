package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.ItemMessageBinding
import org.cnodejs.android.md.model.entity.MessageType
import org.cnodejs.android.md.model.entity.MessageWithSummary
import org.cnodejs.android.md.ui.listener.OnTopicClickListener
import org.cnodejs.android.md.ui.listener.OnUserClickListener
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.util.setSharedName
import org.cnodejs.android.md.util.timeSpanStringFromNow

class MessageListAdapter(private val layoutInflater: LayoutInflater, private val uniqueTag: String) : ListAdapter<MessageWithSummary, MessageListAdapter.ViewHolder>(MessageWithSummaryDiffItemCallback) {
    var onTopicClickListener: OnTopicClickListener? = null
    var onUserClickListener: OnUserClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, uniqueTag)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position == itemCount - 1)
    }

    class ViewHolder(
        private val binding: ItemMessageBinding,
        private val uniqueTag: String,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnItem.setOnClickListener {
                (bindingAdapter as? MessageListAdapter)?.let { adapter ->
                    adapter.onTopicClickListener?.let { listener ->
                        val message = adapter.getItem(bindingAdapterPosition).message
                        listener.onTopicClick(message.topic.id, message.reply.id)
                    }
                }
            }

            binding.imgAuthor.setOnClickListener {
                (bindingAdapter as? MessageListAdapter)?.let { adapter ->
                    adapter.onUserClickListener?.let { listener ->
                        val author = adapter.getItem(bindingAdapterPosition).message.author
                        listener.onUserClick(author, binding.imgAuthor)
                    }
                }
            }
        }

        fun bind(messageWithSummary: MessageWithSummary, isLast: Boolean) {
            val message = messageWithSummary.message
            val resources = itemView.resources
            binding.imgAuthor.loadAvatar(message.author.avatarUrlCompat)
            binding.imgAuthor.setSharedName(uniqueTag, "imgAuthor-${bindingAdapterPosition}")
            binding.tvAuthor.text = message.author.loginName
            binding.tvCreateTime.text = message.createAt.timeSpanStringFromNow(resources)
            binding.dot.isVisible = !message.hasRead
            binding.tvTopicTitle.text = resources.getString(R.string.topic_s, message.topic.title)
            binding.navigationBar.isVisible = isLast
            binding.divider.isVisible = !isLast

            if (message.type == MessageType.AT) {
                if (message.reply.id == null) {
                    binding.tvAction.setText(R.string.at_me_in_topic)
                    binding.tvReplyContent.isVisible = false
                } else {
                    binding.tvAction.setText(R.string.at_me_in_reply)
                    binding.tvReplyContent.isVisible = true
                    binding.tvReplyContent.text = messageWithSummary.replySummary
                }
            } else {
                binding.tvAction.setText(R.string.reply_my_topic)
                binding.tvReplyContent.isVisible = true
                binding.tvReplyContent.text = messageWithSummary.replySummary
            }
        }
    }
}

private object MessageWithSummaryDiffItemCallback : DiffUtil.ItemCallback<MessageWithSummary>() {
    override fun areItemsTheSame(oldItem: MessageWithSummary, newItem: MessageWithSummary): Boolean {
        return oldItem.message.id == newItem.message.id
    }

    override fun areContentsTheSame(oldItem: MessageWithSummary, newItem: MessageWithSummary): Boolean {
        return oldItem.message == newItem.message
    }
}
