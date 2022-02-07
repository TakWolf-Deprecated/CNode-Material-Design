package org.cnodejs.android.md.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import org.cnodejs.android.md.R
import org.cnodejs.android.md.vm.SettingViewModel

class NavHostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val settingViewModel: SettingViewModel by viewModels()
        settingViewModel.loadNightMode()

        setContentView(R.layout.activity_nav_host)
    }
}
