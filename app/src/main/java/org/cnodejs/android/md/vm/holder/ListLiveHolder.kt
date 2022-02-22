package org.cnodejs.android.md.vm.holder

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter

open class ListLiveHolder<Entity>(entities: List<Entity>? = null) {
    val entitiesData: MutableLiveData<List<Entity>?> = MutableLiveData(entities?.toList())

    fun getList(): List<Entity> {
        return entitiesData.value?.toList() ?: listOf()
    }

    fun setList(entities: List<Entity>) {
        entitiesData.value = entities.toList()
    }

    fun appendList(addedEntities: List<Entity>) {
        entitiesData.value = (entitiesData.value?.toMutableList() ?: mutableListOf()).apply {
            addAll(addedEntities)
        }
    }

    fun clearList() {
        entitiesData.value = null
    }
}

fun <Entity> ListLiveHolder<Entity>.setupView(
    owner: LifecycleOwner,
    adapter: ListAdapter<Entity, *>,
) {
    entitiesData.observe(owner) { entities ->
        adapter.submitList(entities?.toList())
    }
}
