package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentCreateTopicBinding
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.CreateTopicViewModel

class CreateTopicFragment : BaseFragment() {
    companion object {
        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_create_topic)
        }
    }

    private val createTopicViewModel: CreateTopicViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentCreateTopicBinding.inflate(inflater, container, false)

        observeViewModel(createTopicViewModel)

        // TODO

        return binding.root
    }
}
