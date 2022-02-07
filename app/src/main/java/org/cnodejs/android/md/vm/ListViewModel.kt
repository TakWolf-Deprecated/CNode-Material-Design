package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

abstract class ListViewModel<Entity>(application: Application) : AndroidViewModel(application) {
    val entitiesData: MutableLiveData<MutableList<Entity>> = MutableLiveData()
}
