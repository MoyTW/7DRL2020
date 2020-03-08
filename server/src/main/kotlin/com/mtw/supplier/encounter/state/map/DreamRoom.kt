package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.RoomPositionComponent
import com.mtw.supplier.encounter.state.map.blueprint.RoomTags
import kotlinx.serialization.Serializable

interface DreamTileI {
    val blocksMovement: Boolean
    val explored: Boolean
    val blocksVision: Boolean
    val entities: List<Entity>
}

enum class ExitDirection {
    NORTH, EAST, SOUTH, WEST;

    fun opposite(): ExitDirection {
        return when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }
    }

    companion object {
        val ALL_DIRECTIONS = listOf(NORTH, EAST, SOUTH, WEST)
    }
}

@Serializable
data class RoomPosition(
    val x: Int,
    val y: Int,
    val roomUuid: String
)

@Serializable
class DreamRoom internal constructor(
    val uuid: String,
    val name: String,
    val commentary: String,
    val width: Int,
    val height: Int,
    val doors: Map<ExitDirection, Entity>,
    private val nodes: Array<Array<DreamTile>>,
    val tags: List<RoomTags>
) {
    internal fun isInBounds(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    internal fun getDoor(direction: ExitDirection): Entity? {
        return doors[direction]
    }

    internal fun getTile(pos: RoomPosition): DreamTile? {
        if (!isInBounds(pos.x, pos.y)) return null
        return nodes[pos.x][pos.y]
    }

    internal fun allTiles(): Map<RoomPosition, DreamTile> {
        val acc: MutableMap<RoomPosition, DreamTile> = mutableMapOf()
        for (x in 0 until width) {
            for (y in 0 until height) {
                acc[RoomPosition(x, y, this.uuid)] = nodes[x][y]
            }
        }
        return acc
    }

    internal fun markExplored(pos: RoomPosition) {
        nodes[pos.x][pos.y].markExplored()
    }

    internal fun positionBlocked(pos: RoomPosition): Boolean {
        if (!isInBounds(pos.x, pos.y)) { return true }
        return nodes[pos.x][pos.y].blocksMovement
    }

    internal fun randomPlacementPosition(): RoomPosition? {
        return this.allTiles().filter { !it.value.blocksMovement && it.value.entities.isEmpty() }
            .map { it.key }
            .random()
    }

    internal fun adjacentUnblockedPositions(pos: RoomPosition): List<RoomPosition> {
        val adjacentUnblockedPositions = mutableListOf<RoomPosition>()
        for(x in (pos.x - 1..pos.x + 1)) {
            for (y in (pos.y - 1..pos.y + 1)) {
                if (x != y && isInBounds(x, y) && !nodes[x][y].blocksMovement) {
                    adjacentUnblockedPositions.add(RoomPosition(x, y, this.uuid))
                }
            }
        }
        return adjacentUnblockedPositions
    }

    // TODO: A more cogent sorting function than creation order?
    internal fun entities(): List<Entity> {
        return this.nodes.flatten().flatMap { it.entities }.sortedBy { it.id }
    }

    internal fun getEntitiesAtPosition(pos: RoomPosition): List<Entity> {
        if (!isInBounds(pos.x, pos.y)) { return emptyList() }
        return this.nodes[pos.x][pos.y].entities
    }

    /**
     * @throws EntityAlreadyHasLocation when a node already has a location
     * @throws NodeHasInsufficientSpaceException when node cannot find space for the entity
     */
    internal fun placeEntity(entity: Entity, targetPosition: RoomPosition, ignoreCollision: Boolean) {
        if (entity.hasComponent(RoomPositionComponent::class)) {
            throw EntityAlreadyHasLocation("Specified entity ${entity.name} already has a location, cannot be placed!")
        } else if (!ignoreCollision && this.positionBlocked(targetPosition)) {
            throw NodeHasInsufficientSpaceException("Node $targetPosition is full, cannot place ${entity.name}")
        }

        this.nodes[targetPosition.x][targetPosition.y].entities.add(entity)
        entity.addComponent(RoomPositionComponent(targetPosition, this.uuid))
    }
    class EntityAlreadyHasLocation(message: String): Exception(message)
    class NodeHasInsufficientSpaceException(message: String): Exception(message)

    internal fun removeEntity(entity: Entity) {
        if (!entity.hasComponent(RoomPositionComponent::class)) {
            throw EntityHasNoLocation("Specified entity ${entity.name} has no location, cannot remove!")
        }

        val locationComponent = entity.getComponent(RoomPositionComponent::class)
        val (x, y) = locationComponent.roomPosition
        this.nodes[x][y].entities.remove(entity)
        entity.removeComponent(locationComponent)
    }
    class EntityHasNoLocation(message: String): Exception(message)
}