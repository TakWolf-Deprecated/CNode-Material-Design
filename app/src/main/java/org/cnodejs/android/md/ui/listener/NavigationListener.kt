package org.cnodejs.android.md.ui.listener

import android.view.View
import org.cnodejs.android.md.util.Navigator

class NavBackOnClickListener(private val navigator: Navigator) : View.OnClickListener {
    override fun onClick(v: View) {
        navigator.back()
    }
}
