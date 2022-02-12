package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentSettingBinding
import org.cnodejs.android.md.util.Navigator

class SettingFragment : BaseFragment() {
    companion object {
        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_setting)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentSettingBinding.inflate(inflater, container, false)

        // TODO

        return binding.root
    }
}
