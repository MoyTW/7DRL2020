package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.CollisionComponent
import kotlinx.serialization.Serializable

@Serializable
internal class DreamTile(
    // Whether or not the node itself is passable
    private var _explored: Boolean = false,
    var terrainBlocksMovement: Boolean = false,
    var terrainBlocksVision: Boolean = false,
    override val entities: MutableList<Entity> = mutableListOf()
): EncounterTileView {

    override val blocksMovement: Boolean
        get() = terrainBlocksMovement ||
            entities.any{ it.getComponentOrNull(CollisionComponent::class)?.blocksMovement ?: false }

    override val explored: Boolean
        get() = _explored

    override val blocksVision: Boolean
        get() = terrainBlocksVision ||
            entities.any{ it.getComponentOrNull(CollisionComponent::class)?.blocksVision ?: false }

    fun markExplored() {
        this._explored = true
    }
}