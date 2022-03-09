package org.cnodejs.android.md.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.cnodejs.android.md.BuildConfig
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentAboutBinding
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.util.openInBrowser

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

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }

        binding.tvVersionName.text = BuildConfig.VERSION_NAME

        binding.btnVersionName.setOnClickListener {
            // TODO
        }

        binding.btnOpenSourceUrl.setOnClickListener {
            requireContext().openInBrowser(Uri.parse(getString(R.string.open_source_url_content)))
        }

        binding.btnAboutCnode.setOnClickListener {
            requireContext().openInBrowser(Uri.parse(getString(R.string.about_cnode_content)))
        }

        binding.btnAboutDeveloper.setOnClickListener {
            requireContext().openInBrowser(Uri.parse(getString(R.string.about_developer_content)))
        }

        binding.btnOpenSourceLicense.setOnClickListener {
            LicenseFragment.open(navigator)
        }

        return binding.root
    }
}
