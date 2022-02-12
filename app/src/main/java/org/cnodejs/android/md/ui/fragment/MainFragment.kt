package org.cnodejs.android.md.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.core.view.GravityCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentMainBinding
import org.cnodejs.android.md.model.entity.Tab
import org.cnodejs.android.md.ui.adapter.TopicListAdapter
import org.cnodejs.android.md.ui.dialog.LogoutAlertDialog
import org.cnodejs.android.md.ui.dialog.NeedLoginAlertDialog
import org.cnodejs.android.md.ui.holder.LoadMoreFooter
import org.cnodejs.android.md.ui.listener.OnDoubleClickListener
import org.cnodejs.android.md.ui.listener.TopicDetailNavigateListener
import org.cnodejs.android.md.ui.listener.UserDetailNavigateListener
import org.cnodejs.android.md.ui.listener.listenToRecyclerView
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.vm.MainViewModel
import org.cnodejs.android.md.vm.holder.setupView

class MainFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val tabViews = arrayOf(
            binding.navLayout.tabAll,
            binding.navLayout.tabGood,
            binding.navLayout.tabShare,
            binding.navLayout.tabAsk,
            binding.navLayout.tabJob,
            binding.navLayout.tabDev,
        )

        val a = requireContext().obtainStyledAttributes(intArrayOf(android.R.attr.colorAccent))
        @ColorInt val colorAccent = a.getColor(0, Color.TRANSPARENT)
        a.recycle()

        val mainViewModel: MainViewModel by viewModels()
        observeViewModel(mainViewModel)

        accountViewModel.accountData.observe(viewLifecycleOwner) {
            it?.also { account ->
                binding.navLayout.imgAvatar.loadAvatar(account.avatarUrlCompat)
                binding.navLayout.tvLoginName.text = account.loginName
                binding.navLayout.tvScore.text = getString(R.string.score_d, account.score)
                binding.navLayout.btnLogout.isVisible = true
            } ?: run {
                binding.navLayout.imgAvatar.load(R.drawable.image_placeholder)
                binding.navLayout.tvLoginName.setText(R.string.click_avatar_to_login)
                binding.navLayout.tvScore.text = null
                binding.navLayout.btnLogout.isVisible = false
            }
        }

        accountViewModel.messageCountData.observe(viewLifecycleOwner) {
            it?.let { count ->
                binding.navLayout.btnMessage.setBadge(count)
            }
        }

        settingViewModel.isNightModeData.observe(viewLifecycleOwner) {
            it?.let { isNightMode ->
                if (isNightMode) {
                    binding.navLayout.btnDayNight.setImageResource(R.drawable.baseline_light_mode_24)
                    binding.navLayout.imgNavHeaderBackground.setImageResource(R.drawable.nav_header_bg_dark)
                } else {
                    binding.navLayout.btnDayNight.setImageResource(R.drawable.baseline_dark_mode_24)
                    binding.navLayout.imgNavHeaderBackground.setImageResource(R.drawable.nav_header_bg_light)
                }
            }
        }

        settingViewModel.isDisplayTabDevData.observe(viewLifecycleOwner) {
            it?.let { isDisplayTabDev ->
                binding.navLayout.tabDev.isVisible = isDisplayTabDev
            }
        }

        mainViewModel.topicPagingLiveHolder.tabData.observe(viewLifecycleOwner) {
            it?.let { tab ->
                binding.contentLayout.toolbar.setTitle(tab.titleId)
                for (tabView in tabViews) {
                    tabView.isChecked = tabView.id == tab.tabId
                }
            }
        }

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                accountViewModel.loadMyInfo()
            }
        })

        binding.contentLayout.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.contentLayout.toolbar.setOnClickListener(object : OnDoubleClickListener() {
            override fun onDoubleClick(v: View) {
                binding.contentLayout.recyclerView.scrollToPosition(0)
            }
        })

        binding.contentLayout.refreshLayout.setColorSchemeColors(colorAccent)
        binding.contentLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        val loadMoreFooter = LoadMoreFooter.create(binding.contentLayout.recyclerView)
        val adapter = TopicListAdapter()
        adapter.onTopicClickListener = TopicDetailNavigateListener(navigator)
        adapter.onUserClickListener = UserDetailNavigateListener(navigator)
        mainViewModel.topicPagingLiveHolder.setupView(viewLifecycleOwner, adapter, binding.contentLayout.refreshLayout, loadMoreFooter)
        loadMoreFooter.addToRecyclerView(binding.contentLayout.recyclerView)
        binding.contentLayout.recyclerView.adapter = adapter

        binding.contentLayout.btnCreateTopic.setOnClickListener {
            if (accountViewModel.isLogined()) {
                CreateTopicFragment.open(navigator)
            } else {
                NeedLoginAlertDialog.show(childFragmentManager)
            }
        }
        binding.contentLayout.btnCreateTopic.listenToRecyclerView(binding.contentLayout.recyclerView)

        val onNavMyInfoClickListener = View.OnClickListener {
            accountViewModel.accountData.value?.also { account ->
                UserDetailFragment.open(navigator, account, binding.navLayout.imgAvatar)
            } ?: run {
                LoginFragment.open(navigator)
            }
        }
        binding.navLayout.imgAvatar.setOnClickListener(onNavMyInfoClickListener)
        binding.navLayout.tvLoginName.setOnClickListener(onNavMyInfoClickListener)
        binding.navLayout.tvScore.setOnClickListener(onNavMyInfoClickListener)

        binding.navLayout.btnDayNight.setOnClickListener {
            settingViewModel.toggleNightMode()
        }

        binding.navLayout.btnLogout.setOnClickListener {
            LogoutAlertDialog.show(childFragmentManager)
        }

        val onNavTabClickListener = View.OnClickListener { v: View ->
            mainViewModel.topicPagingLiveHolder.switchTab(Tab.fromTabId(v.id))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        for (tabView in tabViews) {
            tabView.setOnClickListener(onNavTabClickListener)
        }

        binding.navLayout.btnMessage.setOnClickListener {
            if (accountViewModel.isLogined()) {
                MessageListFragment.open(navigator)
            } else {
                NeedLoginAlertDialog.show(childFragmentManager)
            }
        }

        binding.navLayout.btnSetting.setOnClickListener {
            SettingFragment.open(navigator)
        }

        binding.navLayout.btnAbout.setOnClickListener {
            AboutFragment.open(navigator)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    val secondBackPressedTime = System.currentTimeMillis()
                    if (secondBackPressedTime - mainViewModel.firstBackPressedTime > 2000) {
                        mainViewModel.firstBackPressedTime = secondBackPressedTime
                        showToast(R.string.press_back_again_to_exit)
                    } else {
                        requireActivity().finish()
                    }
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }
}
