package org.cnodejs.android.md.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.DialogFragment
import org.cnodejs.android.md.R

class LoadingDialog : DialogFragment() {
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
