package com.mtw.supplier.ecs.components.item

import com.mtw.supplier.ecs.Component
import kotlinx.serialization.Serializable


@Serializable
class UsableComponent: Component() { override var _parentId: Int? = null }