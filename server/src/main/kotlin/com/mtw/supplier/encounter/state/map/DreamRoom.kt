package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.CollisionComponent
import com.mtw.supplier.ecs.components.DoorComponent
import com.mtw.supplier.ecs.components.EncounterLocationComponent
import com.mtw.supplier.utils.XYCoordinates
import kotlinx.serialization.Serializable
import org.hexworks.cobalt.core.api.UUID

interface EncounterTileView {
    val blocksMovement: Boolean
    val explored: Boolean
    val blocksVision: Boolean
    val entities: List<Entity>
}

interface EncounterTileMapView {
    val width: Int
    val height: Int
    fun getTileView(x: Int, y: Int): EncounterTileView?
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
//    val map: DreamMap,
    val width: Int,
    val height: Int,
    val exits: List<ExitDirection> = ExitDirection.ALL_DIRECTIONS
) {
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
            room.placeEntity(doorOrWall(x == northExitX, ExitDirection.NORTH), XYCoordinates(x, height - 1), false)
        }
        // East
        val eastExitY = if(exits.contains(ExitDirection.EAST))  { (1 until height - 1).random() } else { null }
        for (y in 0 until height - 1) {
            room.placeEntity(doorOrWall(y == eastExitY, ExitDirection.EAST), XYCoordinates(width - 1, y), false)
        }
        // South
        val southExitX = if(exits.contains(ExitDirection.SOUTH))  { (1 until width - 1).random() } else { null }
        for (x in 0 until width - 1) {
            room.placeEntity(doorOrWall(x == southExitX, ExitDirection.SOUTH), XYCoordinates(x, 0), false)
        }
        // West
        val westExitX = if(exits.contains(ExitDirection.WEST))  { (1 until height - 1).random() } else { null }
        for (y in 1 until height - 1) {
            room.placeEntity(doorOrWall(y == westExitX, ExitDirection.WEST), XYCoordinates(0, y), false)
        }
    }

    fun build(): DreamRoom {
        val nodes: Array<Array<DreamTile>> = Array(width) { Array(height) { DreamTile() } }
        val room = DreamRoom(UUID.randomUUID().toString(), width, height, doors, nodes)

        buildWalls(room)

        return room
    }
}

@Serializable
class DreamRoom internal constructor(
    val uuid: String,
    override val width: Int,
    override val height: Int,
    val doors: Map<ExitDirection, Entity>,
    private val nodes: Array<Array<DreamTile>>
): EncounterTileMapView {
    override fun getTileView(x: Int, y: Int): EncounterTileView? {
        // yeah, yeah, exceptions, control flow, you could do a width/height. TODO: cleanup maybe
        return try {
            nodes[x][y]
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    internal fun isInBounds(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    internal fun getDoor(direction: ExitDirection): Entity? {
        return doors[direction]
    }

    internal fun markExplored(pos: XYCoordinates) {
        nodes[pos.x][pos.y].markExplored()
    }

    internal fun positionBlocked(pos: XYCoordinates): Boolean {
        if (!isInBounds(pos.x, pos.y)) { return true }
        return nodes[pos.x][pos.y].blocksMovement
    }

    internal fun arePositionsAdjacent(pos1: XYCoordinates, pos2: XYCoordinates): Boolean {
        val dx = kotlin.math.abs(pos1.x - pos2.x)
        val dy = kotlin.math.abs(pos1.y - pos2.y)
        val adjacent = dx < 2 && dy < 2 && (dx + dy != 0)
        return adjacent
    }

    internal fun adjacentUnblockedPositions(pos: XYCoordinates): List<XYCoordinates> {
        val adjacentUnblockedPositions = mutableListOf<XYCoordinates>()
        for(x in (pos.x - 1..pos.x + 1)) {
            for (y in (pos.y - 1..pos.y + 1)) {
                if (x != y && isInBounds(x, y) && !nodes[x][y].blocksMovement) {
                    adjacentUnblockedPositions.add(XYCoordinates(x, y))
                }
            }
        }
        return adjacentUnblockedPositions
    }

    // TODO: A more cogent sorting function than creation order?
    internal fun entities(): List<Entity> {
        return this.nodes.flatten().flatMap { it.entities }.sortedBy { it.id }
    }

    internal fun getEntitiesAtPosition(pos: XYCoordinates): List<Entity> {
        if (!isInBounds(pos.x, pos.y)) { return emptyList() }
        return this.nodes[pos.x][pos.y].entities
    }

    /**
     * @throws EntityAlreadyHasLocation when a node already has a location
     * @throws NodeHasInsufficientSpaceException when node cannot find space for the entity
     */
    internal fun placeEntity(entity: Entity, targetPosition: XYCoordinates, ignoreCollision: Boolean) {
        if (entity.hasComponent(EncounterLocationComponent::class)) {
            throw EntityAlreadyHasLocation("Specified entity ${entity.name} already has a location, cannot be placed!")
        } else if (!ignoreCollision && this.positionBlocked(targetPosition)) {
            throw NodeHasInsufficientSpaceException("Node $targetPosition is full, cannot place ${entity.name}")
        }

        this.nodes[targetPosition.x][targetPosition.y].entities.add(entity)
        entity.addComponent(EncounterLocationComponent(targetPosition))
    }
    class EntityAlreadyHasLocation(message: String): Exception(message)
    class NodeHasInsufficientSpaceException(message: String): Exception(message)

    internal fun removeEntity(entity: Entity) {
        if (!entity.hasComponent(EncounterLocationComponent::class)) {
            throw EntityHasNoLocation("Specified entity ${entity.name} has no location, cannot remove!")
        }

        val locationComponent = entity.getComponent(EncounterLocationComponent::class)
        val (x, y) = locationComponent.position
        this.nodes[x][y].entities.remove(entity)
        entity.removeComponent(locationComponent)
    }
    class EntityHasNoLocation(message: String): Exception(message)

    internal fun teleportEntity(entity: Entity, targetPosition: XYCoordinates, ignoreCollision: Boolean) {
        this.removeEntity(entity)
        this.placeEntity(entity, targetPosition, ignoreCollision)
    }

}