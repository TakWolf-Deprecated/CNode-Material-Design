package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.takwolf.android.hfrecyclerview.loadmorefooter.LoadMoreFooter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class PagingViewModel<Entity, PagingParams>(application: Application) : ListViewModel<Entity>(application) {
    val refreshStateData = MutableLiveData(false)
    val loadMoreStateData = MutableLiveData(LoadMoreFooter.STATE_DISABLED)
    private var isRefreshDoing = false
    private var isLoadMoreDoing = false
    private var version = 0
    private var pagingParams: PagingParams? = null
    private var isFinished = false

    fun refresh() {
        if (!isRefreshDoing) {
            refreshStateData.value = true
            isRefreshDoing = true
            viewModelScope.launch(Dispatchers.IO) {
                doRefresh()
            }
        }
    }

    abstract suspend fun doRefresh()

    protected fun refreshSuccess(entities: List<Entity>, pagingParams: PagingParams, isFinished: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            version += 1
            entitiesData.value = ArrayList(entities)
            this@PagingViewModel.pagingParams = pagingParams
            this@PagingViewModel.isFinished = isFinished
            isRefreshDoing = false
            refreshStateData.value = false
            isLoadMoreDoing = false
            loadMoreStateData.value = if (isFinished) LoadMoreFooter.STATE_FINISHED else LoadMoreFooter.STATE_ENDLESS
        }
    }

    protected fun refreshFailure() {
        viewModelScope.launch(Dispatchers.Main) {
            isRefreshDoing = false
            refreshStateData.value = false
        }
    }

    fun loadMore() {
        if (!isLoadMoreDoing && !isFinished) {
            loadMoreStateData.value = LoadMoreFooter.STATE_LOADING
            isLoadMoreDoing = true
            viewModelScope.launch(Dispatchers.IO) {
                doLoadMore(version, pagingParams!!)
            }
        }
    }

    abstract suspend fun doLoadMore(version: Int, pagingParams: PagingParams)

    protected fun loadMoreSuccess(version: Int, addedEntities: List<Entity>, pagingParams: PagingParams, isFinished: Boolean) {
        if (this.version == version) {
            viewModelScope.launch(Dispatchers.Main) {
                entitiesData.value?.let { entities ->
                    entities.addAll(addedEntities)
                    entitiesData.value = entities
                }
                this@PagingViewModel.pagingParams = pagingParams
                this@PagingViewModel.isFinished = isFinished
                isLoadMoreDoing = false
                loadMoreStateData.value = if (isFinished) LoadMoreFooter.STATE_FINISHED else LoadMoreFooter.STATE_ENDLESS
            }
        }
    }

    protected fun loadMoreFailure(version: Int) {
        if (this.version == version) {
            viewModelScope.launch(Dispatchers.Main) {
                isLoadMoreDoing = false
                loadMoreStateData.value = LoadMoreFooter.STATE_FAILED
            }
        }
    }
}
