package org.cnodejs.android.md.ui.listener

import android.widget.ImageView
import org.cnodejs.android.md.model.entity.IUser
import org.cnodejs.android.md.ui.fragment.UserDetailFragment
import org.cnodejs.android.md.util.Navigator

interface OnUserClickListener {
    fun onUserClick(loginName: String)

    fun onUserClick(user: IUser, imgAvatar: ImageView)
}

class UserDetailNavigateListener(
    private val navigator: Navigator,
    private val currentLoginName: String? = null,
) : OnUserClickListener {
    override fun onUserClick(loginName: String) {
        if (loginName != currentLoginName) {
            UserDetailFragment.open(navigator, loginName)
        }
    }

    override fun onUserClick(user: IUser, imgAvatar: ImageView) {
        if (user.loginName != currentLoginName) {
            UserDetailFragment.open(navigator, user, imgAvatar)
        }
    }
}
