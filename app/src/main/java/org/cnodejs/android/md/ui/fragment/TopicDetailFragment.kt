package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentTopicDetailBinding
import org.cnodejs.android.md.model.entity.ITopic
import org.cnodejs.android.md.util.NavUtils

class TopicDetailFragment : Fragment() {
    companion object {
        private const val KEY_TOPIC_ID = "topic_id"

        fun open(fragment: Fragment, topicId: String) {
            val args = Bundle()
            args.putString(KEY_TOPIC_ID, topicId)
            NavUtils.push(fragment, R.id.fragment_topic_detail, args)
        }

        fun open(fragment: Fragment, topic: ITopic) {
            open(fragment, topic.id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentTopicDetailBinding.inflate(inflater, container, false)

        // TODO

        return binding.root
    }
}
