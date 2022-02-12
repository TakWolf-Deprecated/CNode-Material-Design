package org.cnodejs.android.md.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import org.cnodejs.android.md.R
import org.cnodejs.android.md.ui.fragment.LoginFragment

class NeedLoginAlertDialog : BaseDialog() {
    companion object {
        private const val TAG = "NeedLoginAlertDialog"

        fun show(manager: FragmentManager) {
            find(manager, TAG) ?: run {
                NeedLoginAlertDialog().show(manager, TAG)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.Theme_App_Dialog_Alert)
            .setMessage(R.string.need_login_tip)
            .setPositiveButton(R.string.login) { _, _ ->
                LoginFragment.open(navigator)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}
