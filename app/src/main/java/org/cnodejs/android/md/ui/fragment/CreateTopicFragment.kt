package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentCreateTopicBinding
import org.cnodejs.android.md.util.navPush
import org.cnodejs.android.md.vm.CreateTopicViewModel

class CreateTopicFragment : BaseFragment() {
    companion object {
        fun open(fragment: Fragment) {
            fragment.navPush(R.id.fragment_create_topic)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentCreateTopicBinding.inflate(inflater, container, false)

        val createTopicViewModel: CreateTopicViewModel by viewModels()
        observeViewModel(createTopicViewModel)

        // TODO

        return binding.root
    }
}
