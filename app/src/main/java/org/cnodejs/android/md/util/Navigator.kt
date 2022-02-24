package org.cnodejs.android.md.util

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import org.cnodejs.android.md.R
import java.util.*

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
    companion object {
        private const val KEY_SHARED_ELEMENT = "sharedElement"

        fun sharedKey(name: String): String {
            return "${KEY_SHARED_ELEMENT}.${name}"
        }
    }

    @IdRes
    fun current(): Int? {
        return controller.currentDestination?.id
    }

    fun push(
        @IdRes destId: Int,
        args: Bundle? = null,
        anim: NavAnim = NavAnim.FADE,
    ) {
        val options = NavOptions.Builder()
            .apply { anim.applyToOptions(this) }
            .build()
        controller.navigate(destId, args, options)
    }

    fun pushShared(
        @IdRes destId: Int,
        elements: Map<View, String>,
        args: Bundle = Bundle(),
    ) {
        val extrasBuilder = FragmentNavigator.Extras.Builder()
        val uniqueId = UUID.randomUUID().toString()
        elements.forEach { (view, name) ->
            val targetTransitionName = "${uniqueId}:${name}"
            extrasBuilder.addSharedElement(view, targetTransitionName)
            args.putString(sharedKey(name), targetTransitionName)
        }
        controller.navigate(destId, args, null, extrasBuilder.build())
    }

    fun replace(
        @IdRes destId: Int,
        args: Bundle? = null,
        anim: NavAnim = NavAnim.FADE,
    ) {
        val options = NavOptions.Builder()
            .setPopUpTo(current()!!, true)
            .apply { anim.applyToOptions(this) }
            .build()
        controller.navigate(destId, args, options)
    }

    fun replaceShared(
        @IdRes destId: Int,
        elements: Map<View, String>,
        args: Bundle = Bundle(),
    ) {
        val options = NavOptions.Builder()
            .setPopUpTo(current()!!, true)
            .build()
        val extrasBuilder = FragmentNavigator.Extras.Builder()
        val uniqueId = UUID.randomUUID().toString()
        elements.forEach { (view, name) ->
            val targetTransitionName = "${uniqueId}:${name}"
            extrasBuilder.addSharedElement(view, targetTransitionName)
            args.putString(sharedKey(name), targetTransitionName)
        }
        controller.navigate(destId, args, options, extrasBuilder.build())
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

fun View.setSharedName(who: String, name: String) {
    transitionName = "${who}:${name}"
}
