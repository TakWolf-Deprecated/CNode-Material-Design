package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.ItemTopicForHomeBinding
import org.cnodejs.android.md.model.entity.TopicForHome
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.util.timeSpanStringFromNow

class TopicForHomeListAdapter : TopicListAdapter<TopicForHome, TopicForHomeListAdapter.ViewHolder>(TopicForHomeDiffItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTopicForHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemTopicForHomeBinding) : TopicListAdapter.ViewHolder(
        binding.root,
        binding.layoutContent,
        binding.layoutAuthor,
        binding.imgAuthor,
    ) {
        fun bind(topicForHome: TopicForHome) {
            val topic = topicForHome.topic
            val resources = itemView.resources
            binding.imgGood.isVisible = topic.isGood
            binding.tvTop.isVisible = topic.isTop
            binding.tvTab.isVisible = !topic.isTop
            binding.tvTab.setText(topic.tab.titleId)
            binding.tvReplyAndVisitCount.text = resources.getString(R.string.d_reply_d_visit, topic.replyCount, topic.visitCount)
            binding.tvReplyTime.text = resources.getString(R.string.reply_at_s, topic.lastReplyAt.timeSpanStringFromNow(resources))
            binding.tvTitle.text = topic.title
            binding.tvSummary.text = topicForHome.summary
            binding.imgAuthor.loadAvatar(topic.author.avatarUrlCompat)
            binding.imgAuthor.transitionName = "imgAvatar@${bindingAdapterPosition}"
            binding.tvAuthor.text = topic.author.loginName
            binding.tvCreateTime.text = resources.getString(R.string.create_at_s, topic.createAt.timeSpanStringFromNow(resources))
        }
    }
}

private object TopicForHomeDiffItemCallback : DiffUtil.ItemCallback<TopicForHome>() {
    override fun areItemsTheSame(oldItem: TopicForHome, newItem: TopicForHome): Boolean {
        return oldItem.topic.id == newItem.topic.id
    }

    override fun areContentsTheSame(oldItem: TopicForHome, newItem: TopicForHome): Boolean {
        return oldItem.topic == newItem.topic
    }
}
