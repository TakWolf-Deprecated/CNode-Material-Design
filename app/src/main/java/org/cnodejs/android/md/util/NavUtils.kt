package org.cnodejs.android.md.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController

object NavUtils {
    fun push(
        fragment: Fragment,
        @IdRes destId: Int,
        args: Bundle? = null,
        extras: FragmentNavigator.Extras? = null,
    ) {
        fragment.findNavController().navigate(
            destId,
            args,
            NavOptions.Builder().build(),
            extras,
        )
    }

    fun back(fragment: Fragment) {
        fragment.findNavController().navigateUp()
    }
}
