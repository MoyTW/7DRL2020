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
    // This is in Absolute XY
    override fun getDreamTileI(pos: AbsolutePosition): DreamTileI? {
        val roomPosition = absoluteToRoomPosition(pos)
        return this.roomsById[roomPosition!!.roomUuid]!!.getTile(roomPosition)
    }

    override fun getAllDreamTileIs(): Map<AbsolutePosition, DreamTileI> {
        val acc: MutableMap<AbsolutePosition, DreamTileI> = mutableMapOf()
        this.activeRoomsToAbsolutePositions.map {
            val room = this.roomsById[it.key]!!
            room.allTiles().map {
                acc[roomToAbsolutePosition(it.key)] = it.value
            }
        }
        return acc
    }

    override val entities: List<Entity>
        get() = roomsById.flatMap { it.value.entities() }

    private val roomsById: MutableMap<String, DreamRoom> = mutableMapOf()
    private val activeRoomsToAbsolutePositions: MutableMap<String, AbsolutePosition> = mutableMapOf()
    // You'll have to remember to link/unlink both ways!
    private val roomGraph: MutableMap<String, MutableMap<ExitDirection, String>> = mutableMapOf()

    fun initFirstRoom(room: DreamRoom) {
        this.roomsById[room.uuid] = room
        activeRoomsToAbsolutePositions[room.uuid] = AbsolutePosition(0, 0)
    }

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
        val roomPosition = absoluteToRoomPosition(pos)
        this.roomsById[roomPosition!!.roomUuid]!!.getTile(roomPosition)?.markExplored()
    }

    /******************************************************************************************************************
     * Entity Management
     ******************************************************************************************************************/

    internal fun absoluteToRoomPosition(absolute: AbsolutePosition): RoomPosition? {
        this.activeRoomsToAbsolutePositions.map {
            val room = this.roomsById[it.key]!!
            val roomPosition = this.activeRoomsToAbsolutePositions[it.key]!!
            /**
             * You're in the room if:
             * roomX <= absoluteX <= roomX + width && roomY <= absoluteY <= roomY + height
             */
            if (roomPosition.x <= absolute.x && absolute.x <= roomPosition.x + room.width &&
                roomPosition.y <= absolute.y && absolute.y <= roomPosition.y + room.height)
            {
                return RoomPosition(absolute.x - roomPosition.x, absolute.y - roomPosition.y, room.uuid)
            }
        }
        return null
    }

    internal fun roomToAbsolutePosition(roomPosition: RoomPosition): AbsolutePosition {
        val roomAbsolutePosition = this.activeRoomsToAbsolutePositions[roomPosition.roomUuid]!!
        return AbsolutePosition(roomAbsolutePosition.x + roomPosition.x, roomAbsolutePosition.y + roomPosition.y)
    }

    internal fun getEntitiesAtPosition(pos: AbsolutePosition): List<Entity> {
        val roomPosition = absoluteToRoomPosition(pos)!!
        return this.roomsById[roomPosition.roomUuid]!!.getEntitiesAtPosition(roomPosition)
    }

    internal fun positionBlocked(pos: AbsolutePosition): Boolean {
        val roomPosition = absoluteToRoomPosition(pos)!!
        return this.roomsById[roomPosition.roomUuid]!!.positionBlocked(roomPosition)
    }

    internal fun arePositionsAdjacent(pos1: AbsolutePosition, pos2: AbsolutePosition): Boolean {
        val dx = kotlin.math.abs(pos1.x - pos2.x)
        val dy = kotlin.math.abs(pos1.y - pos2.y)
        return dx < 2 && dy < 2 && (dx + dy != 0)
    }

    // We actually need to write this one at this level...
    internal fun adjacentUnblockedPositions(pos: AbsolutePosition): List<AbsolutePosition> {
        TODO()
    }

    internal fun placeEntity(entity: Entity, targetPosition: AbsolutePosition, ignoreCollision: Boolean) {
        val roomPosition = absoluteToRoomPosition(targetPosition)!!
        return this.roomsById[roomPosition.roomUuid]!!.placeEntity(entity, roomPosition, ignoreCollision)
    }

    // Ok we need to actually write this one
    internal fun removeEntity(entity: Entity) {
        TODO()
    }

    // TODO: you can't actually pass between rooms lol
    internal fun teleportEntity(entity: Entity, targetPosition: AbsolutePosition, ignoreCollision: Boolean) {
        val roomPosition = absoluteToRoomPosition(targetPosition)!!
        return this.roomsById[roomPosition.roomUuid]!!.teleportEntity(entity, roomPosition, ignoreCollision)
    }
}