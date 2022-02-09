package org.cnodejs.android.md.vm.holder

import androidx.lifecycle.MutableLiveData

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
