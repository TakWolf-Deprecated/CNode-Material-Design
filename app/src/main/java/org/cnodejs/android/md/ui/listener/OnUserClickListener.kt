package org.cnodejs.android.md.ui.listener

import android.widget.ImageView
import org.cnodejs.android.md.model.entity.IUser
import org.cnodejs.android.md.ui.fragment.UserDetailFragment
import org.cnodejs.android.md.util.Navigator

interface OnUserClickListener {
    fun onUserClick(user: IUser, imgAvatar: ImageView)
}

class UserDetailNavigateListener(private val navigator: Navigator) : OnUserClickListener {
    override fun onUserClick(user: IUser, imgAvatar: ImageView) {
        UserDetailFragment.open(navigator, user, imgAvatar)
    }
}
