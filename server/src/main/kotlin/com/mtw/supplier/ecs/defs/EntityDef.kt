package com.mtw.supplier.ecs.defs

import com.mtw.supplier.ecs.Entity
import kotlinx.serialization.Serializable

@Serializable
class EntityDef(
    val componentDefs: List<ComponentDef>
) {
    fun buildEntity(id: Int?, name: String): Entity {
        if (id == null) {
            TODO("Auto-increment entity IDs aren't a thing yet")
        }
        val entity = Entity(id, name)
        for (componentDef in componentDefs) {
            entity.addComponent(componentDef.buildComponent())
        }
        return entity
    }
}