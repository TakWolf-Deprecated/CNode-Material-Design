package org.cnodejs.android.md.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.FragmentManager
import org.cnodejs.android.md.R

class LoadingDialog : BaseDialog() {
    companion object {
        const val TAG = "LoadingDialog"

        fun show(manager: FragmentManager, tag: String) {
            find(manager, tag) ?: run {
                LoadingDialog().show(manager, tag)
            }
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
