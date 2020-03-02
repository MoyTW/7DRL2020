package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.encounter.state.map.RoomPosition
import com.mtw.supplier.utils.XYCoordinates
import kotlinx.serialization.Serializable

@Serializable
class EncounterLocationComponent(var roomPosition: RoomPosition, var roomUuid: String): Component() {
    override var _parentId: String? = null
}