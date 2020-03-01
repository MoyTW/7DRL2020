package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import kotlinx.serialization.Serializable

@Serializable
class DoorComponent(): Component() {
    override var _parentId: String? = null
}