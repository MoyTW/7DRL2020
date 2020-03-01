package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.encounter.state.map.ExitDirection
import kotlinx.serialization.Serializable

@Serializable
class DoorComponent(val direction: ExitDirection): Component() {
    override var _parentId: String? = null
}