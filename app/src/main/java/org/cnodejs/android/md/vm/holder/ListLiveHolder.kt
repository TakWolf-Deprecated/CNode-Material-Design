package org.cnodejs.android.md.vm.holder

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter

open class ListLiveHolder<Entity>(entities: List<Entity>? = null) {
    val entitiesData: MutableLiveData<MutableList<Entity>?> = MutableLiveData(entities?.let { ArrayList(it) })

    fun getList(): List<Entity> {
        return entitiesData.value?.let { entities -> ArrayList(entities) } ?: ArrayList()
    }

    fun setList(entities: List<Entity>) {
        entitiesData.value = ArrayList(entities)
    }

    fun appendList(addedEntities: List<Entity>) {
        val entities = entitiesData.value ?: ArrayList()
        entities.addAll(addedEntities)
        entitiesData.value = entities
    }

    fun clearList() {
        entitiesData.value = null
    }
}

fun <Entity> ListLiveHolder<Entity>.setupView(
    owner: LifecycleOwner,
    adapter: ListAdapter<Entity, *>,
) {
    entitiesData.observe(owner) {
        adapter.submitList(it?.let { entities -> ArrayList(entities) } ?: ArrayList())
    }
}
