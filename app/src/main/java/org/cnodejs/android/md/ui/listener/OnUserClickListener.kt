package org.cnodejs.android.md.ui.listener

import android.widget.ImageView
import androidx.fragment.app.Fragment
import org.cnodejs.android.md.model.entity.IUser
import org.cnodejs.android.md.ui.fragment.UserDetailFragment

interface OnUserClickListener {
    fun onUserClick(user: IUser, imgAvatar: ImageView)
}

class UserDetailNavigateListener(private val fragment: Fragment) : OnUserClickListener {
    override fun onUserClick(user: IUser, imgAvatar: ImageView) {
        UserDetailFragment.open(fragment, user, imgAvatar)
    }
}
