package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.BundleCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentMainBinding
import org.cnodejs.android.md.model.entity.Account
import org.cnodejs.android.md.util.NavUtils
import org.cnodejs.android.md.util.OnDoubleClickListener
import org.cnodejs.android.md.vm.AccountViewModel
import org.cnodejs.android.md.vm.SettingViewModel

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val accountViewModel: AccountViewModel by activityViewModels()
        val settingViewModel: SettingViewModel by activityViewModels()

        accountViewModel.accountData.observe(viewLifecycleOwner) {
            it?.also { account ->
                binding.navLayout.imgLoginAvatar.load(account.avatarUrl) {
                    placeholder(R.drawable.image_placeholder)
                }
                binding.navLayout.tvLoginName.text = account.loginName
                binding.navLayout.tvScore.text = getString(R.string.score__d_, account.score)
                binding.navLayout.btnLogout.visibility = View.VISIBLE
            } ?: run {
                binding.navLayout.imgLoginAvatar.load(R.drawable.image_placeholder)
                binding.navLayout.tvLoginName.setText(R.string.click_avatar_to_login)
                binding.navLayout.tvScore.text = null
                binding.navLayout.btnLogout.visibility = View.GONE
            }
        }

        settingViewModel.nightModeData.observe(viewLifecycleOwner) {
            it?.let { isNightMode ->
                if (isNightMode) {
                    binding.navLayout.btnDayNight.setImageResource(R.drawable.baseline_light_mode_24)
                    binding.navLayout.imgNavHeaderBackground.setImageResource(R.drawable.login_header_bg)
                } else {
                    binding.navLayout.btnDayNight.setImageResource(R.drawable.baseline_dark_mode_24)
                    binding.navLayout.imgNavHeaderBackground.setImageResource(R.drawable.main_nav_header_bg)
                }
            }
        }

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            // TODO
        })

        binding.contentLayout.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.contentLayout.toolbar.setOnClickListener(object : OnDoubleClickListener(400) {
            override fun onDoubleClick(v: View) {
                binding.contentLayout.recyclerView.scrollToPosition(0)
            }
        })

        val onNavMyInfoClickListener = View.OnClickListener {
            accountViewModel.accountData.value?.also { account ->
                UserDetailFragment.open(this, account)
            } ?: run {
                LoginFragment.open(this)
            }
        }
        binding.navLayout.imgLoginAvatar.setOnClickListener(onNavMyInfoClickListener)
        binding.navLayout.tvLoginName.setOnClickListener(onNavMyInfoClickListener)
        binding.navLayout.tvScore.setOnClickListener(onNavMyInfoClickListener)

        binding.navLayout.btnDayNight.setOnClickListener {
            settingViewModel.toggleNightMode()
        }

        return binding.root
    }
}
