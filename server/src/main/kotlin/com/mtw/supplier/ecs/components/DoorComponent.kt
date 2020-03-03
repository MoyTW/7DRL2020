package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.encounter.state.map.ExitDirection
import kotlinx.serialization.Serializable

@Serializable
class DoorComponent(val direction: ExitDirection, var closed: Boolean = true): Component() {
    override var _parentId: String? = null

    fun toggleOpen(parent: Entity) {
        val collisionComponent = parent.getComponent(CollisionComponent::class)
        if (this.closed) {
            collisionComponent.blocksMovement = false
            collisionComponent.blocksVision = false
            this.closed = false
        } else {
            collisionComponent.blocksMovement = true
            collisionComponent.blocksVision = true
            this.closed = true
        }
    }
}