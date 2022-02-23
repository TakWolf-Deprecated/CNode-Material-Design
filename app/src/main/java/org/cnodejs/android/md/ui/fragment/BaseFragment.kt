package org.cnodejs.android.md.ui.fragment

import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.who
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import org.cnodejs.android.md.ui.dialog.BaseDialog
import org.cnodejs.android.md.ui.dialog.LoadingDialog
import org.cnodejs.android.md.util.NavControllerProvider
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.util.setSharedName
import org.cnodejs.android.md.vm.holder.ILoadingViewModel
import org.cnodejs.android.md.vm.holder.IToastViewModel

abstract class BaseFragment : Fragment(), NavControllerProvider {
    override val navigator: Navigator by lazy { Navigator(findNavController()) }

    protected fun setTargetSharedName(view: View, name: String) {
        arguments?.getString(Navigator.sharedKey(name))?.let { transitionName ->
            view.transitionName = transitionName
        } ?: run {
            view.setSharedName(who, name)
        }
    }

    protected fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showToast(@StringRes resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    protected fun observeViewModel(viewModel: ViewModel) {
        if (viewModel is IToastViewModel) {
            viewModel.toastHolder.toastEvent.observe(viewLifecycleOwner) {
                it?.let { message ->
                    showToast(message)
                }
            }
        }

        if (viewModel is ILoadingViewModel) {
            viewModel.loadingHolder.loadingCountData.observe(viewLifecycleOwner) {
                it?.let { count ->
                    if (count > 0) {
                        LoadingDialog.show(childFragmentManager, LoadingDialog.TAG)
                    } else {
                        BaseDialog.dismiss(childFragmentManager, LoadingDialog.TAG)
                    }
                }
            }
        }
    }
}
