package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.CollisionComponent
import com.mtw.supplier.ecs.components.DoorComponent
import com.mtw.supplier.ecs.components.EncounterLocationComponent
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.utils.AbsolutePosition
import kotlinx.serialization.Serializable
import org.hexworks.cobalt.core.api.UUID

interface DreamTileI {
    val blocksMovement: Boolean
    val explored: Boolean
    val blocksVision: Boolean
    val entities: List<Entity>
}

interface DreamMapI {
    fun getDreamTileI(pos: AbsolutePosition): DreamTileI?
    fun getAllDreamTileIs(): Map<AbsolutePosition, DreamTileI>
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

class DreamRoomBuilder(
    val width: Int,
    val height: Int,
    val exits: List<ExitDirection> = ExitDirection.ALL_DIRECTIONS
) {
    private val roomUuid: String = UUID.randomUUID().toString()
    private val doors: MutableMap<ExitDirection, Entity> = mutableMapOf()

    private fun doorOrWall(isDoor: Boolean, direction: ExitDirection): Entity {
        return if (isDoor) {
            val door = Entity(UUID.randomUUID().toString(), "Door")
                .addComponent(CollisionComponent.defaultBlocker())
                .addComponent(DoorComponent(direction))
            doors[direction] = door
            door
        } else {
            Entity(UUID.randomUUID().toString(), "Wall")
                .addComponent(CollisionComponent.defaultBlocker())
        }
    }

    private fun buildWalls(room: DreamRoom) {
        // North wall
        val northExitX = if(exits.contains(ExitDirection.NORTH)) { (1 until width - 1).random() } else { null }
        for (x in 0 until width) {
            room.placeEntity(doorOrWall(x == northExitX, ExitDirection.NORTH), RoomPosition(x, height - 1, roomUuid), false)
        }
        // East
        val eastExitY = if(exits.contains(ExitDirection.EAST))  { (1 until height - 1).random() } else { null }
        for (y in 0 until height - 1) {
            room.placeEntity(doorOrWall(y == eastExitY, ExitDirection.EAST), RoomPosition(width - 1, y, roomUuid), false)
        }
        // South
        val southExitX = if(exits.contains(ExitDirection.SOUTH))  { (1 until width - 1).random() } else { null }
        for (x in 0 until width - 1) {
            room.placeEntity(doorOrWall(x == southExitX, ExitDirection.SOUTH), RoomPosition(x, 0, roomUuid), false)
        }
        // West
        val westExitX = if(exits.contains(ExitDirection.WEST))  { (1 until height - 1).random() } else { null }
        for (y in 1 until height - 1) {
            room.placeEntity(doorOrWall(y == westExitX, ExitDirection.WEST), RoomPosition(0, y, roomUuid), false)
        }
    }

    fun build(): DreamRoom {
        val nodes: Array<Array<DreamTile>> = Array(width) { Array(height) { DreamTile() } }
        val room = DreamRoom(roomUuid, width, height, doors, nodes)

        buildWalls(room)

        return room
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
    val width: Int,
    val height: Int,
    val doors: Map<ExitDirection, Entity>,
    private val nodes: Array<Array<DreamTile>>
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
        if (entity.hasComponent(EncounterLocationComponent::class)) {
            throw EntityAlreadyHasLocation("Specified entity ${entity.name} already has a location, cannot be placed!")
        } else if (!ignoreCollision && this.positionBlocked(targetPosition)) {
            throw NodeHasInsufficientSpaceException("Node $targetPosition is full, cannot place ${entity.name}")
        }

        this.nodes[targetPosition.x][targetPosition.y].entities.add(entity)
        entity.addComponent(EncounterLocationComponent(targetPosition, this.uuid))
    }
    class EntityAlreadyHasLocation(message: String): Exception(message)
    class NodeHasInsufficientSpaceException(message: String): Exception(message)

    internal fun removeEntity(entity: Entity) {
        if (!entity.hasComponent(EncounterLocationComponent::class)) {
            throw EntityHasNoLocation("Specified entity ${entity.name} has no location, cannot remove!")
        }

        val locationComponent = entity.getComponent(EncounterLocationComponent::class)
        val (x, y) = locationComponent.roomPosition
        this.nodes[x][y].entities.remove(entity)
        entity.removeComponent(locationComponent)
    }
    class EntityHasNoLocation(message: String): Exception(message)

    internal fun teleportEntity(entity: Entity, targetPosition: RoomPosition, ignoreCollision: Boolean) {
        this.removeEntity(entity)
        this.placeEntity(entity, targetPosition, ignoreCollision)
    }

}