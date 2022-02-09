package org.cnodejs.android.md.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentMainBinding
import org.cnodejs.android.md.ui.adapter.TopicListAdapter
import org.cnodejs.android.md.ui.holder.LoadMoreFooter
import org.cnodejs.android.md.util.OnDoubleClickListener
import org.cnodejs.android.md.vm.AccountViewModel
import org.cnodejs.android.md.vm.MainViewModel
import org.cnodejs.android.md.vm.SettingViewModel
import org.cnodejs.android.md.vm.holder.setupView

class MainFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val a = requireContext().obtainStyledAttributes(intArrayOf(android.R.attr.colorAccent))
        @ColorInt val colorAccent = a.getColor(0, Color.TRANSPARENT)
        a.recycle()

        val accountViewModel: AccountViewModel by activityViewModels()
        val settingViewModel: SettingViewModel by activityViewModels()
        val mainViewModel: MainViewModel by viewModels()
        observeBaseLiveHolder(mainViewModel.baseLiveHolder)

        accountViewModel.accountData.observe(viewLifecycleOwner) {
            it?.also { account ->
                binding.navLayout.imgLoginAvatar.load(account.getCompatAvatarUrl()) {
                    placeholder(R.drawable.image_placeholder)
                }
                binding.navLayout.tvLoginName.text = account.loginName
                binding.navLayout.tvScore.text = getString(R.string.score_d, account.score)
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

        mainViewModel.topicPagingLiveHolder.tabData.observe(viewLifecycleOwner) {
            // TODO
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

        binding.contentLayout.refreshLayout.setColorSchemeColors(colorAccent)
        binding.contentLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        val loadMoreFooter = LoadMoreFooter.create(binding.contentLayout.recyclerView)
        val adapter = TopicListAdapter()
        mainViewModel.topicPagingLiveHolder.setupView(viewLifecycleOwner, adapter, binding.contentLayout.refreshLayout, loadMoreFooter)
        loadMoreFooter.addToRecyclerView(binding.contentLayout.recyclerView)
        binding.contentLayout.recyclerView.adapter = adapter

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
