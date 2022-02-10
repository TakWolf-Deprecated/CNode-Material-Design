package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.TransitionInflater
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentUserDetailBinding
import org.cnodejs.android.md.model.entity.IUser
import org.cnodejs.android.md.util.NavUtils
import org.cnodejs.android.md.vm.UserDetailViewModel

class UserDetailFragment : BaseFragment() {
    companion object {
        private const val KEY_LOGIN_NAME = "login_name"
        private const val KEY_AVATAR_URL = "avatar_url"

        fun open(fragment: Fragment, loginName: String) {
            val args = Bundle()
            args.putString(KEY_LOGIN_NAME, loginName)
            NavUtils.push(fragment, R.id.fragment_user_detail, args, NavUtils.Anim.FADE)
        }

        fun open(fragment: Fragment, user: IUser, imgAvatar: ImageView) {
            if (user.avatarUrl == null) {
                return
            }
            val args = Bundle()
            args.putString(KEY_LOGIN_NAME, user.loginName)
            args.putString(KEY_AVATAR_URL, user.avatarUrl)
            val extras = FragmentNavigatorExtras(imgAvatar to "img_avatar")
            NavUtils.push(fragment, R.id.fragment_user_detail, args, extras = extras)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedElementTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = sharedElementTransition
        sharedElementReturnTransition = sharedElementTransition
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentUserDetailBinding.inflate(inflater, container, false)

        val userDetailViewModel: UserDetailViewModel by viewModels()
        observeBaseLiveHolder(userDetailViewModel.baseLiveHolder)

        return binding.root
    }
}
