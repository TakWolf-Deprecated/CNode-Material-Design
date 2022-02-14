package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import org.cnodejs.android.md.databinding.ItemTopicSimpleBinding
import org.cnodejs.android.md.model.entity.ITopicSimple
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.util.timeSpanStringFromNow

class TopicSimpleListAdapter(private val uniqueTag: String) : TopicListAdapter<ITopicSimple, TopicSimpleListAdapter.ViewHolder>(TopicSimpleDiffItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopicSimpleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, uniqueTag)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemTopicSimpleBinding,
        private val uniqueTag: String,
    ) : TopicListAdapter.ViewHolder(
        binding.root,
        binding.btnItem,
        binding.imgAuthor,
        binding.imgAuthor,
    ) {
        fun bind(topic: ITopicSimple) {
            binding.imgAuthor.loadAvatar(topic.author.avatarUrlCompat)
            binding.imgAuthor.transitionName = "imgAvatar-${bindingAdapterPosition}@${uniqueTag}"
            binding.tvTitle.text = topic.title
            binding.tvAuthor.text = topic.author.loginName
            binding.tvLastReplyTime.text = topic.lastReplyAt.timeSpanStringFromNow(itemView.resources)
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
