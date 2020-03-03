package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.DoorComponent
import com.mtw.supplier.ecs.components.RoomPositionComponent
import com.mtw.supplier.utils.AbsolutePosition
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory

/** You can only have MAX 5 visible rooms
 *        ROOM
 *         |
 * ROOM - ROOM - ROOM
 *         |
 *        ROOM
 */

interface DreamMapI {
    fun getDreamTileI(pos: AbsolutePosition): DreamTileI?
    fun getAllDreamTileIs(): Map<AbsolutePosition, DreamTileI>
    fun getDoors(roomUuid: String): Map<ExitDirection, Entity>
    val entities: List<Entity>
}

class DreamMapBuilder(val numRooms: Int = 3) {
    fun build(): DreamMap {
        val map = DreamMap()
        for (i in 0 until numRooms) {
            val room = DreamRoomBuilder(15, 15, ExitDirection.ALL_DIRECTIONS).build()
            map.addRoom(room)
        }
        map.initializeWith(map.inactiveRooms.random())

        return map
    }
}

@Serializable
class DreamMap: DreamMapI {
    private val logger = LoggerFactory.getLogger(DreamMap::class.java)

    private val roomsById: MutableMap<String, DreamRoom> = mutableMapOf()
    private val activeRoomsToAbsolutePositions: MutableMap<String, AbsolutePosition> = mutableMapOf()
    // You'll have to remember to link/unlink both ways!
    private val roomGraph: MutableMap<String, MutableMap<ExitDirection, String>> = mutableMapOf()

    /******************************************************************************************************************
     * Rooms
     ******************************************************************************************************************/

    val activeRooms: List<DreamRoom>
        get() = roomsById.filter { activeRoomsToAbsolutePositions.containsKey(it.key) }.values.toList()

    val inactiveRooms: List<DreamRoom>
        get() = roomsById.filter { !activeRoomsToAbsolutePositions.containsKey(it.key) }.values.toList()

    fun initializeWith(room: DreamRoom) {
        addRoom(room)
        activeRoomsToAbsolutePositions[room.uuid] = AbsolutePosition(0, 0)
    }

    fun addRoom(room: DreamRoom) {
        if (this.roomsById[room.uuid] != null && this.roomsById[room.uuid] != room) {
            throw RuntimeException("ROOM IS ATTEMPTING TO BE REASSIGNED DON'T DO THIS")
        }
        this.roomsById[room.uuid] = room
    }

    private fun drawInactiveRoom(): DreamRoom {
        return inactiveRooms.random()
    }

    fun drawAndConnectRoom(existingRoomUuid: String, exitDirection: ExitDirection) {
        logger.info("Roomgraph size: " + this.roomGraph.size)
        // Get all exits from this room that already exist
        val toUnlink: MutableList<Pair<String, ExitDirection>> = mutableListOf()
        this.roomGraph[existingRoomUuid]?.map {
            // Unlink this side of the exit
            val adjacentUuid = it.value

            // Unlink from both
            toUnlink.add(Pair(existingRoomUuid, it.key))
            toUnlink.add(Pair(adjacentUuid, exitDirection.opposite()))

            // Remove the room from the map
            this.activeRoomsToAbsolutePositions.remove(adjacentUuid)

            // Close the room's doors
            val adjacent = this.roomsById[adjacentUuid]!!
            adjacent.doors.map { door ->
                door.value.getComponent(DoorComponent::class).close(door.value)
            }
        }
        toUnlink.map { this.roomGraph[it.first]!!.remove(it.second) }

        connectRooms(this.roomsById[existingRoomUuid]!!, exitDirection, drawInactiveRoom())
    }

    private fun connectRooms(existingRoom: DreamRoom, exitDirection: ExitDirection, newRoom: DreamRoom) {
        if (this.roomsById[newRoom.uuid] == null) {
            this.roomsById[newRoom.uuid] = newRoom
        }

        if (roomGraph[existingRoom.uuid]?.get(exitDirection) != null) {
            throw RuntimeException("COULD NOT LINK: $existingRoom already had an exit link in $exitDirection")
        }
        if (roomGraph[newRoom.uuid]?.get(exitDirection.opposite()) != null) {
            throw RuntimeException("COULD NOT LINK: $newRoom already had an exit link in ${exitDirection.opposite()}")
        }

        if (!roomGraph.containsKey(existingRoom.uuid)) {
            roomGraph[existingRoom.uuid] = mutableMapOf()
        }
        if (!roomGraph.containsKey(newRoom.uuid)) {
            roomGraph[newRoom.uuid] = mutableMapOf()
        }
        roomGraph[existingRoom.uuid]?.set(exitDirection, newRoom.uuid)
        roomGraph[newRoom.uuid]?.set(exitDirection.opposite(), existingRoom.uuid)

        val existingDoor = existingRoom.getDoor(exitDirection)!!
        val existingDoorAbsolutePosition = roomToAbsolutePosition(existingDoor.getComponent(RoomPositionComponent::class).roomPosition)!!

        val newRoomDoor = newRoom.getDoor(exitDirection.opposite())!!
        val newRoomDoorPosition = newRoomDoor.getComponent(RoomPositionComponent::class).roomPosition

        // Open the door
        newRoomDoor.getComponent(DoorComponent::class).toggleOpen(newRoomDoor)

        // Line them up such that the doors match!
        if (exitDirection == ExitDirection.NORTH) {
            val newRoomX = existingDoorAbsolutePosition.x - newRoomDoorPosition.x
            val newRoomY = existingDoorAbsolutePosition.y + 1
            activeRoomsToAbsolutePositions[newRoom.uuid] = AbsolutePosition(newRoomX, newRoomY)
        } else if (exitDirection == ExitDirection.EAST) {
            val newRoomX = existingDoorAbsolutePosition.x + 1
            val newRoomY = existingDoorAbsolutePosition.y - newRoomDoorPosition.y
            activeRoomsToAbsolutePositions[newRoom.uuid] = AbsolutePosition(newRoomX, newRoomY)
        } else if (exitDirection == ExitDirection.SOUTH) {
            val newRoomX = existingDoorAbsolutePosition.x - newRoomDoorPosition.x
            val newRoomY = existingDoorAbsolutePosition.y - existingRoom.height
            activeRoomsToAbsolutePositions[newRoom.uuid] = AbsolutePosition(newRoomX, newRoomY)
        } else if (exitDirection == ExitDirection.WEST) {
            val newRoomX = existingDoorAbsolutePosition.x - existingRoom.width
            val newRoomY = existingDoorAbsolutePosition.y - newRoomDoorPosition.y
            activeRoomsToAbsolutePositions[newRoom.uuid] = AbsolutePosition(newRoomX, newRoomY)
        }
    }

