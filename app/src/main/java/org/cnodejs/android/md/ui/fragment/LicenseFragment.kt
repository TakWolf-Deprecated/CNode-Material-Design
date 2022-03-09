package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentLicenseBinding
import org.cnodejs.android.md.util.Navigator

class LicenseFragment : BaseFragment() {
    companion object {
        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_license)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentLicenseBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }

        return binding.root
    }
}
