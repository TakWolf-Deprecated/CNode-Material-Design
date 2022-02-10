package org.cnodejs.android.md.vm.holder

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import org.cnodejs.android.md.BuildConfig
import org.cnodejs.android.md.model.api.CNodeClient
import org.cnodejs.android.md.model.entity.ErrorResult
import org.cnodejs.android.md.model.entity.Tab
import org.cnodejs.android.md.model.entity.TopicWithSummary

class TopicPagingLiveHolder(
    viewModelScope: CoroutineScope,
    baseLiveHolder: BaseLiveHolder,
) : PagingLiveHolder<TopicWithSummary, Int>(
    viewModelScope,
    baseLiveHolder,
) {
    companion object {
        private const val TAG = "TopicPagingLiveHolder"
    }

    val tabData = MutableLiveData(Tab.ALL)

    init {
        refresh()
    }

    fun getTab(): Tab {
        return tabData.value ?: Tab.ALL
    }

    fun switchTab(tab: Tab) {
        if (getTab() != tab) {
            tabData.value = tab
            resetPaging()
            refresh()
        }
    }

    override suspend fun doRefresh(version: Int) {
        try {
            val result = CNodeClient.api.getTopics(getTab().queryValue, mdrender = true)
            val topics = TopicWithSummary.fromList(result.data)
            refreshSuccess(version, topics, 1, topics.isEmpty())
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "doRefresh", e)
            }
            val errorResult = ErrorResult.from(e)
            refreshFailure(version, errorResult.message)
        }
    }

    override suspend fun doLoadMore(version: Int, pagingParams: Int) {
        try {
            val nextPage = pagingParams + 1
            val result = CNodeClient.api.getTopics(getTab().queryValue, nextPage, mdrender = true)
            val topics = TopicWithSummary.fromList(result.data)
            loadMoreSuccess(version, topics, nextPage, topics.isEmpty())
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "doLoadMore", e)
            }
            val errorResult = ErrorResult.from(e)
            refreshFailure(version, errorResult.message)
        }
    }
}
