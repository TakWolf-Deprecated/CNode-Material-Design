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

fun Fragment.navPush(
    @IdRes destId: Int,
    args: Bundle? = null,
    anim: NavAnim = NavAnim.FADE,
    extras: FragmentNavigator.Extras? = null,
    isSingleTop: Boolean = false,
) {
    val controller = findNavController()
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

fun Fragment.navReplace(
    @IdRes destId: Int,
    args: Bundle? = null,
    anim: NavAnim = NavAnim.FADE,
    extras: FragmentNavigator.Extras? = null,
) {
    val controller = findNavController()
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

fun Fragment.navBack() {
    findNavController().navigateUp()
}

fun Fragment.navBackTo(@IdRes destId: Int, isPopDest: Boolean = false) {
    findNavController().popBackStack(destId, isPopDest)
}
