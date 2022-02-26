package org.cnodejs.android.md.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.cnodejs.android.md.BuildConfig
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentLoginByGithubBinding
import org.cnodejs.android.md.model.api.CNodeDefine
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.LoginByGithubViewModel

class LoginByGithubFragment : BaseFragment() {
    companion object {
        private const val TAG = "LoginByGithubFragment"

        const val REQUEST_KEY = "requestKey.LoginByGithub"

        const val EXTRA_ACCESS_TOKEN = "accessToken"
        private const val EXTRA_CURRENT_URL = "currentUrl"

        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_login_by_github)
        }
    }

    private val loginByGithubViewModel: LoginByGithubViewModel by viewModels()

    private var _binding: FragmentLoginByGithubBinding? = null

    private val cookieManager = CookieManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            cookieManager.removeAllCookies {}
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.let { binding ->
            outState.putString(EXTRA_CURRENT_URL, binding.web.url)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentLoginByGithubBinding.inflate(inflater, container, false)
        _binding = binding

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }

        binding.web.settings.javaScriptEnabled = true
        binding.web.settings.domStorageEnabled = true
        binding.web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "load url: ${request.url}")
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (url == CNodeDefine.HOST_BASE_URL || url == "${CNodeDefine.HOST_BASE_URL}/") {
                    cookieManager.getCookie(url)?.let { cookie ->
                        loginByGithubViewModel.fetchAccessToken(cookie)
                    }
                }
            }
        }
        binding.web.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                binding.progressBar.progress = newProgress
                binding.progressBar.isVisible = newProgress < 100
            }
        }
        savedInstanceState?.let { state ->
            binding.web.loadUrl(state.getString(EXTRA_CURRENT_URL) ?: "")
        } ?: run {
            binding.web.loadUrl(CNodeDefine.GITHUB_LOGIN_URL)
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                binding.web.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                binding.web.onPause()
            }
        })

        observeViewModel(loginByGithubViewModel)

        loginByGithubViewModel.accessTokenEvent.observe(viewLifecycleOwner) {
            it?.let { accessToken ->
                val result = bundleOf(EXTRA_ACCESS_TOKEN to accessToken)
                setFragmentResult(REQUEST_KEY, result)
                navigator.back()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
