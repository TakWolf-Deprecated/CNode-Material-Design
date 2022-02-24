package org.cnodejs.android.md.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.fragment.app.who
import androidx.recyclerview.widget.LinearLayoutManager
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentTopicDetailBinding
import org.cnodejs.android.md.model.entity.ITopic
import org.cnodejs.android.md.ui.adapter.ReplyListAdapter
import org.cnodejs.android.md.ui.dialog.NeedLoginAlertDialog
import org.cnodejs.android.md.ui.listener.OnDoubleClickListener
import org.cnodejs.android.md.ui.listener.UserDetailNavigateListener
import org.cnodejs.android.md.ui.listener.listenToRecyclerView
import org.cnodejs.android.md.ui.widget.TopicDetailHeader
import org.cnodejs.android.md.util.NavAnim
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.AccountViewModel
import org.cnodejs.android.md.vm.TopicDetailViewModel

class TopicDetailFragment : BaseFragment() {
    companion object {
        private const val KEY_TOPIC_ID = "topicId"
        private const val KEY_JUMP_TO_REPLY_ID = "jumpToReplyId"

        fun open(navigator: Navigator, topicId: String, jumpToReplyId: String? = null) {
            val args = Bundle().apply {
                putString(KEY_TOPIC_ID, topicId)
                putString(KEY_JUMP_TO_REPLY_ID, jumpToReplyId)
            }
            navigator.push(R.id.fragment_topic_detail, args, NavAnim.SLIDE)
        }

        fun open(navigator: Navigator, topic: ITopic) {
            open(navigator, topic.id)
        }
    }

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val topicDetailViewModel: TopicDetailViewModel by viewModels()

    private lateinit var topicId: String
    private var jumpToReplyId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = requireArguments()
        topicId = args.getString(KEY_TOPIC_ID)!!
        jumpToReplyId = (savedInstanceState ?: args).getString(KEY_JUMP_TO_REPLY_ID)

        topicDetailViewModel.topicId = topicId
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_JUMP_TO_REPLY_ID, jumpToReplyId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentTopicDetailBinding.inflate(inflater, container, false)

        val a = requireContext().obtainStyledAttributes(intArrayOf(android.R.attr.colorAccent))
        @ColorInt val colorAccent = a.getColor(0, Color.TRANSPARENT)
        a.recycle()

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }
        binding.toolbar.setOnClickListener(object : OnDoubleClickListener() {
            override fun onDoubleClick(v: View) {
                binding.recyclerView.scrollToPosition(0)
            }
        })
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.btn_share -> {
                    // TODO
                    true
                }
                else -> false
            }
        }

        binding.refreshLayout.setColorSchemeColors(colorAccent)
        binding.refreshLayout.setOnRefreshListener {
            topicDetailViewModel.loadTopicDetail()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val header = TopicDetailHeader(inflater, binding.recyclerView, topicDetailViewModel).apply {
            myId = accountViewModel.accountData.value?.id
        }
        binding.recyclerView.addFooterView(inflater, R.layout.footer_topic_detail)
        val adapter = ReplyListAdapter(inflater, who).apply {
            myId = accountViewModel.accountData.value?.id
            onBtnUpClickListener = { reply ->
                // TODO
            }
            onBtnAtClickListener = { reply ->
                // TODO
            }
            onUserClickListener = UserDetailNavigateListener(navigator)
        }
        binding.recyclerView.adapter = adapter

        binding.btnCreateReply.setOnClickListener {
            if (accountViewModel.isLogined()) {
                // TODO
            } else {
                NeedLoginAlertDialog.show(childFragmentManager)
            }
        }
        binding.btnCreateReply.listenToRecyclerView(binding.recyclerView, true)

        observeViewModel(topicDetailViewModel)

        accountViewModel.accountData.observe(viewLifecycleOwner) { account ->
            account?.id.apply {
                header.myId = this
                adapter.myId = this
            }
        }

        topicDetailViewModel.loadingStateData.observe(viewLifecycleOwner) {
            it?.let { isLoading ->
                binding.refreshLayout.isRefreshing = isLoading
            }
        }

        topicDetailViewModel.topicDetailData.observe(viewLifecycleOwner) {
            it?.let { topicDetail ->
                header.updateViews(topicDetail)
                adapter.submitList(topicDetail.replies.toList())
                jumpToReplyId?.let { jumpToReplyId ->
                    this.jumpToReplyId = null
                    for (i in topicDetail.replies.indices) {
                        if (topicDetail.replies[i].id == jumpToReplyId) {
                            val layoutPosition = i + 1 // TODO
                            binding.recyclerView.scrollToPosition(layoutPosition)
                            break
                        }
                    }
                }
            }
        }

        topicDetailViewModel.onViewStart()

        return binding.root
    }
}
