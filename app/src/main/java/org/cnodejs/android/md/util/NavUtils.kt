package org.cnodejs.android.md.util

import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import org.cnodejs.android.md.R

object NavUtils {
    enum class Anim(
        @AnimRes @AnimatorRes val enterAnim: Int = -1,
        @AnimRes @AnimatorRes val exitAnim: Int = -1,
        @AnimRes @AnimatorRes val popEnterAnim: Int = -1,
        @AnimRes @AnimatorRes val popExitAnim: Int = -1,
    ) {
        NONE,
        SLIDE(
            R.anim.page_slide_enter,
            R.anim.page_slide_exit,
            R.anim.page_slide_pop_enter,
            R.anim.page_slide_pop_exit,
        ),
        FADE(
            R.anim.page_fade_enter,
            R.anim.page_fade_exit,
            R.anim.page_fade_pop_enter,
            R.anim.page_fade_pop_exit,
        );

        internal fun applyToOptions(builder: NavOptions.Builder) {
            builder.setEnterAnim(enterAnim).setExitAnim(exitAnim)
                .setPopEnterAnim(popEnterAnim).setPopExitAnim(popExitAnim)
        }
    }

    fun push(
        fragment: Fragment,
        @IdRes destId: Int,
        args: Bundle? = null,
        anim: Anim = Anim.SLIDE,
        extras: FragmentNavigator.Extras? = null,
    ) {
        val options = NavOptions.Builder()
            .apply {
                if (extras == null) {
                    anim.applyToOptions(this)
                }
            }
            .build()
        fragment.findNavController().navigate(destId, args, options, extras)
    }

    fun replace(
        fragment: Fragment,
        @IdRes destId: Int,
        args: Bundle? = null,
        anim: Anim = Anim.FADE,
        extras: FragmentNavigator.Extras? = null,
    ) {
        val controller = fragment.findNavController()
        val currentId = controller.currentDestination!!.id
        val options = NavOptions.Builder()
            .setPopUpTo(currentId, true)
            .apply {
                if (extras == null) {
                    anim.applyToOptions(this)
                }
            }
            .build()
        controller.navigate(destId, args, options, extras)
    }

    fun back(fragment: Fragment) {
        fragment.findNavController().navigateUp()
    }

    fun backTo(fragment: Fragment, @IdRes destId: Int, isPopDest: Boolean = false) {
        fragment.findNavController().popBackStack(destId, isPopDest)
    }
}
