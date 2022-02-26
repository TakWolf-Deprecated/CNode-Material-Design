package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.ItemTopicBinding
import org.cnodejs.android.md.model.entity.Topic
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.util.loadThumb
import org.cnodejs.android.md.util.setSharedName
import org.cnodejs.android.md.util.timeSpanStringFromNow

class TopicListAdapter(
    private val layoutInflater: LayoutInflater,
    private val who: String,
) : ITopicListAdapter<Topic, TopicListAdapter.ViewHolder>(TopicDiffItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopicBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, who)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemTopicBinding,
        private val who: String,
    ) : ITopicListAdapter.ViewHolder(
        binding.root,
        binding.layoutContent,
        binding.layoutAuthor,
        binding.imgAuthor,
    ) {
        private val resources = itemView.resources
        private val imgThumbs = listOf(binding.imgThumb1, binding.imgThumb2, binding.imgThumb3)

        fun bind(topic: Topic) {
            binding.imgGood.isVisible = topic.isGood
            binding.tvTop.isVisible = topic.isTop
            binding.tvTab.isVisible = !topic.isTop
            binding.tvTab.setText(topic.tab.titleId)
            binding.tvReplyAndVisitCount.text = resources.getString(R.string.d_reply_d_visit, topic.replyCount, topic.visitCount)
            binding.tvReplyTime.text = resources.getString(R.string.reply_at_s, topic.lastReplyAt.timeSpanStringFromNow(resources))
            binding.tvTitle.text = topic.title
            binding.tvSummary.text = topic.content.summary
            binding.tvSummary.isVisible = topic.content.summary.isNotBlank()
            binding.imgAuthor.loadAvatar(topic.author.avatarUrl)
            binding.imgAuthor.setSharedName(who, "imgAuthor-${bindingAdapterPosition}")
            binding.tvAuthor.text = topic.author.loginName
            binding.tvCreateTime.text = resources.getString(R.string.create_at_s, topic.createAt.timeSpanStringFromNow(resources))

            val images = topic.content.images
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

private object TopicDiffItemCallback : DiffUtil.ItemCallback<Topic>() {
    override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
        return oldItem == newItem
    }
}
