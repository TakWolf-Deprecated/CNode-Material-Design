package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.ItemTopicBinding
import org.cnodejs.android.md.model.entity.TopicWithSummary
import org.cnodejs.android.md.ui.listener.OnTopicClickListener
import org.cnodejs.android.md.ui.listener.OnUserClickListener
import org.cnodejs.android.md.util.FormatUtils

class TopicListAdapter : ListAdapter<TopicWithSummary, TopicListAdapter.ViewHolder>(TopicDiffItemCallback) {
    var onTopicClickListener: OnTopicClickListener? = null
    var onUserClickListener: OnUserClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layoutContent.setOnClickListener {
                (bindingAdapter as? TopicListAdapter)?.let { adapter ->
                    adapter.onTopicClickListener?.let { listener ->
                        val topic = adapter.getItem(bindingAdapterPosition).topic
                        listener.onTopicClick(topic)
                    }
                }
            }

            binding.layoutAuthor.setOnClickListener {
                (bindingAdapter as? TopicListAdapter)?.let { adapter ->
                    adapter.onUserClickListener?.let { listener ->
                        val author = adapter.getItem(bindingAdapterPosition).topic.author
                        listener.onUserClick(author, binding.imgAuthor)
                    }
                }
            }
        }

        fun bind(topicWithSummary: TopicWithSummary) {
            val topic = topicWithSummary.topic
            val resources = itemView.resources
            binding.imgGood.visibility = if (topic.isGood) View.VISIBLE else View.GONE
            if (topic.isTop) {
                binding.tvTop.visibility = View.VISIBLE
                binding.tvTab.visibility = View.GONE
            } else {
                binding.tvTop.visibility = View.GONE
                binding.tvTab.visibility = View.VISIBLE
                binding.tvTab.setText(topic.tab.titleResId)
            }
            binding.tvReplyAndVisitCount.text = resources.getString(R.string.d_reply_d_visit, topic.replyCount, topic.visitCount)
            binding.tvReplyTime.text = resources.getString(R.string.reply_at_s, FormatUtils.getRelativeTimeSpanString(resources, topic.lastReplyAt))
            binding.tvTitle.text = topic.title
            binding.tvSummary.text = topicWithSummary.summary
            binding.imgAuthor.load(topic.author.getCompatAvatarUrl()) {
                placeholder(R.drawable.image_placeholder)
            }
            binding.imgAuthor.transitionName = "img_avatar_${bindingAdapterPosition}"
            binding.tvAuthor.text = topic.author.loginName
            binding.tvCreateTime.text = resources.getString(R.string.create_at_s, FormatUtils.getRelativeTimeSpanString(resources, topic.createAt))
        }
    }
}

private object TopicDiffItemCallback : DiffUtil.ItemCallback<TopicWithSummary>() {
    override fun areItemsTheSame(oldItem: TopicWithSummary, newItem: TopicWithSummary): Boolean {
        return oldItem.topic.id == newItem.topic.id
    }

    override fun areContentsTheSame(oldItem: TopicWithSummary, newItem: TopicWithSummary): Boolean {
        return oldItem.topic == newItem.topic
    }
}
