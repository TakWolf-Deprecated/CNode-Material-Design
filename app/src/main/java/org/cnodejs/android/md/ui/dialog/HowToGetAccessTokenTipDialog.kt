package org.cnodejs.android.md.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import org.cnodejs.android.md.R

class HowToGetAccessTokenTipDialog : BaseDialog() {
    companion object {
        private const val TAG = "HowToGetAccessTokenTipDialog"

        fun show(manager: FragmentManager) {
            find(manager, TAG) ?: run {
                HowToGetAccessTokenTipDialog().show(manager, TAG)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.Theme_App_Dialog_Alert)
            .setMessage(R.string.how_to_get_access_token_tip_content)
            .setPositiveButton(R.string.ok, null)
            .create()
    }
}
