package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentQrCodeScanBinding
import org.cnodejs.android.md.util.Navigator

class QRCodeScanFragment : BaseFragment() {
    companion object {
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

        return binding.root
    }
}
