package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentLoginBinding
import org.cnodejs.android.md.util.NavUtils
import org.cnodejs.android.md.vm.AccountViewModel

class LoginFragment : Fragment() {
    companion object {
        fun open(fragment: Fragment) {
            NavUtils.push(fragment, R.id.fragment_login)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        val accountViewModel: AccountViewModel by activityViewModels()

        binding.toolbar.setNavigationOnClickListener {
            NavUtils.back(this)
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
