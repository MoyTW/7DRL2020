package com.mtw.supplier.ecs.components.item

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.ecs.Entity
import kotlinx.serialization.Serializable

@Serializable
class InventoryComponent(
    val size: Int = 26
): Component() {
    override var _parentId: Int? = null

    private val contents: MutableList<Entity> = mutableListOf()

    /**
     * @throws InventoryFullException when the inventory is already full
     * @throws NotCarryableException when the entity has no carryable component
     */
    fun addItem(carryable: Entity) {
        if (contents.size >= size) {
            throw InventoryFullException("Cannot carry ${carryable.name} because inventory is already full!")
        }

        val carryableComponent = carryable.getComponentOrNull(CarryableComponent::class)
        if (carryableComponent == null) {
            throw NotCarryableException("Cannot carry entity ${carryable.name} id=${carryable.id}")
        } else {
            this.contents.add(carryable)
        }
    }
    class InventoryFullException(message: String): Exception(message)
    class NotCarryableException(message: String): Exception(message)

    fun removeItem(carryable: Entity) {
        contents.remove(carryable)
    }
}
