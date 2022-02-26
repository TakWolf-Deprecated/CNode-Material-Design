package org.cnodejs.android.md.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.cnodejs.android.md.BuildConfig
import org.cnodejs.android.md.R
import org.cnodejs.android.md.model.entity.ErrorResult
import org.cnodejs.android.md.util.notifyDataChanged
import org.cnodejs.android.md.vm.holder.ILoadingViewModel
import org.cnodejs.android.md.vm.holder.IToastViewModel
import org.cnodejs.android.md.vm.holder.LoadingLiveHolder
import org.cnodejs.android.md.vm.holder.ToastLiveHolder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class LoginByGithubViewModel(application: Application) : AndroidViewModel(application), IToastViewModel, ILoadingViewModel {
    companion object {
        private const val TAG = "LoginByGithubViewModel"
    }

    private val resources = application.resources

    override val toastHolder = ToastLiveHolder()
    override val loadingHolder = LoadingLiveHolder()
    val accessTokenEvent = LiveEvent<String>()

    fun fetchAccessToken(cookie: String) {
        loadingHolder.showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect("https://cnodejs.org/setting").header("Cookie", cookie).get()
                val accessToken = doc.getElementById("accessToken")?.text()
                withContext(Dispatchers.Main) {
                    loadingHolder.hideLoading()
                    accessToken?.let {
                        accessTokenEvent.value = it
                    } ?: run {
                        toastHolder.showToast(resources.getString(R.string.access_token_fetch_error))
                    }
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "fetchAccessToken", e)
                }
                withContext(Dispatchers.Main) {
                    loadingHolder.hideLoading()
                    toastHolder.showToast(resources.getString(R.string.access_token_fetch_error))
                }
            }
        }
    }
}
