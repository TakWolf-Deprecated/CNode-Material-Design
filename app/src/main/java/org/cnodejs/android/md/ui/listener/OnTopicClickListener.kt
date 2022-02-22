package org.cnodejs.android.md.ui.listener

import org.cnodejs.android.md.ui.fragment.TopicDetailFragment
import org.cnodejs.android.md.util.Navigator

interface OnTopicClickListener {
    fun onTopicClick(topicId: String, replyId: String? = null)
}

class TopicDetailNavigateListener(private val navigator: Navigator) : OnTopicClickListener {
    override fun onTopicClick(topicId: String, replyId: String?) {
        TopicDetailFragment.open(navigator, topicId, replyId)
    }
}
