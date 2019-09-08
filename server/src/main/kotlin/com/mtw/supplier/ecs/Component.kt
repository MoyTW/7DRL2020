package com.mtw.supplier.ecs

import com.mtw.supplier.encounter.state.EncounterState
import kotlinx.serialization.Serializable
import java.lang.UnsupportedOperationException

@Serializable
abstract class Component {
    abstract var _parentId: Int?

    val parentId: Int
        get() = _parentId!!

    fun getParent(encounterState: EncounterState): Entity {
        return encounterState.getEntity(this.parentId)
    }

    internal fun notifyAdded(parentId: Int) {
        if (this._parentId != null) {
            throw UnsupportedOperationException("You can't double-add a Component!")
        }

        this._parentId = parentId
    }

    internal fun notifyRemoved() {
        if (this._parentId == null) {
            throw UnsupportedOperationException("You can't remove a component that's never been added!")
        }

        this._parentId = null
    }
}