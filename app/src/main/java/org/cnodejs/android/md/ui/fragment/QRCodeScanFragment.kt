package org.cnodejs.android.md.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentQrCodeScanBinding
import org.cnodejs.android.md.util.Navigator
import java.util.concurrent.Executors

class QRCodeScanFragment : BaseFragment() {
    companion object {
        const val REQUEST_KEY = "requestKey.QRCodeScanFragment"
        const val KEY_VALUE = "value"

        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_qr_code_scan)
        }
    }

    private var _binding: FragmentQrCodeScanBinding? = null

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val cameraExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                showRequestPermissionRationale()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentQrCodeScanBinding.inflate(inflater, container, false)
        _binding = binding

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                showRequestPermissionRationale()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRequestPermissionRationale() {
        // TODO
    }

    private fun startCamera() {
        _binding?.let { binding ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get().apply {
                    unbindAll()
                }
                val preview = Preview.Builder()
                    .build()
                    .apply {
                        setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .apply {
                        setAnalyzer(cameraExecutor, QRCodeAnalyzer { value ->
                            binding.root.post {
                                cameraProvider.unbindAll()
                                handleBarcodeResult(value)
                            }
                        })
                    }
                try {
                    cameraProvider.bindToLifecycle(viewLifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalysis)
                } catch (e: Exception) {
                    showToast(R.string.camera_bind_failed)
                }
            }, ContextCompat.getMainExecutor(requireContext()))
        }
    }

    private fun handleBarcodeResult(value: String) {
        val result = bundleOf(KEY_VALUE to value)
        setFragmentResult(REQUEST_KEY, result)
        navigator.back()
    }

    private class QRCodeAnalyzer(private val onResultListener: (value: String) -> Unit) : ImageAnalysis.Analyzer {
        private val scanner = BarcodeScanning.getClient(BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build())

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            imageProxy.image?.let { image ->
                val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
                scanner.process(inputImage).addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        barcode.displayValue?.let { value ->
                            onResultListener(value)
                            return@addOnSuccessListener
                        }
                    }
                }
            }
            imageProxy.close()
        }
    }
}
