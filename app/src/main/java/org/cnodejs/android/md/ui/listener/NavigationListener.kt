package org.cnodejs.android.md.ui.listener

import android.view.View
import androidx.fragment.app.Fragment
import org.cnodejs.android.md.util.navBack

class NavBackOnClickListener(private val fragment: Fragment) : View.OnClickListener {
    override fun onClick(v: View) {
        fragment.navBack()
    }
}
