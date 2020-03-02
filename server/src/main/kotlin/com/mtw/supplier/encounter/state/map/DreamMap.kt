package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.utils.AbsolutePosition
import kotlinx.serialization.Serializable

/** You can only have MAX 5 visible rooms
 *        ROOM
 *         |
 * ROOM - ROOM - ROOM
 *         |
 *        ROOM
 */

@Serializable
class DreamMap: DreamMapI {
    override fun getDreamTileI(x: Int, y: Int): DreamTileI? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllDreamTileIs(): Map<AbsolutePosition, DreamTileI> {
        // For each map, get all tiles
        // For each tile, convert to absolute position
        // Dump them out
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val entities: List<Entity>
        get() = roomsById.flatMap { it.value.entities() }

    private val roomsById: MutableMap<String, DreamRoom> = mutableMapOf()
    // You'll have to remember to link/unlink both ways!
    private val roomGraph: MutableMap<String, MutableMap<ExitDirection, String>> = mutableMapOf()

    internal fun connectRooms(existingRoom: DreamRoom, exitDirection: ExitDirection, newRoom: DreamRoom) {
        if (roomGraph[existingRoom.uuid]?.get(exitDirection) != null) {
            throw RuntimeException("COULD NOT LINK: $existingRoom already had an exit link in $exitDirection")
        }
        if (roomGraph[newRoom.uuid]?.get(exitDirection.opposite()) != null) {
            throw RuntimeException("COULD NOT LINK: $newRoom already had an exit link in ${exitDirection.opposite()}")
        }

        roomGraph[existingRoom.uuid]?.set(exitDirection, newRoom.uuid)
        roomGraph[newRoom.uuid]?.set(exitDirection.opposite(), existingRoom.uuid)
    }

    private fun getConnectedRoomByDirection(room: DreamRoom, direction: ExitDirection): DreamRoom? {
        val uuid = roomGraph[room.uuid]!![direction]
        return roomsById[uuid]
    }

    fun markExplored(pos: AbsolutePosition) {
        TODO()
    }

    /******************************************************************************************************************
     * Entity Management
     ******************************************************************************************************************/

    internal fun absoluteToRoomPosition(absolute: AbsolutePosition): RoomPosition? {
        // Get the room overlapping that position
        // Convert to room coordinates
        TODO()
    }

    internal fun roomToAbsolutePosition(roomPosition: RoomPosition): AbsolutePosition {
        TODO()
    }

    internal fun getEntitiesAtPosition(pos: AbsolutePosition): List<Entity> {
        TODO()
    }

    internal fun positionBlocked(pos: AbsolutePosition): Boolean {
        TODO()
    }

    internal fun arePositionsAdjacent(pos1: AbsolutePosition, pos2: AbsolutePosition): Boolean {
        val dx = kotlin.math.abs(pos1.x - pos2.x)
        val dy = kotlin.math.abs(pos1.y - pos2.y)
        return dx < 2 && dy < 2 && (dx + dy != 0)
    }

    internal fun adjacentUnblockedPositions(pos: AbsolutePosition): List<AbsolutePosition> {
        TODO()
    }

    internal fun placeEntity(entity: Entity, targetPosition: AbsolutePosition, ignoreCollision: Boolean) {
        TODO()
    }

    internal fun removeEntity(entity: Entity) {
        TODO()
    }

    internal fun teleportEntity(entity: Entity, targetPosition: AbsolutePosition, ignoreCollision: Boolean) {
        TODO()
    }
}