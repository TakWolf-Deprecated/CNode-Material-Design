package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.ItemMessageBinding
import org.cnodejs.android.md.model.entity.Message
import org.cnodejs.android.md.model.entity.MessageType
import org.cnodejs.android.md.ui.listener.OnTopicClickListener
import org.cnodejs.android.md.ui.listener.OnUserClickListener
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.util.setSharedName
import org.cnodejs.android.md.util.timeSpanStringFromNow

class MessageListAdapter(private val layoutInflater: LayoutInflater, private val uniqueTag: String) : ListAdapter<Message, MessageListAdapter.ViewHolder>(MessageDiffItemCallback) {
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
            // TODO
        }

        fun bind(message: Message, isLast: Boolean) {
            val resources = itemView.resources
            binding.imgAuthor.loadAvatar(message.author.avatarUrlCompat)
            binding.imgAuthor.setSharedName(uniqueTag, "imgAuthor-${bindingAdapterPosition}")
            binding.tvAuthor.text = message.author.loginName
            binding.tvCreateTime.text = message.createAt.timeSpanStringFromNow(resources)
            binding.dot.isVisible = !message.hasRead
            binding.tvTopicTitle.text = resources.getString(R.string.topic_s, message.topic.title)
            binding.divider.isVisible = !isLast
            binding.navigationBar.isVisible = isLast

            if (message.type == MessageType.AT) {
                if (message.reply.id == null) {
                    binding.tvAction.setText(R.string.at_you_in_topic)
                    binding.tvReplyContent.isVisible = false
                } else {
                    binding.tvAction.setText(R.string.at_you_in_reply)
                    binding.tvReplyContent.isVisible = true
                    binding.tvReplyContent.text = message.reply.content // TODO
                }
            } else {
                binding.tvAction.setText(R.string.reply_your_topic)
                binding.tvReplyContent.isVisible = true
                binding.tvReplyContent.text = message.reply.content // TODO
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
