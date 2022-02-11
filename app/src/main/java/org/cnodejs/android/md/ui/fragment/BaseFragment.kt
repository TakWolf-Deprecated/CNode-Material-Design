package org.cnodejs.android.md.ui.fragment

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import org.cnodejs.android.md.ui.dialog.LoadingDialog
import org.cnodejs.android.md.vm.holder.BaseLiveHolder

abstract class BaseFragment : Fragment() {
    protected fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showToast(@StringRes resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    protected fun observeBaseLiveHolder(holder: BaseLiveHolder) {
        holder.toastEvent.observe(viewLifecycleOwner) {
            it?.let { message ->
                showToast(message)
            }
        }

        holder.loadingCountData.observe(viewLifecycleOwner) {
            it?.let { count ->
                if (count > 0) {
                    LoadingDialog.show(childFragmentManager)
                } else {
                    LoadingDialog.dismiss(childFragmentManager)
                }
            }
        }
    }
}
