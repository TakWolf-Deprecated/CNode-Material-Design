package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentUserDetailBinding
import org.cnodejs.android.md.databinding.PageUserDetailTopicsBinding
import org.cnodejs.android.md.model.entity.IUser
import org.cnodejs.android.md.ui.adapter.TopicSimpleListAdapter
import org.cnodejs.android.md.ui.listener.TopicDetailNavigateListener
import org.cnodejs.android.md.ui.listener.UserDetailNavigateListener
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.util.loadAvatar
import org.cnodejs.android.md.vm.UserDetailViewModel

class UserDetailFragment : BaseFragment() {
    companion object {
        private const val KEY_LOGIN_NAME = "loginName"
        private const val KEY_AVATAR_URL = "avatarUrl"

        fun open(navigator: Navigator, loginName: String) {
            val args = Bundle().apply {
                putString(KEY_LOGIN_NAME, loginName)
            }
            navigator.push(R.id.fragment_user_detail, args)
        }

        fun open(navigator: Navigator, user: IUser, imgAvatar: ImageView) {
            if (user.loginName == null) {
                return
            }
            val elements = mapOf<View, String>(imgAvatar to "imgAvatar")
            val args = Bundle().apply {
                putString(KEY_LOGIN_NAME, user.loginName)
                putString(KEY_AVATAR_URL, user.avatarUrl)
            }
            navigator.pushShared(R.id.fragment_user_detail, elements, args)
        }
    }

    private val userDetailViewModel: UserDetailViewModel by viewModels()

    private lateinit var loginName: String
    private var avatarUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        val args = requireArguments()
        loginName = args.getString(KEY_LOGIN_NAME)!!
        avatarUrl = args.getString(KEY_AVATAR_URL)

        userDetailViewModel.loginName = loginName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentUserDetailBinding.inflate(inflater, container, false)

        binding.imgAvatar.loadAvatar(avatarUrl)
        setTargetSharedName(binding.imgAvatar, "imgAvatar")

        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {
                return TopicListFragment.create(loginName, position)
            }
        }

        observeViewModel(userDetailViewModel)

        userDetailViewModel.userDetailData.observe(viewLifecycleOwner) {
            it?.let { userDetail ->
                binding.imgAvatar.loadAvatar(userDetail.user.avatarUrlCompat)
            }
        }

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
            const val TYPE_REPLY = 0
            const val TYPE_CREATE = 1
            const val TYPE_COLLECT = 2

            private const val KEY_TYPE = "type"

            fun create(loginName: String, type: Int): TopicListFragment {
                val fragment = TopicListFragment()
                fragment.arguments = Bundle().apply {
                    putString(KEY_LOGIN_NAME, loginName)
                    putInt(KEY_TYPE, type)
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
            loginName = args.getString(KEY_LOGIN_NAME)!!
            type = args.getInt(KEY_TYPE, -1)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View {
            val binding = PageUserDetailTopicsBinding.inflate(inflater, container, false)

            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = TopicSimpleListAdapter(uniqueTag)
            adapter.onTopicClickListener = TopicDetailNavigateListener(navigator)
            adapter.onUserClickListener = UserDetailNavigateListener(navigator, loginName)
            binding.recyclerView.adapter = adapter

            userDetailViewModel.userDetailData.observe(viewLifecycleOwner) {
                it?.let { userDetail ->
                    when (type) {
                        TYPE_REPLY -> adapter.submitList(userDetail.user.recentReplies.toList())
                        TYPE_CREATE -> adapter.submitList(userDetail.user.recentTopics.toList())
                        TYPE_COLLECT -> adapter.submitList(userDetail.collectTopics.toList())
                    }
                }
            }

            return binding.root
        }
    }
}
