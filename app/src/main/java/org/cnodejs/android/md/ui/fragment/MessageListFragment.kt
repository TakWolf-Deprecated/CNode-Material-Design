package org.cnodejs.android.md.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentMessageListBinding
import org.cnodejs.android.md.ui.adapter.MessageListAdapter
import org.cnodejs.android.md.ui.listener.OnDoubleClickListener
import org.cnodejs.android.md.ui.listener.TopicDetailNavigateListener
import org.cnodejs.android.md.ui.listener.UserDetailNavigateListener
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.AccountViewModel
import org.cnodejs.android.md.vm.MessageListViewModel
import org.cnodejs.android.md.vm.holder.setupView

class MessageListFragment : BaseFragment() {
    companion object {
        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_message_list)
        }
    }

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val messageListViewModel: MessageListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMessageListBinding.inflate(inflater, container, false)

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
                R.id.btn_mark_all_read -> {
                    messageListViewModel.markAllMessagesRead()
                    true
                }
                else -> false
            }
        }

        binding.refreshLayout.setColorSchemeColors(colorAccent)
        binding.refreshLayout.setOnRefreshListener {
            messageListViewModel.loadMessages()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MessageListAdapter(inflater, uniqueTag).apply {
            onMessageReadListener = { messageId ->
                messageListViewModel.markMessageRead(messageId)
            }
            onTopicClickListener = TopicDetailNavigateListener(navigator)
            onUserClickListener = UserDetailNavigateListener(navigator)
        }
        binding.recyclerView.adapter = adapter

        observeViewModel(messageListViewModel)

        accountViewModel.accountData.observe(viewLifecycleOwner) { account ->
            if (account == null) {
                navigator.back()
            }
        }

        messageListViewModel.loadingStateData.observe(viewLifecycleOwner) {
            it?.let { isLoading ->
                binding.refreshLayout.isRefreshing = isLoading
            }
        }

        messageListViewModel.messagesHolder.setupView(viewLifecycleOwner, adapter)

        messageListViewModel.onViewStart()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }
}
