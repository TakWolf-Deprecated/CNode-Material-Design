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
import org.cnodejs.android.md.model.api.CNodeDefine
import org.cnodejs.android.md.model.entity.ITopic
import org.cnodejs.android.md.ui.adapter.ReplyListAdapter
import org.cnodejs.android.md.ui.dialog.NeedLoginAlertDialog
import org.cnodejs.android.md.ui.listener.OnDoubleClickListener
import org.cnodejs.android.md.ui.listener.UserDetailNavigateListener
import org.cnodejs.android.md.ui.listener.listenToRecyclerView
import org.cnodejs.android.md.ui.widget.TopicDetailHeader
import org.cnodejs.android.md.util.NavAnim
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.util.openShare
import org.cnodejs.android.md.util.showToast
import org.cnodejs.android.md.vm.AccountViewModel
import org.cnodejs.android.md.vm.TopicDetailViewModel
import java.util.*

class TopicDetailFragment : BaseFragment() {
    companion object {
        private const val EXTRA_TOPIC_ID = "topicId"
        private const val EXTRA_JUMP_TO_REPLY_ID = "jumpToReplyId"

        fun open(navigator: Navigator, topicId: String, jumpToReplyId: String? = null) {
            val args = Bundle().apply {
                putString(EXTRA_TOPIC_ID, topicId)
                putString(EXTRA_JUMP_TO_REPLY_ID, jumpToReplyId)
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
        topicId = args.getString(EXTRA_TOPIC_ID)!!
        jumpToReplyId = (savedInstanceState ?: args).getString(EXTRA_JUMP_TO_REPLY_ID)

        topicDetailViewModel.topicId = topicId
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_JUMP_TO_REPLY_ID, jumpToReplyId)
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
                    topicDetailViewModel.topicDetailData.value?.let { topicDetail ->
                        val title = getString(R.string.share)
                        val text = "《${topicDetail.title}》\n${CNodeDefine.TOPIC_LINK_PREFIX}${topicDetail.id}\n—— 来自 CNode 社区"
                        requireContext().openShare(title, text)
                    }
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
            }
        }

        topicDetailViewModel.onViewStart()

        return binding.root
    }
}
