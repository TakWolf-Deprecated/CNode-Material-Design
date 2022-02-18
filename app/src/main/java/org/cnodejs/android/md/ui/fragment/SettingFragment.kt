package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentSettingBinding
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.SettingViewModel

class SettingFragment : BaseFragment() {
    companion object {
        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_setting)
        }
    }

    private val settingViewModel: SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentSettingBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }

        binding.btnToggleThemeDarkMode.setOnClickListener {
            settingViewModel.toggleThemeDarkMode()
        }

        binding.btnToggleTopicSaveDraftEnabled.setOnClickListener {
            settingViewModel.toggleTopicSaveDraftEnabled()
        }

        binding.btnToggleTopicSignEnabled.setOnClickListener {
            settingViewModel.toggleTopicSignEnabled()
        }

        binding.btnTopicSignEdit.setOnClickListener {
            // TODO
        }

        binding.btnToggleTopicDisplayTabDev.setOnClickListener {
            settingViewModel.toggleTopicDisplayTabDev()
        }

        settingViewModel.isThemeDarkModeData.observe(viewLifecycleOwner) {
            it?.let { isThemeDarkMode ->
                binding.switchThemeDarkMode.isChecked = isThemeDarkMode
            }
        }

        settingViewModel.isTopicSaveDraftEnabledData.observe(viewLifecycleOwner) {
            it?.let { isTopicSaveDraftEnabled ->
                binding.switchTopicSaveDraft.isChecked = isTopicSaveDraftEnabled
            }
        }

        settingViewModel.isTopicSignEnabledData.observe(viewLifecycleOwner) {
            it?.let { isTopicSignEnabled ->
                binding.switchTopicSign.isChecked = isTopicSignEnabled
            }
        }

        settingViewModel.isTopicDisplayTabDevData.observe(viewLifecycleOwner) {
            it?.let { isTopicDisplayTabDev ->
                binding.switchTopicDisplayTabDev.isChecked = isTopicDisplayTabDev
            }
        }

        return binding.root
    }
}
