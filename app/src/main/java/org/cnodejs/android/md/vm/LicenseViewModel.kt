package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.cnodejs.android.md.R
import org.cnodejs.android.md.util.MarkdownUtils

class LicenseViewModel(application: Application) : AndroidViewModel(application) {
    private val resources = application.resources

    val contentData = MutableLiveData<String>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val markdown = resources.openRawResource(R.raw.license).bufferedReader().use { it.readText() }
            val html = MarkdownUtils.renderHtml(markdown)
            contentData.postValue(html)
        }
    }
}
