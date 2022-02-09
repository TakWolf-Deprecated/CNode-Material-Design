package org.cnodejs.android.md.ui.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import org.cnodejs.android.md.ui.dialog.LoadingDialog
import org.cnodejs.android.md.vm.holder.BaseLiveHolder

abstract class BaseFragment : Fragment() {
    private var loadingDialog: LoadingDialog? = null

    override fun onDestroyView() {
        super.onDestroyView()
        setLoadingVisible(false)
    }

    protected fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setLoadingVisible(visible: Boolean) {
        if (visible) {
            loadingDialog ?: LoadingDialog(requireContext()).also { loadingDialog = it }.show()
        } else {
            loadingDialog?.apply {
                dismiss()
                loadingDialog = null
            }
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
