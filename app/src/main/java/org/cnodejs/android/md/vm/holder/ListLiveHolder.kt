package org.cnodejs.android.md.vm.holder

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter

abstract class ListLiveHolder<Entity> {
    val entitiesData: MutableLiveData<MutableList<Entity>?> = MutableLiveData()

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
    viewLifecycleOwner: LifecycleOwner,
    adapter: ListAdapter<Entity, *>,
) {
    entitiesData.observe(viewLifecycleOwner) {
        adapter.submitList(it?.let { entities -> ArrayList(entities) } ?: ArrayList())
    }
}
