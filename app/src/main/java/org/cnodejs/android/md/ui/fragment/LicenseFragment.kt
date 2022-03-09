package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentLicenseBinding
import org.cnodejs.android.md.util.NavAnim
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.LicenseViewModel

class LicenseFragment : BaseFragment() {
    companion object {
        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_license, anim = NavAnim.SLIDE)
        }
    }

    private val licenseViewModel: LicenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentLicenseBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }

        binding.web.setOnHideMaskListener {
            binding.webMask.isVisible = false
        }

        licenseViewModel.contentData.observe(viewLifecycleOwner) {
            it?.let { content ->
                binding.web.updateContent(content)
            }
        }

        return binding.root
    }
}
