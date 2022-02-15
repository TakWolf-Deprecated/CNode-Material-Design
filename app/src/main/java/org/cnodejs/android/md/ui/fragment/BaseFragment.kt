package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import org.cnodejs.android.md.ui.dialog.BaseDialog
import org.cnodejs.android.md.ui.dialog.LoadingDialog
import org.cnodejs.android.md.util.NavControllerProvider
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.holder.ILoadingViewModel
import org.cnodejs.android.md.vm.holder.IToastViewModel
import java.util.*

abstract class BaseFragment : Fragment(), NavControllerProvider {
    companion object {
        private const val KEY_UNIQUE_TAG = "uniqueTag"
        private const val TAG_LOADING = "${LoadingDialog.TAG}@Base"
    }

    override val navigator: Navigator by lazy { Navigator(findNavController()) }

    private lateinit var _uniqueTag: String
    protected val uniqueTag: String get() = _uniqueTag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _uniqueTag = if (savedInstanceState == null) {
            UUID.randomUUID().toString()
        } else {
            savedInstanceState.getString(KEY_UNIQUE_TAG)!!
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_UNIQUE_TAG, _uniqueTag)
    }

    protected fun setTargetSharedName(view: View, name: String) {
        val key = navigator.sharedKey(name)
        view.transitionName = arguments?.getString(key) ?: "${uniqueTag}:${name}"
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
                        LoadingDialog.show(childFragmentManager, TAG_LOADING)
                    } else {
                        BaseDialog.dismiss(childFragmentManager, TAG_LOADING)
                    }
                }
            }
        }
    }
}
