package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import kotlinx.serialization.Serializable

@Serializable
class CollisionComponent(
    var blocksMovement: Boolean,
    var blocksVision: Boolean,
    var attackOnHit: Boolean,
    var selfDestructOnHit: Boolean,
    override var _parentId: String? = null
): Component() {
    companion object {
        fun mover(): CollisionComponent = CollisionComponent(
            blocksMovement = true,
            blocksVision = false,
            attackOnHit = false,
            selfDestructOnHit = false
        )
        fun blocker(): CollisionComponent = CollisionComponent(
            blocksMovement = true,
            blocksVision = true,
            attackOnHit = false,
            selfDestructOnHit = false
        )
        fun passable(): CollisionComponent = CollisionComponent(
            blocksMovement = false,
            blocksVision = false,
            attackOnHit = false,
            selfDestructOnHit = false
        )
        fun fog(): CollisionComponent = CollisionComponent(
            blocksMovement = false,
            blocksVision = true,
            attackOnHit = false,
            selfDestructOnHit = false
        )
    }
}