package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentQrCodeScanBinding
import org.cnodejs.android.md.util.Navigator
import java.util.*

class QRCodeScanFragment : BaseFragment() {
    companion object {
        const val REQUEST_KEY = "requestKey.QRCodeScanFragment"
        const val KEY_VALUE = "value"

        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_qr_code_scan)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentQrCodeScanBinding.inflate(inflater, container, false)

        // TODO

        binding.btn.setOnClickListener {
            val result = bundleOf(KEY_VALUE to UUID.randomUUID().toString())
            setFragmentResult(REQUEST_KEY, result)
            navigator.back()
        }

        // TODO

        return binding.root
    }
}
