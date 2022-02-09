package org.cnodejs.android.md.ui.dialog

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import org.cnodejs.android.md.R

class LoadingDialog(context: Context) : AppCompatDialog(context, R.style.Theme_App_Dialog_Loading) {
    init {
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
    }
}
