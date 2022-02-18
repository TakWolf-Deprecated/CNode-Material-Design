package org.cnodejs.android.md.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import org.cnodejs.android.md.R
import org.cnodejs.android.md.bus.AuthInvalidEvent
import org.cnodejs.android.md.ui.dialog.AuthInvalidAlertDialog
import org.cnodejs.android.md.util.NavControllerProvider
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.SettingViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NavHostActivity : AppCompatActivity(), NavControllerProvider {
    override val navigator: Navigator by lazy { Navigator(findNavController(R.id.nav_host)) }

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        EventBus.getDefault().register(this)

        settingViewModel.loadThemeConfig()

        setContentView(R.layout.activity_nav_host)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAuthInvalid(event: AuthInvalidEvent) {
        AuthInvalidAlertDialog.show(supportFragmentManager)
    }
}
