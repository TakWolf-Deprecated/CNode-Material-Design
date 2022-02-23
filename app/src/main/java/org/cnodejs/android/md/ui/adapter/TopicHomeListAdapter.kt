package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.ItemTopicHomeBinding
import org.cnodejs.android.md.model.entity.TopicWithSummary
import org.cnodejs.android.md.util.*

class TopicHomeListAdapter(private val layoutInflater: LayoutInflater, private val who: String) : TopicListAdapter<TopicWithSummary, TopicHomeListAdapter.ViewHolder>(TopicWithSummaryDiffItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopicHomeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, who)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemTopicHomeBinding,
        private val who: String,
    ) : TopicListAdapter.ViewHolder(
        binding.root,
        binding.layoutContent,
        binding.layoutAuthor,
        binding.imgAuthor,
    ) {
        private val imgThumbs = listOf(binding.imgThumb1, binding.imgThumb2, binding.imgThumb3)

        fun bind(topicWithSummary: TopicWithSummary) {
            val topic = topicWithSummary.topic
            val summary = topicWithSummary.summary
            val resources = itemView.resources

            binding.imgGood.isVisible = topic.isGood
            binding.tvTop.isVisible = topic.isTop
            binding.tvTab.isVisible = !topic.isTop
            binding.tvTab.setText(topic.tab.titleId)
            binding.tvReplyAndVisitCount.text = resources.getString(R.string.d_reply_d_visit, topic.replyCount, topic.visitCount)
            binding.tvReplyTime.text = resources.getString(R.string.reply_at_s, topic.lastReplyAt.timeSpanStringFromNow(resources))
            binding.tvTitle.text = topic.title
            binding.tvSummary.text = summary.text
            binding.tvSummary.isVisible = summary.text.isNotBlank()
            binding.imgAuthor.loadAvatar(topic.author.avatarUrl)
            binding.imgAuthor.setSharedName(who, "imgAuthor-${bindingAdapterPosition}")
            binding.tvAuthor.text = topic.author.loginName
            binding.tvCreateTime.text = resources.getString(R.string.create_at_s, topic.createAt.timeSpanStringFromNow(resources))

            if (summary.images.isEmpty()) {
                binding.layoutThumb.isVisible = false
            } else {
                binding.layoutThumb.isVisible = true
                if (summary.images.size == 1) {
                    binding.imgThumb0.isVisible = true
                    binding.imgThumb0.load(summary.images[0])
                    imgThumbs.forEach { it.isVisible = false }
                } else {
                    binding.imgThumb0.isVisible = false
                    for (i in imgThumbs.indices) {
                        val imgThumb = imgThumbs[i]
                        if (i < summary.images.size) {
                            imgThumb.visibility = View.VISIBLE
                            imgThumb.load(summary.images[i])
                        } else {
                            imgThumb.visibility = View.INVISIBLE
                        }
                    }
                }
            }
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
