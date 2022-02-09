package org.cnodejs.android.md.ui.listener

import androidx.fragment.app.Fragment
import org.cnodejs.android.md.model.entity.ITopic
import org.cnodejs.android.md.ui.fragment.TopicDetailFragment

interface OnTopicClickListener {
    fun onTopicClick(topic: ITopic)
}

class TopicDetailNavigateListener(private val fragment: Fragment) : OnTopicClickListener {
    override fun onTopicClick(topic: ITopic) {
        TopicDetailFragment.open(fragment, topic)
    }
}
