package org.cnodejs.android.md.ui.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import org.cnodejs.android.md.util.NavControllerProvider
import org.cnodejs.android.md.util.Navigator

abstract class BaseDialog : DialogFragment(), NavControllerProvider {
    companion object {
        fun find(manager: FragmentManager, tag: String): BaseDialog? {
            return manager.findFragmentByTag(tag) as? BaseDialog
        }

        fun dismiss(manager: FragmentManager, tag: String) {
            find(manager, tag)?.dismiss()
        }
    }

    override val navigator: Navigator by lazy { Navigator(findNavController()) }
}
