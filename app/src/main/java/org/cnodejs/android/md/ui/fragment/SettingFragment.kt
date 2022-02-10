package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentSettingBinding
import org.cnodejs.android.md.util.NavAnim
import org.cnodejs.android.md.util.navPush
import org.cnodejs.android.md.vm.SettingViewModel

class SettingFragment : Fragment() {
    companion object {
        fun open(fragment: Fragment) {
            fragment.navPush(R.id.fragment_setting, anim = NavAnim.FADE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentSettingBinding.inflate(inflater, container, false)

        val settingViewModel: SettingViewModel by activityViewModels()

        // TODO

        return binding.root
    }
}
