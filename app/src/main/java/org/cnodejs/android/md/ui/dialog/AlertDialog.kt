package org.cnodejs.android.md.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import org.cnodejs.android.md.R
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.cnodejs.android.md.ui.fragment.LoginFragment

class AuthInvalidAlertDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.Theme_App_Dialog_Alert)
            .setMessage(R.string.access_token_out_of_date)
            .setPositiveButton(R.string.re_login) { _, _ ->
                LoginFragment.open(this)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}

class NeedLoginAlertDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.Theme_App_Dialog_Alert)
            .setMessage(R.string.need_login_tip)
            .setPositiveButton(R.string.login) { _, _ ->
                LoginFragment.open(this)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}

class LogoutAlertDialog : DialogFragment() {
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
