package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentLoginBinding
import org.cnodejs.android.md.util.NavAnim
import org.cnodejs.android.md.util.navBack
import org.cnodejs.android.md.util.navPush
import org.cnodejs.android.md.vm.LoginViewModel

class LoginFragment : BaseFragment() {
    companion object {
        fun open(fragment: Fragment) {
            fragment.navPush(R.id.fragment_login, anim = NavAnim.FADE, isSingleTop = true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        val loginViewModel: LoginViewModel by viewModels()
        observeBaseLiveHolder(loginViewModel.baseLiveHolder)

        binding.toolbar.setNavigationOnClickListener {
            navBack()
        }

        binding.btnLogin.setOnClickListener {
            // TODO
        }

        binding.btnLoginByQrCode.setOnClickListener {
            // TODO
        }

        binding.btnLoginByGithub.setOnClickListener {
            // TODO
        }

        binding.btnLoginTip.setOnClickListener {
            // TODO
        }

        return binding.root
    }
}
