package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.ItemTopicHomeBinding
import org.cnodejs.android.md.model.entity.TopicWithSummary
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.util.setSharedName
import org.cnodejs.android.md.util.timeSpanStringFromNow

class TopicHomeListAdapter(private val layoutInflater: LayoutInflater, private val uniqueTag: String) : TopicListAdapter<TopicWithSummary, TopicHomeListAdapter.ViewHolder>(TopicWithSummaryDiffItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopicHomeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, uniqueTag)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemTopicHomeBinding,
        private val uniqueTag: String,
    ) : TopicListAdapter.ViewHolder(
        binding.root,
        binding.layoutContent,
        binding.layoutAuthor,
        binding.imgAuthor,
    ) {
        fun bind(topicWithSummary: TopicWithSummary) {
            val topic = topicWithSummary.topic
            val resources = itemView.resources
            binding.imgGood.isVisible = topic.isGood
            binding.tvTop.isVisible = topic.isTop
            binding.tvTab.isVisible = !topic.isTop
            binding.tvTab.setText(topic.tab.titleId)
            binding.tvReplyAndVisitCount.text = resources.getString(R.string.d_reply_d_visit, topic.replyCount, topic.visitCount)
            binding.tvReplyTime.text = resources.getString(R.string.reply_at_s, topic.lastReplyAt.timeSpanStringFromNow(resources))
            binding.tvTitle.text = topic.title
            binding.tvSummary.text = topicWithSummary.summary
            binding.imgAuthor.loadAvatar(topic.author.avatarUrlCompat)
            binding.imgAuthor.setSharedName(uniqueTag, "imgAuthor-${bindingAdapterPosition}")
            binding.tvAuthor.text = topic.author.loginName
            binding.tvCreateTime.text = resources.getString(R.string.create_at_s, topic.createAt.timeSpanStringFromNow(resources))
        }
    }
}

private object TopicWithSummaryDiffItemCallback : DiffUtil.ItemCallback<TopicWithSummary>() {
    override fun areItemsTheSame(oldItem: TopicWithSummary, newItem: TopicWithSummary): Boolean {
        return oldItem.topic.id == newItem.topic.id
    }

    override fun areContentsTheSame(oldItem: TopicWithSummary, newItem: TopicWithSummary): Boolean {
        return oldItem.topic == newItem.topic
    }
}
