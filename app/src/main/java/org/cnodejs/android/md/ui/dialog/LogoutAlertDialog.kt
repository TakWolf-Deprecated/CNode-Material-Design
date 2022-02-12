package org.cnodejs.android.md.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import org.cnodejs.android.md.R
import org.cnodejs.android.md.model.store.AppStoreHolder

class LogoutAlertDialog : BaseDialog() {
    companion object {
        private const val TAG = "LogoutAlertDialog"

        fun show(manager: FragmentManager) {
            find(manager, TAG) ?: run {
                LogoutAlertDialog().show(manager, TAG)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.Theme_App_Dialog_Alert)
            .setMessage(R.string.logout_tip)
            .setPositiveButton(R.string.logout) { _, _ ->
                val accountStore = AppStoreHolder.getInstance(requireActivity().application).accountStore
                accountStore.logout()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}
