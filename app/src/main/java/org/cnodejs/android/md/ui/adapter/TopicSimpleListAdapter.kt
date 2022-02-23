package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import org.cnodejs.android.md.databinding.ItemTopicSimpleBinding
import org.cnodejs.android.md.model.entity.ITopicSimple
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.util.setSharedName
import org.cnodejs.android.md.util.timeSpanStringFromNow

class TopicSimpleListAdapter(private val layoutInflater: LayoutInflater, private val who: String) : TopicListAdapter<ITopicSimple, TopicSimpleListAdapter.ViewHolder>(TopicSimpleDiffItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopicSimpleBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, who)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position == itemCount - 1)
    }

    class ViewHolder(
        private val binding: ItemTopicSimpleBinding,
        private val who: String,
    ) : TopicListAdapter.ViewHolder(
        binding.root,
        binding.btnItem,
        binding.imgAuthor,
        binding.imgAuthor,
    ) {
        fun bind(topic: ITopicSimple, isLast: Boolean) {
            binding.imgAuthor.loadAvatar(topic.author.avatarUrl)
            binding.imgAuthor.setSharedName(who, "imgAuthor-${bindingAdapterPosition}")
            binding.tvTitle.text = topic.title
            binding.tvAuthor.text = topic.author.loginName
            binding.tvLastReplyTime.text = topic.lastReplyAt.timeSpanStringFromNow(itemView.resources)
            binding.divider.isVisible = !isLast
        }
    }
}

private object TopicSimpleDiffItemCallback : DiffUtil.ItemCallback<ITopicSimple>() {
    override fun areItemsTheSame(oldItem: ITopicSimple, newItem: ITopicSimple): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ITopicSimple, newItem: ITopicSimple): Boolean {
        return oldItem.id == newItem.id && oldItem.author == newItem.author
                && oldItem.title == newItem.title && oldItem.lastReplyAt == newItem.lastReplyAt
    }
}
