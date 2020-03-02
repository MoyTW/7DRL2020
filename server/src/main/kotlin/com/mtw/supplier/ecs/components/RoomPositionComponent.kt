package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.encounter.state.map.RoomPosition
import com.mtw.supplier.utils.AbsolutePosition
import kotlinx.serialization.Serializable

@Serializable
class RoomPositionComponent(var roomPosition: RoomPosition, var roomUuid: String): Component() {
    override var _parentId: String? = null

    fun asAbsolutePosition(encounterState: EncounterState): AbsolutePosition? {
        return encounterState.roomToAbsolutePosition(roomPosition)
    }
}