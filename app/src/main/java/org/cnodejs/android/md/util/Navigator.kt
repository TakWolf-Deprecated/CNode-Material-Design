package org.cnodejs.android.md.util

import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import org.cnodejs.android.md.R

enum class NavAnim(
    @AnimRes @AnimatorRes internal val enterAnim: Int = -1,
    @AnimRes @AnimatorRes internal val exitAnim: Int = -1,
    @AnimRes @AnimatorRes internal val popEnterAnim: Int = -1,
    @AnimRes @AnimatorRes internal val popExitAnim: Int = -1,
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

class Navigator(private val controller: NavController) {
    fun push(
        @IdRes destId: Int,
        args: Bundle? = null,
        anim: NavAnim = NavAnim.FADE,
        extras: FragmentNavigator.Extras? = null,
        isSingleTop: Boolean = false,
    ) {
        if (isSingleTop) {
            val currentId = controller.currentDestination?.id ?: -1
            if (currentId == destId) {
                return
            }
        }
        val options = NavOptions.Builder()
            .apply {
                if (extras == null) {
                    anim.applyToOptions(this)
                }
            }
            .build()
        controller.navigate(destId, args, options, extras)
    }

    fun replace(
        @IdRes destId: Int,
        args: Bundle? = null,
        anim: NavAnim = NavAnim.FADE,
        extras: FragmentNavigator.Extras? = null,
    ) {
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

    fun back() {
        controller.navigateUp()
    }

    fun backTo(@IdRes destId: Int, isPopDest: Boolean = false) {
        controller.popBackStack(destId, isPopDest)
    }
}

interface NavControllerProvider {
    val navigator: Navigator
}
