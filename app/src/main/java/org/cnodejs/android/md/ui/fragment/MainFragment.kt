package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentMainBinding
import org.cnodejs.android.md.vm.SettingViewModel

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val settingViewModel: SettingViewModel by activityViewModels()

        settingViewModel.nightModeData.observe(viewLifecycleOwner) {
            it?.let { isNightMode ->
                if (isNightMode) {
                    binding.navLayout.btnDayNight.setImageResource(R.drawable.baseline_light_mode_24)
                    binding.navLayout.imgNavHeaderBackground.setImageDrawable(null)
                } else {
                    binding.navLayout.btnDayNight.setImageResource(R.drawable.baseline_dark_mode_24)
                    binding.navLayout.imgNavHeaderBackground.setImageResource(R.drawable.main_nav_header_bg)
                }
            }
        }

        binding.navLayout.btnDayNight.setOnClickListener {
            settingViewModel.toggleNightMode()
        }

        return binding.root
    }
}
