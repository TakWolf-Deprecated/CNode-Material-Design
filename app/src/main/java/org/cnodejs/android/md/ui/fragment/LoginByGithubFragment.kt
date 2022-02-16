package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentLoginByGithubBinding
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.LoginByGithubViewModel
import java.util.*

class LoginByGithubFragment : BaseFragment() {
    companion object {
        const val REQUEST_KEY = "requestKey.LoginByGithubFragment"
        const val KEY_ACCESS_TOKEN = "accessToken"

        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_login_by_github)
        }
    }

    private val loginByGithubViewModel: LoginByGithubViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentLoginByGithubBinding.inflate(inflater, container, false)

        // TODO

        binding.btn.setOnClickListener {
            val result = bundleOf(KEY_ACCESS_TOKEN to UUID.randomUUID().toString())
            setFragmentResult(REQUEST_KEY, result)
            navigator.back()
        }

        // TODO

        return binding.root
    }
}
