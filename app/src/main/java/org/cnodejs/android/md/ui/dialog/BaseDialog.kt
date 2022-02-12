package org.cnodejs.android.md.ui.dialog

import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import org.cnodejs.android.md.util.NavControllerProvider
import org.cnodejs.android.md.util.Navigator

abstract class BaseDialog : DialogFragment(), NavControllerProvider {
    override val navigator: Navigator = Navigator(findNavController())
}
