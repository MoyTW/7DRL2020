package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.encounter.rulebook.actions.TerrifyAction
import com.mtw.supplier.encounter.rulebook.actions.TerrorChangeStats
import kotlinx.serialization.Serializable

@Serializable
class InspectEvent(
    val eventHeader: String,
    val eventText: String,
    val terrorChangeStats: TerrorChangeStats?,
    val memory: Memory?
) {
    fun toTerrifyAction(actor: Entity, target: Entity): TerrifyAction? {
        return if (terrorChangeStats != null) {
            TerrifyAction(actor, target, terrorChangeStats)
        } else {
            null
        }
    }
}

@Serializable
class InspectableComponent(
    val defaultInspectHeader: String,
    val defaultInspectText: String,
    private val _inspectEvents: MutableList<InspectEvent> = mutableListOf()
): Component() {
    override var _parentId: String? = null

    val inspectEvents: List<InspectEvent>
        get() = _inspectEvents

    fun completeEvent(inspectEvent: InspectEvent) {
        this._inspectEvents.remove(inspectEvent)
    }
}