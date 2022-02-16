package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentLoginByGithubBinding
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.LoginByGithubViewModel

class LoginByGithubFragment : BaseFragment() {
    companion object {
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

        observeViewModel(loginByGithubViewModel)

        // TODO

        return binding.root
    }
}
