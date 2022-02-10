package org.cnodejs.android.md.ui.fragment

import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import org.cnodejs.android.md.ui.dialog.LoadingDialog
import org.cnodejs.android.md.vm.holder.BaseLiveHolder

abstract class BaseFragment : Fragment() {
    companion object {
        private const val TAG_LOADING = "loading"
    }

    protected fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setLoadingVisible(visible: Boolean) {
        if (visible) {
            childFragmentManager.findFragmentByTag(TAG_LOADING) ?: run {
                LoadingDialog().show(childFragmentManager, TAG_LOADING)
            }
        } else {
            (childFragmentManager.findFragmentByTag(TAG_LOADING) as? DialogFragment)?.dismiss()
        }
    }

    protected fun observeBaseLiveHolder(holder: BaseLiveHolder) {
        holder.toastEvent.observe(viewLifecycleOwner) {
            it?.let { message ->
                showToast(message)
            }
        }

        holder.loadingCountData.observe(viewLifecycleOwner) {
            it?.let { count ->
                setLoadingVisible(count > 0)
            }
        }
    }
}
