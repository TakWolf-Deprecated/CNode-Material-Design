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
import org.cnodejs.android.md.util.JsonUtils
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.LoginByGithubViewModel

class LoginByGithubFragment : BaseFragment() {
    companion object {
        private const val TAG = "LoginByGithubFragment"

        const val REQUEST_KEY = "requestKey.LoginByGithub"

        const val EXTRA_ACCESS_TOKEN = "accessToken"
        private const val EXTRA_CURRENT_URL = "currentUrl"
        private const val EXTRA_CURRENT_HTML = "currentHtml"

        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_login_by_github)
        }
    }

    private val loginByGithubViewModel: LoginByGithubViewModel by viewModels()

    private var _binding: FragmentLoginByGithubBinding? = null

    private val cookieManager = CookieManager.getInstance()

    private lateinit var currentUrl: String
    private var currentHtml: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { state ->
            currentUrl = state.getString(EXTRA_CURRENT_URL)!!
            currentHtml = state.getString(EXTRA_CURRENT_HTML)
        } ?: run {
            currentUrl = CNodeDefine.GITHUB_LOGIN_URL
            cookieManager.removeAllCookies {}
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.let { binding ->
            outState.putString(EXTRA_CURRENT_URL, currentUrl)
            outState.putString(EXTRA_CURRENT_HTML, currentHtml)
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
        binding.web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "--> ${request.url}")
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                if (url == CNodeDefine.HOST_BASE_URL || url == "${CNodeDefine.HOST_BASE_URL}/") {
                    cookieManager.getCookie(url)?.let { cookie ->
                        loginByGithubViewModel.fetchAccessToken(cookie)
                    }
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                currentUrl = url
                val script = "(function() { return '<html>' + document.getElementsByTagName('html')[0].innerHTML + '</html>'; })();"
                view.evaluateJavascript(script) { json ->
                    currentHtml = JsonUtils.moshi.adapter(String::class.java).fromJson(json)
                }
            }
        }
        binding.web.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                binding.progressBar.progress = newProgress
                binding.progressBar.isVisible = newProgress < 100
            }
        }
        currentHtml?.let { html ->
            binding.web.loadDataWithBaseURL(currentUrl, html, "text/html", "utf-8", null)
        } ?: run {
            binding.web.loadUrl(currentUrl)
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
