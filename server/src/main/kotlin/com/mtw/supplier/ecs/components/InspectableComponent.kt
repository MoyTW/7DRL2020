package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.encounter.rulebook.actions.TerrifyAction
import kotlinx.serialization.Serializable

@Serializable
class InspectEvent(
    val eventHeader: String,
    val eventText: String,
    val terrorDiff: Int?,
    val memory: Memory?
) {}

@Serializable
class InspectableComponent(
    val inspectEvents: List<InspectEvent>,
    val defaultInspectHeader: String,
    val defaultInspectText: String
): Component() {
    override var _parentId: String? = null
}