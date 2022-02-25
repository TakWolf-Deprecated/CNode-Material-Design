package org.cnodejs.android.md.ui.fragment

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
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.LoginByGithubViewModel
import org.cnodejs.android.md.vm.UserDetailViewModel

class LoginByGithubFragment : BaseFragment() {
    companion object {
        private const val TAG = "LoginByGithubFragment"

        const val REQUEST_KEY = "requestKey.LoginByGithub"
        const val EXTRA_ACCESS_TOKEN = "accessToken"

        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_login_by_github)
        }
    }

    private val loginByGithubViewModel: LoginByGithubViewModel by viewModels()

    private val cookieManager = CookieManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            cookieManager.apply {
                setCookie("https://github.com", null)
                flush()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentLoginByGithubBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }

        binding.web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "load url: ${request.url}")
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        binding.web.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                binding.progressBar.progress = newProgress
                binding.progressBar.isVisible = newProgress < 100
            }
        }
        if (savedInstanceState == null) {
            binding.web.loadUrl("https://cnodejs.org/auth/github")
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                binding.web.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                binding.web.onPause()
            }
        })

        loginByGithubViewModel.accessTokenEvent.observe(viewLifecycleOwner) {
            it?.let { accessToken ->
                val result = bundleOf(EXTRA_ACCESS_TOKEN to accessToken)
                setFragmentResult(REQUEST_KEY, result)
                navigator.back()
            }
        }

        return binding.root
    }
}
