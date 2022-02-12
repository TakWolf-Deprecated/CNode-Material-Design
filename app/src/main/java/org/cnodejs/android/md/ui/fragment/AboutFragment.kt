package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentAboutBinding
import org.cnodejs.android.md.util.Navigator

class AboutFragment : BaseFragment() {
    companion object {
        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_about)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentAboutBinding.inflate(inflater, container, false)

        // TODO

        return binding.root
    }
}
