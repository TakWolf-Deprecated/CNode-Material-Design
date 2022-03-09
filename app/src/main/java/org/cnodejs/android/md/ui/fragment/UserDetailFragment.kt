package org.cnodejs.android.md.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentUserDetailBinding
import org.cnodejs.android.md.databinding.PageUserDetailTopicsBinding
import org.cnodejs.android.md.model.api.CNodeDefine
import org.cnodejs.android.md.model.entity.IUser
import org.cnodejs.android.md.ui.adapter.TopicSimpleListAdapter
import org.cnodejs.android.md.ui.listener.TopicDetailNavigateListener
import org.cnodejs.android.md.ui.listener.UserDetailNavigateListener
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.util.openInBrowser
import org.cnodejs.android.md.util.openShare
import org.cnodejs.android.md.vm.UserDetailViewModel

class UserDetailFragment : BaseFragment() {
    companion object {
        private const val EXTRA_LOGIN_NAME = "loginName"
        private const val EXTRA_AVATAR_URL = "avatarUrl"
        private const val EXTRA_IS_SCRIMS_SHOWN = "isScrimsShown"

        fun open(navigator: Navigator, loginName: String) {
            val args = Bundle().apply {
                putString(EXTRA_LOGIN_NAME, loginName)
            }
            navigator.push(R.id.fragment_user_detail, args)
        }

        fun open(navigator: Navigator, user: IUser, imgAvatar: ImageView) {
            if (user.loginName == null) {
                return
            }
            val elements = mapOf<View, String>(imgAvatar to "imgAvatar")
            val args = Bundle().apply {
                putString(EXTRA_LOGIN_NAME, user.loginName)
                putString(EXTRA_AVATAR_URL, user.avatarUrl)
            }
            navigator.pushShared(R.id.fragment_user_detail, elements, args)
        }
    }

    private val userDetailViewModel: UserDetailViewModel by viewModels()

    private lateinit var loginName: String
    private var avatarUrl: String? = null

    private var isScrimsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        val args = requireArguments()
        loginName = args.getString(EXTRA_LOGIN_NAME)!!
        avatarUrl = args.getString(EXTRA_AVATAR_URL)

        isScrimsShown = savedInstanceState?.getBoolean(EXTRA_IS_SCRIMS_SHOWN) ?: false

        userDetailViewModel.loginName = loginName
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(EXTRA_IS_SCRIMS_SHOWN, isScrimsShown)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentUserDetailBinding.inflate(inflater, container, false)

        @ColorInt val colorAppPrimaryVariant = ContextCompat.getColor(requireContext(), R.color.app_primary_variant)
        @ColorInt val colorTranslucentSystemBars = ContextCompat.getColor(requireContext(), R.color.translucent_system_bars)

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.btn_share -> {
                    val title = getString(R.string.share)
                    val text = "$loginName 在 CNode 社区\n${CNodeDefine.USER_LINK_PREFIX}${loginName}"
                    requireContext().openShare(title, text)
                    true
                }
                else -> false
            }
        }

        val updateAppBarViews = {
            if (isScrimsShown) {
                binding.root.insetsColorTop = colorAppPrimaryVariant
                binding.toolbarTitle.visibility = View.VISIBLE
            } else {
                binding.root.insetsColorTop = colorTranslucentSystemBars
                binding.toolbarTitle.visibility = View.GONE
            }
        }
        updateAppBarViews()
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            isScrimsShown = binding.collapsingToolbarLayout.height + verticalOffset < binding.collapsingToolbarLayout.scrimVisibleHeightTrigger
            updateAppBarViews()
        })

        binding.toolbarTitle.title = loginName
        binding.tvLoginName.text = loginName
        binding.imgAvatar.loadAvatar(avatarUrl)
        setTargetSharedName(binding.imgAvatar, "imgAvatar")

        View.OnClickListener {
            userDetailViewModel.loadUserDetail()
        }.apply {
            binding.toolbar.setOnClickListener(this)
            binding.imgAvatar.setOnClickListener(this)
            binding.tvLoginName.setOnClickListener(this)
        }

        binding.tvGithubUsername.setOnClickListener {
            userDetailViewModel.userDetailData.value?.user?.let { user ->
                user.githubUsername?.let { githubUsername ->
                    val uri = Uri.parse("https://github.com/${githubUsername}")
                    requireContext().openInBrowser(uri)
                }
            }
        }

        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {
                return TopicListFragment.create(loginName, position)
            }
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(when (position) {
                0 -> R.string.user_detail_tab_reply
                1 -> R.string.user_detail_tab_create
                2 -> R.string.user_detail_tab_collect
                else -> error("Unknown position")
            })
        }.attach()

        observeViewModel(userDetailViewModel)

        userDetailViewModel.loadingStateData.observe(viewLifecycleOwner) {
            it?.let { isLoading ->
                binding.loadingBar.isVisible = isLoading
            }
        }

        userDetailViewModel.userDetailData.observe(viewLifecycleOwner) {
            it?.let { userDetail ->
                val user = userDetail.user
                binding.imgAvatar.loadAvatar(user.avatarUrl)
                binding.tvGithubUsername.text = getString(R.string.github_s, user.githubUsername)
                binding.tvCreateTime.text = getString(R.string.register_at_s, user.createAt.toLocalDate())
                binding.tvScore.text = getString(R.string.score_d, user.score)
            }
        }

        userDetailViewModel.onViewStart()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    class TopicListFragment : BaseFragment() {
        companion object {
            private const val EXTRA_TYPE = "type"

            fun create(loginName: String, type: Int): TopicListFragment {
                val fragment = TopicListFragment()
                fragment.arguments = Bundle().apply {
                    putString(EXTRA_LOGIN_NAME, loginName)
                    putInt(EXTRA_TYPE, type)
                }
                return fragment
            }
        }

        private val userDetailViewModel: UserDetailViewModel by viewModels({ requireParentFragment() })

        private lateinit var loginName: String
        private var type: Int  = -1

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val args = requireArguments()
            loginName = args.getString(EXTRA_LOGIN_NAME)!!
            type = args.getInt(EXTRA_TYPE, -1)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View {
            val binding = PageUserDetailTopicsBinding.inflate(inflater, container, false)

            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.addFooterView(inflater, R.layout.footer_topic_simple)
            val adapter = TopicSimpleListAdapter(inflater, who).apply {
                onTopicClickListener = TopicDetailNavigateListener(navigator)
                onUserClickListener = UserDetailNavigateListener(navigator, loginName)
            }
            binding.recyclerView.adapter = adapter

            userDetailViewModel.userDetailData.observe(viewLifecycleOwner) {
                it?.let { userDetail ->
                    val topics = when (type) {
                        0 -> userDetail.user.recentReplies.toList()
                        1 -> userDetail.user.recentTopics.toList()
                        2 -> userDetail.collectTopics.toList()
                        else -> error("Unknown type")
                    }
                    adapter.submitList(topics)
                    binding.layoutEmpty.isVisible = topics.isEmpty()
                }
            }

            return binding.root
        }
    }
}
