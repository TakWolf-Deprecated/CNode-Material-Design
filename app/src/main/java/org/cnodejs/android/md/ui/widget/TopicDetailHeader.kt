package org.cnodejs.android.md.ui.widget

import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.takwolf.android.hfrecyclerview.HeaderAndFooterRecyclerView
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.HeaderTopicDetailBinding
import org.cnodejs.android.md.model.entity.TopicDetail
import org.cnodejs.android.md.vm.TopicDetailViewModel

class TopicDetailHeader(
    layoutInflater: LayoutInflater,
    recyclerView: HeaderAndFooterRecyclerView,
    private val topicDetailViewModel: TopicDetailViewModel,
) {
    private val binding = HeaderTopicDetailBinding.inflate(layoutInflater, recyclerView.headerViewContainer, false)

    var myId: String? = null

    init {
        recyclerView.addHeaderView(binding.root)
    }

    fun updateViews(topicDetail: TopicDetail) {
        val resources = binding.root.resources
        binding.layoutTopic.isVisible = true
        binding.tvContent.text = topicDetail.content // TODO
        if (topicDetail.replies.isEmpty()) {
            binding.layoutNoReply.isVisible = true
            binding.layoutReplyBar.isVisible = false
        } else {
            binding.layoutNoReply.isVisible = false
            binding.layoutReplyBar.isVisible = true
            binding.tvRepliesCount.text = resources.getString(R.string.reply_d, topicDetail.replies.size)
        }
    }
}
