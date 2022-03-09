package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.cnodejs.android.md.R

class LicenseViewModel(application: Application) : AndroidViewModel(application) {
    private val resources = application.resources

    val contentData = MutableLiveData<String>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val content = resources.openRawResource(R.raw.license).bufferedReader().use { it.readText() }
            contentData.postValue(content)
        }
    }
}
