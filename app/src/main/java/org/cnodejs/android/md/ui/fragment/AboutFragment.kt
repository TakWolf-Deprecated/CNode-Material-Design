package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentAboutBinding
import org.cnodejs.android.md.util.NavUtils

class AboutFragment : Fragment() {
    companion object {
        fun open(fragment: Fragment) {
            NavUtils.push(fragment, R.id.fragment_about, anim = NavUtils.Anim.FADE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentAboutBinding.inflate(inflater, container, false)

        // TODO

        return binding.root
    }
}
