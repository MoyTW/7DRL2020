package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.encounter.state.EncounterState
import kotlinx.serialization.Serializable

@Serializable
class FactionComponent(var factionId: Int, override var _parentId: Int? = null) : Component() {
    fun isHostileTo(otherEntityId: Int, encounterState: EncounterState): Boolean {
        val parentEntity = encounterState.getEntity(this.parentId)
        val otherEntity = encounterState.getEntity(otherEntityId)

        return parentEntity.getComponent(FactionComponent::class).factionId != otherEntity.getComponent(FactionComponent::class).factionId
    }
}
