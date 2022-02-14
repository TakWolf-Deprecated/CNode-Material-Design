package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.TransitionInflater
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentUserDetailBinding
import org.cnodejs.android.md.model.entity.IUser
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.UserDetailViewModel

class UserDetailFragment : BaseFragment() {
    companion object {
        private const val KEY_LOGIN_NAME = "loginName"
        private const val KEY_AVATAR_URL = "avatarUrl"

        fun open(navigator: Navigator, loginName: String) {
            val args = Bundle()
            args.putString(KEY_LOGIN_NAME, loginName)
            navigator.push(R.id.fragment_user_detail, args)
        }

        fun open(navigator: Navigator, user: IUser, imgAvatar: ImageView) {
            if (user.loginName == null) {
                return
            }
            val args = Bundle()
            args.putString(KEY_LOGIN_NAME, user.loginName)
            args.putString(KEY_AVATAR_URL, user.avatarUrl)
            val extras = FragmentNavigatorExtras(imgAvatar to "imgAvatar")
            navigator.push(R.id.fragment_user_detail, args, extras = extras)
        }
    }

    private val userDetailViewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        userDetailViewModel.loginName = requireArguments().getString(KEY_LOGIN_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentUserDetailBinding.inflate(inflater, container, false)

        observeViewModel(userDetailViewModel)

        // TODO

        return binding.root
    }
}