    private fun getConnectedRoomByDirection(room: DreamRoom, direction: ExitDirection): DreamRoom? {
        val uuid = roomGraph[room.uuid]!![direction]
        return roomsById[uuid]
    }

    /******************************************************************************************************************
     * Tiles
     ******************************************************************************************************************/

    fun markExplored(pos: AbsolutePosition) {
        val roomPosition = absoluteToRoomPosition(pos)
        if (roomPosition != null) {
            this.roomsById[roomPosition.roomUuid]?.getTile(roomPosition)?.markExplored()
        }
    }

    override fun getDreamTileI(pos: AbsolutePosition): DreamTileI? {
        val roomPosition = absoluteToRoomPosition(pos) ?: return null
        return this.roomsById[roomPosition.roomUuid]?.getTile(roomPosition)
    }

    override fun getAllDreamTileIs(): Map<AbsolutePosition, DreamTileI> {
        val acc: MutableMap<AbsolutePosition, DreamTileI> = mutableMapOf()

        logger.info("Num active rooms: " + this.activeRooms.size)

        this.activeRoomsToAbsolutePositions.map { uuidToPosition ->
            val room = this.roomsById[uuidToPosition.key]!!
            room.allTiles().map {
                acc[roomToAbsolutePosition(it.key)!!] = it.value
            }
        }
        return acc
    }

    override val entities: List<Entity>
        get() = roomsById.flatMap { it.value.entities() }


    override fun getDoors(roomUuid: String): Map<ExitDirection, Entity> {
        return this.roomsById[roomUuid]!!.doors
    }

    internal fun absoluteToRoomPosition(absolute: AbsolutePosition): RoomPosition? {
        this.activeRoomsToAbsolutePositions.map {
            val room = this.roomsById[it.key]!!
            val roomPosition = this.activeRoomsToAbsolutePositions[it.key]!!
            /**
             * You're in the room if:
             * roomX <= absoluteX <= roomX + width && roomY <= absoluteY <= roomY + height
             */
            if (roomPosition.x <= absolute.x && absolute.x < roomPosition.x + room.width &&
                roomPosition.y <= absolute.y && absolute.y < roomPosition.y + room.height)
            {
                return RoomPosition(absolute.x - roomPosition.x, absolute.y - roomPosition.y, room.uuid)
            }
        }
        return null
    }

    internal fun roomToAbsolutePosition(roomPosition: RoomPosition): AbsolutePosition? {
        val roomAbsolutePosition = this.activeRoomsToAbsolutePositions[roomPosition.roomUuid] ?: return null
        return AbsolutePosition(roomAbsolutePosition.x + roomPosition.x, roomAbsolutePosition.y + roomPosition.y)
    }

    internal fun getEntitiesAtPosition(pos: AbsolutePosition): List<Entity> {
        val roomPosition = absoluteToRoomPosition(pos) ?: return listOf()
        return this.roomsById[roomPosition.roomUuid]!!.getEntitiesAtPosition(roomPosition)
    }

    internal fun positionBlocked(pos: AbsolutePosition): Boolean {
        val roomPosition = absoluteToRoomPosition(pos) ?: return true
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

    internal fun teleportEntity(entity: Entity, targetPosition: AbsolutePosition, ignoreCollision: Boolean) {

        val roomPosition = absoluteToRoomPosition(targetPosition)!!
        // Remove from origin room
        this.roomsById[entity.getComponent(RoomPositionComponent::class).roomPosition.roomUuid]!!.removeEntity(entity)
        // Place into target room
        return this.roomsById[roomPosition.roomUuid]!!.placeEntity(entity, roomPosition, ignoreCollision)
    }
}