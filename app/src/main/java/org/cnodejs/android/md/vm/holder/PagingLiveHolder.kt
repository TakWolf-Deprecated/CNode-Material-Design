package org.cnodejs.android.md.vm.holder

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ListAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.takwolf.android.hfrecyclerview.loadmorefooter.LoadMoreFooter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class PagingLiveHolder<Entity, PagingParams>(
    private val viewModel: ViewModel,
    private val toastHolder: ToastLiveHolder,
) : ListLiveHolder<Entity>() {
    val refreshStateData = MutableLiveData(false)
    val loadMoreStateData = MutableLiveData(LoadMoreFooter.STATE_DISABLED)

    private var dataVersion = 0
    private var isRefreshDoing = false
    private var isLoadMoreDoing = false
    private var pagingParams: PagingParams? = null
    private var isFinished = false

    fun refresh() {
        if (!isRefreshDoing) {
            val version = dataVersion
            refreshStateData.value = true
            isRefreshDoing = true
            viewModel.viewModelScope.launch(Dispatchers.IO) {
                doRefresh(version)
            }
        }
    }

    protected abstract suspend fun doRefresh(version: Int)

    protected fun refreshSuccess(version: Int, entities: List<Entity>, pagingParams: PagingParams, isFinished: Boolean) {
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            if (dataVersion == version) {
                dataVersion += 1
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
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            if (dataVersion == version) {
                isRefreshDoing = false
                refreshStateData.value = false
                toastHolder.showToast(message)
            }
        }
    }

    fun loadMore() {
        if (!isLoadMoreDoing && !isFinished) {
            val version = dataVersion
            loadMoreStateData.value = LoadMoreFooter.STATE_LOADING
            isLoadMoreDoing = true
            viewModel.viewModelScope.launch(Dispatchers.IO) {
                doLoadMore(version, pagingParams!!)
            }
        }
    }

    protected abstract suspend fun doLoadMore(version: Int, pagingParams: PagingParams)

    protected fun loadMoreSuccess(version: Int, addedEntities: List<Entity>, pagingParams: PagingParams, isFinished: Boolean) {
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            if (dataVersion == version) {
                appendList(addedEntities)
                this@PagingLiveHolder.pagingParams = pagingParams
                this@PagingLiveHolder.isFinished = isFinished
                isLoadMoreDoing = false
                loadMoreStateData.value = if (isFinished) LoadMoreFooter.STATE_FINISHED else LoadMoreFooter.STATE_ENDLESS
            }
        }
    }

    protected fun loadMoreFailure(version: Int, message: String) {
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            if (dataVersion == version) {
                isLoadMoreDoing = false
                loadMoreStateData.value = LoadMoreFooter.STATE_FAILED
                toastHolder.showToast(message)
            }
        }
    }

    fun resetPaging() {
        dataVersion += 1
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
    owner: LifecycleOwner,
    refreshLayout: SwipeRefreshLayout,
    loadMoreFooter: LoadMoreFooter,
    adapter: ListAdapter<Entity, *>,
) {
    refreshLayout.setOnRefreshListener {
        refresh()
    }
    loadMoreFooter.setOnLoadMoreListener {
        loadMore()
    }
    setupView(owner, adapter)
    refreshStateData.observe(owner) {
        it?.let { isRefreshing ->
            refreshLayout.isRefreshing = isRefreshing
        }
    }
    loadMoreStateData.observe(owner) {
        it?.let { state ->
            loadMoreFooter.state = state
        }
    }
}
