package org.cnodejs.android.md.ui.jsbridge

import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatDelegate

class AppJavascriptInterface {
    companion object {
        const val NAME = "appBridge"
    }

    @JavascriptInterface
    fun isThemeDarkMode(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }
}
