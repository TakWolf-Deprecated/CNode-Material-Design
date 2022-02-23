package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentLoginBinding
import org.cnodejs.android.md.ui.dialog.HowToGetAccessTokenTipDialog
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.util.isAccessToken
import org.cnodejs.android.md.vm.LoginViewModel

class LoginFragment : BaseFragment() {
    companion object {
        fun open(navigator: Navigator) {
            if (navigator.current() == R.id.fragment_login) {
                return
            }
            navigator.push(R.id.fragment_login)
        }
    }

    private val loginViewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(QRCodeScanFragment.REQUEST_KEY) { _, result ->
            _binding?.let { binding ->
                val accessToken = result.getString(QRCodeScanFragment.KEY_VALUE)
                binding.edtAccessToken.setText(accessToken)
                binding.btnLogin.callOnClick()
            }
        }
        setFragmentResultListener(LoginByGithubFragment.REQUEST_KEY) { _, result ->
            _binding?.let { binding ->
                val accessToken = result.getString(LoginByGithubFragment.KEY_ACCESS_TOKEN)
                binding.edtAccessToken.setText(accessToken)
                binding.btnLogin.callOnClick()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        _binding = binding

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }

        binding.edtAccessToken.addTextChangedListener {
            binding.edtLayoutAccessToken.error = null
        }

        binding.btnLogin.setOnClickListener {
            val accessToken = (binding.edtAccessToken.text ?: "").trim().toString()
            if (accessToken.isBlank()) {
                binding.edtLayoutAccessToken.error = getString(R.string.please_input_access_token)
                return@setOnClickListener
            }
            if (!accessToken.isAccessToken()) {
                binding.edtLayoutAccessToken.error = getString(R.string.access_token_format_error)
                return@setOnClickListener
            }
            loginViewModel.login(accessToken)
        }

        binding.btnLoginByQrCode.setOnClickListener {
            QRCodeScanFragment.open(navigator)
        }

        binding.btnLoginByGithub.setOnClickListener {
            LoginByGithubFragment.open(navigator)
        }

        binding.btnLoginTip.setOnClickListener {
            HowToGetAccessTokenTipDialog.show(childFragmentManager)
        }

        observeViewModel(loginViewModel)

        loginViewModel.loginedEvent.observe(viewLifecycleOwner) {
            showToast(R.string.login_success)
            navigator.back()
        }

        loginViewModel.errorMessageEvent.observe(viewLifecycleOwner) {
            it?.let { message ->
                binding.edtLayoutAccessToken.error = message
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
