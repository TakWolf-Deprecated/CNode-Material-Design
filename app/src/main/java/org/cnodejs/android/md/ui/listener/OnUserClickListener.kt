package org.cnodejs.android.md.ui.listener

import androidx.fragment.app.Fragment
import org.cnodejs.android.md.model.entity.IUser
import org.cnodejs.android.md.ui.fragment.UserDetailFragment

interface OnUserClickListener {
    fun onUserClick(user: IUser)
}

class UserDetailNavigateListener(private val fragment: Fragment) : OnUserClickListener {
    override fun onUserClick(user: IUser) {
        UserDetailFragment.open(fragment, user)
    }
}
