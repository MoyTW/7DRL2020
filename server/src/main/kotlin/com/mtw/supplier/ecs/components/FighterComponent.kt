package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import kotlinx.serialization.Serializable

@Serializable
class FighterComponent(
    var hitDamage: Int,
    var toHit: Int,
    var toDodge: Int,
    override var _parentId: Int? = null
): Component()