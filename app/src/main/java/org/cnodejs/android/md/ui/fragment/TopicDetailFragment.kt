package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentTopicDetailBinding
import org.cnodejs.android.md.model.entity.ITopic
import org.cnodejs.android.md.util.NavAnim
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.TopicDetailViewModel

class TopicDetailFragment : BaseFragment() {
    companion object {
        private const val KEY_TOPIC_ID = "topicId"

        fun open(navigator: Navigator, topicId: String) {
            val args = Bundle().apply {
                putString(KEY_TOPIC_ID, topicId)
            }
            navigator.push(R.id.fragment_topic_detail, args, NavAnim.SLIDE)
        }

        fun open(navigator: Navigator, topic: ITopic) {
            open(navigator, topic.id)
        }
    }

    private val topicDetailViewModel: TopicDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentTopicDetailBinding.inflate(inflater, container, false)

        // TODO

        observeViewModel(topicDetailViewModel)

        // TODO

        return binding.root
    }
}
