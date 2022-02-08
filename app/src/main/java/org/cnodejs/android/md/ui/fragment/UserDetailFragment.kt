package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentUserDetailBinding
import org.cnodejs.android.md.model.entity.IUser
import org.cnodejs.android.md.util.NavUtils

class UserDetailFragment : Fragment() {
    companion object {
        private const val KEY_LOGIN_NAME = "login_name"
        private const val KEY_AVATAR_URL = "avatar_url"

        fun open(fragment: Fragment, loginName: String?, avatarUrl: String? = null) {
            if (loginName == null) {
                return
            }
            val args = Bundle()
            args.putString(KEY_LOGIN_NAME, loginName)
            args.putString(KEY_AVATAR_URL, avatarUrl)
            NavUtils.push(fragment, R.id.fragment_user_detail, args)
        }

        fun open(fragment: Fragment, user: IUser) {
            open(fragment, user.loginName, user.avatarUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentUserDetailBinding.inflate(inflater, container, false)

        // TODO

        return binding.root
    }
}
