package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentMessageListBinding
import org.cnodejs.android.md.util.navPush
import org.cnodejs.android.md.vm.MessageListViewModel

class MessageListFragment : BaseFragment() {
    companion object {
        fun open(fragment: Fragment) {
            fragment.navPush(R.id.fragment_message_list)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMessageListBinding.inflate(inflater, container, false)

        val messageListViewModel: MessageListViewModel by viewModels()
        observeViewModel(messageListViewModel)

        // TODO

        return binding.root
    }
}
