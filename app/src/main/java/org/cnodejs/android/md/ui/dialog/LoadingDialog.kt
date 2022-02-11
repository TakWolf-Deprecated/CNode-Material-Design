package org.cnodejs.android.md.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import org.cnodejs.android.md.R

class LoadingDialog : DialogFragment() {
    companion object {
        private const val TAG = "LoadingDialog"

        fun show(manager: FragmentManager) {
            manager.findFragmentByTag(TAG) ?: run {
                LoadingDialog().show(manager, TAG)
            }
        }

        fun dismiss(manager: FragmentManager) {
            (manager.findFragmentByTag(TAG) as? DialogFragment)?.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AppCompatDialog(context, R.style.Theme_App_Dialog_Loading).apply {
            setContentView(R.layout.dialog_loading)
        }
    }
}
