package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import org.cnodejs.android.md.ui.dialog.BaseDialog
import org.cnodejs.android.md.ui.dialog.LoadingDialog
import org.cnodejs.android.md.util.NavControllerProvider
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.util.setSharedName
import org.cnodejs.android.md.util.showToast
import org.cnodejs.android.md.vm.holder.ILoadingViewModel
import org.cnodejs.android.md.vm.holder.IToastViewModel
import java.util.*

abstract class BaseFragment : Fragment(), NavControllerProvider {
    companion object {
        private const val EXTRA_UNIQUE_ID = "uniqueId"
    }

    override val navigator: Navigator by lazy { Navigator(findNavController()) }

    private lateinit var uniqueId: String
    val who get() = uniqueId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uniqueId = savedInstanceState?.let { state ->
            state.getString(EXTRA_UNIQUE_ID)!!
        } ?: UUID.randomUUID().toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_UNIQUE_ID, uniqueId)
    }

    protected fun setTargetSharedName(view: View, name: String) {
        arguments?.getString(Navigator.sharedKey(name))?.let { transitionName ->
            view.transitionName = transitionName
        } ?: run {
            view.setSharedName(who, name)
        }
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
