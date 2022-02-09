package org.cnodejs.android.md.vm.holder

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.takwolf.android.hfrecyclerview.loadmorefooter.LoadMoreFooter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class PagingLiveHolder<Entity, PagingParams>(
    private val viewModelScope: CoroutineScope,
    val baseLiveHolder: BaseLiveHolder,
) : ListLiveHolder<Entity>() {
    val refreshStateData = MutableLiveData(false)
    val loadMoreStateData = MutableLiveData(LoadMoreFooter.STATE_DISABLED)
    private var isRefreshDoing = false
    private var isLoadMoreDoing = false
    private var refreshVersion = 0
    private var loadMoreVersion = 0
    private var pagingParams: PagingParams? = null
    private var isFinished = false

    fun refresh() {
        if (!isRefreshDoing) {
            refreshVersion += 1
            refreshStateData.value = true
            isRefreshDoing = true
            viewModelScope.launch(Dispatchers.IO) {
                doRefresh(refreshVersion)
            }
        }
    }

    protected abstract suspend fun doRefresh(version: Int)

    protected fun refreshSuccess(version: Int, entities: List<Entity>, pagingParams: PagingParams, isFinished: Boolean) {
        if (refreshVersion == version) {
            viewModelScope.launch(Dispatchers.Main) {
                loadMoreVersion += 1
                setList(entities)
                this@PagingLiveHolder.pagingParams = pagingParams
                this@PagingLiveHolder.isFinished = isFinished
                isRefreshDoing = false
                refreshStateData.value = false
                isLoadMoreDoing = false
                loadMoreStateData.value = if (isFinished) LoadMoreFooter.STATE_FINISHED else LoadMoreFooter.STATE_ENDLESS
            }
        }
    }

    protected fun refreshFailure(version: Int, message: String) {
        if (refreshVersion == version) {
            viewModelScope.launch(Dispatchers.Main) {
                isRefreshDoing = false
                refreshStateData.value = false
                baseLiveHolder.showToast(message)
            }
        }
    }

    fun loadMore() {
        if (!isLoadMoreDoing && !isFinished) {
            loadMoreStateData.value = LoadMoreFooter.STATE_LOADING
            isLoadMoreDoing = true
            viewModelScope.launch(Dispatchers.IO) {
                doLoadMore(loadMoreVersion, pagingParams!!)
            }
        }
    }

    protected abstract suspend fun doLoadMore(version: Int, pagingParams: PagingParams)

    protected fun loadMoreSuccess(version: Int, addedEntities: List<Entity>, pagingParams: PagingParams, isFinished: Boolean) {
        if (loadMoreVersion == version) {
            viewModelScope.launch(Dispatchers.Main) {
                appendList(addedEntities)
                this@PagingLiveHolder.pagingParams = pagingParams
                this@PagingLiveHolder.isFinished = isFinished
                isLoadMoreDoing = false
                loadMoreStateData.value = if (isFinished) LoadMoreFooter.STATE_FINISHED else LoadMoreFooter.STATE_ENDLESS
            }
        }
    }

    protected fun loadMoreFailure(version: Int, message: String) {
        if (loadMoreVersion == version) {
            viewModelScope.launch(Dispatchers.Main) {
                isLoadMoreDoing = false
                loadMoreStateData.value = LoadMoreFooter.STATE_FAILED
                baseLiveHolder.showToast(message)
            }
        }
    }

    fun resetPaging() {
        refreshVersion += 1
        loadMoreVersion += 1
        isRefreshDoing = false
        isLoadMoreDoing = false
        pagingParams = null
        isFinished = false
        refreshStateData.value = false
        loadMoreStateData.value = LoadMoreFooter.STATE_DISABLED
        clearList()
    }
}

fun <Entity> PagingLiveHolder<Entity, *>.setupView(
    viewLifecycleOwner: LifecycleOwner,
    adapter: ListAdapter<Entity, *>,
    refreshLayout: SwipeRefreshLayout,
    loadMoreFooter: LoadMoreFooter,
) {
    setupView(viewLifecycleOwner, adapter)
    refreshStateData.observe(viewLifecycleOwner) {
        it?.let { isRefreshing ->
            refreshLayout.isRefreshing = isRefreshing
        }
    }
    loadMoreStateData.observe(viewLifecycleOwner) {
        it?.let { state ->
            loadMoreFooter.state = state
        }
    }
    refreshLayout.setOnRefreshListener {
        refresh()
    }
    loadMoreFooter.setOnLoadMoreListener {
        loadMore()
    }
}
