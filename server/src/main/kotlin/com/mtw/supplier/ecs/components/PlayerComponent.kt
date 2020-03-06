package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import kotlinx.serialization.Serializable

@Serializable
class PlayerComponent: Component() {
    override var _parentId: String? = null
}