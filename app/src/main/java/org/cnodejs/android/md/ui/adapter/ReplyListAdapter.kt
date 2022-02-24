package org.cnodejs.android.md.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.cnodejs.android.md.databinding.ItemReplyBinding
import org.cnodejs.android.md.model.entity.Reply
import org.cnodejs.android.md.ui.listener.OnUserClickListener
import org.cnodejs.android.md.util.fixTextIsSelectable
import org.cnodejs.android.md.util.setMarkdown

class ReplyListAdapter(private val layoutInflater: LayoutInflater, private val who: String) : ListAdapter<Reply, ReplyListAdapter.ViewHolder>(ReplyDiffItemCallback) {
    var myId: String? = null
    set(value) {
        if (field != value) {
            field = value
            notifyItemRangeChanged(0, itemCount)
        }
    }

    var onBtnUpClickListener: ((reply: Reply) -> Unit)? = null
    var onBtnAtClickListener: ((reply: Reply) -> Unit)? = null
    var onUserClickListener: OnUserClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReplyBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, who)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), myId, position == itemCount - 1)
    }

    class ViewHolder(
        private val binding: ItemReplyBinding,
        private val who: String,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            // TODO
        }

        fun bind(reply: Reply, myId: String?, isLast: Boolean) {
            // TODO

            binding.tvContent.setMarkdown(reply.content) // TODO
            binding.tvContent.fixTextIsSelectable()
            binding.divider.isVisible = !isLast
        }
    }
}

private object ReplyDiffItemCallback : DiffUtil.ItemCallback<Reply>() {
    override fun areItemsTheSame(oldItem: Reply, newItem: Reply): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Reply, newItem: Reply): Boolean {
        return oldItem == newItem
    }
}
