package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.DoorComponent
import com.mtw.supplier.ecs.components.RoomPositionComponent
import com.mtw.supplier.encounter.state.map.blueprint.DreamRoomBlueprint
import com.mtw.supplier.encounter.state.map.blueprint.ThemeTag
import com.mtw.supplier.utils.AbsolutePosition
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.util.*

interface DreamMapI {
    fun getDreamTileI(pos: AbsolutePosition): DreamTileI?
    fun getAllDreamTileIs(): Map<AbsolutePosition, DreamTileI>
    fun getDoors(roomUuid: String): Map<ExitDirection, Entity>
    val entities: List<Entity>
}

class DreamMapBuilder(val numRooms: Int = DreamRoomBlueprint.values().size) {
    fun build(): DreamMap {
        val roomBlueprints = DreamRoomBlueprint.values().toMutableList()

        val map = DreamMap()
        for (i in 0 until numRooms) {
            if (roomBlueprints.isNotEmpty()) {
                map.addRoom(DreamRoomBuilder().withDreamRoomBlueprint(roomBlueprints[0]).build())
                roomBlueprints.removeAt(0)
            } else {
                val width = (5..20).random()
                val height = (5..20).random()
                map.addRoom(DreamRoomBuilder(null, null, width, height).build())
            }

        }
        map.initializeWith(map.inDeckRooms.filter { it.tag == ThemeTag.STRANGE_PLACE }.random())
        return map
    }
}

@Serializable
class RoomGraph {
    // This is a two-sided map - when operating, you have to remember to link/unlink both sides.
    private val currentGraph: MutableMap<String, MutableMap<ExitDirection, String>> = mutableMapOf()

    fun initializeWith(firstRoom: String) {
        if (this.currentGraph.isNotEmpty()) {
            throw RuntimeException("Can't re-initialize")
        }
        currentGraph[firstRoom] = mutableMapOf()
    }

    /**
     * When you draw a new room:
     * + If there are already n rooms in the graph, drop the last *visited* room in the graph
     * + Close all other doors to the current room other than the new room's direction
     *   - Draw the rooms as names only
     * + Add the new room to the graph
     *
     * To do this:
     * + you must be able to know the last visited room
     * + you have to be able to know whether or not you should draw the room as a label or in full
     */

    fun allRooms(): Set<String> {
        return currentGraph.keys
    }

    fun contains(uuid: String): Boolean {
        return this.currentGraph.containsKey(uuid)
    }

    fun connectedRooms(roomUuid: String): Map<ExitDirection, String>? {
        return this.currentGraph[roomUuid]
    }

    fun getConnected(roomUuid: String, exitDirection: ExitDirection): String? {
        return this.currentGraph[roomUuid]?.get(exitDirection)
    }

    private fun connectSingle(firstRoom: String, exitDirection: ExitDirection, secondRoom: String) {
        if (currentGraph[firstRoom] == null) { currentGraph[firstRoom] = mutableMapOf() }
        currentGraph[firstRoom]!![exitDirection] = secondRoom
    }

    fun connect(firstRoom: String, exitDirection: ExitDirection, secondRoom: String) {
        if (this.getConnected(firstRoom, exitDirection) != null) {
            throw RuntimeException("COULD NOT LINK: $firstRoom already had an exit link in $exitDirection")
        }
        if (this.getConnected(secondRoom, exitDirection.opposite()) != null) {
            throw RuntimeException("COULD NOT LINK: $secondRoom already had an exit link in ${exitDirection.opposite()}")
        }

        connectSingle(firstRoom, exitDirection, secondRoom)
        connectSingle(secondRoom, exitDirection.opposite(), firstRoom)
    }

    private fun unlink(firstRoom: String, exitDirection: ExitDirection) {
        val adjacent = this.getConnected(firstRoom, exitDirection)
            ?: throw RuntimeException("COULD NOT UNLINK: $firstRoom had no linked room in direction ${exitDirection}")
        currentGraph[firstRoom]!!.remove(exitDirection)
        currentGraph[adjacent]!!.remove(exitDirection.opposite())
    }

    /**
     * Cuts a room out of the graph. Assumes that the excision will not create a disjoint graph.
     */
    fun disconnect(room: DreamRoom) {
        // For every exit which it has a connection, unlink that connection
        room.doors.map {
            if (this.getConnected(room.uuid, it.key) != null) {
                unlink(room.uuid, it.key)
            }
        }
        this.currentGraph.remove(room.uuid)
    }
}

@Serializable
private class SeenHistory(
    val memorySize: Int = 5
) {
    private val history: SortedMap<Int, MutableList<String>> = sortedMapOf()
    private val historyByRoom: MutableMap<String, MutableSet<Int>> = mutableMapOf()

    fun lastSeenRooms(roomGraph: RoomGraph): List<String> {
        return roomGraph.allRooms()
            .map { Pair(it, historyByRoom[it]!!.sorted().last()) }
            .sortedBy { it.second }
            .takeLast(memorySize)
            .map { it.first }
            .reversed()
    }

    fun lastSeenAt(roomUuid: String): Int? {
        return historyByRoom[roomUuid]?.max()
    }

    fun markSeen(roomUuid: String, time: Int) {
        if (!history.containsKey(time)) { history[time] = mutableListOf() }
        if(!history[time]!!.contains(roomUuid)) {
            history[time]!!.add(roomUuid)
        }

        if (!historyByRoom.containsKey(roomUuid)) { historyByRoom[roomUuid] = mutableSetOf() }
        historyByRoom[roomUuid]!!.add(time)
    }

    fun markOccupied(roomUuid: String, time: Int) {
        history[time]!!.remove(roomUuid)
        history[time]!!.add(0, roomUuid)
    }
}

@Serializable
class DreamMap: DreamMapI {
    private val logger = LoggerFactory.getLogger(DreamMap::class.java)

    private val roomsById: MutableMap<String, DreamRoom> = mutableMapOf()
    private val mappedRoomsToAbsolutePositions: MutableMap<String, AbsolutePosition> = mutableMapOf()
    private val roomGraph: RoomGraph = RoomGraph()
    private val seenHistory: SeenHistory = SeenHistory()

    /******************************************************************************************************************
     * Rooms
     ******************************************************************************************************************/

    /**
     * Terms:
     * MAPPED = The rooms of the graph which are currently being drawn fully to the screen
     * IN_GRAPH = Exists in the node graph but is *not* being drawn to the screen
     * IN_DECK = Free-floating rooms which are not in the current graph
     */

    val inDeckRooms: List<DreamRoom>
        get() = roomsById.filter { !roomGraph.contains(it.key) }.values.toList()

    fun initializeWith(room: DreamRoom) {
        addRoom(room)
        mappedRoomsToAbsolutePositions[room.uuid] = AbsolutePosition(0, 0)
        roomGraph.initializeWith(room.uuid)
    }

    fun lastSeenRoomNames(): List<String> {
        return this.seenHistory.lastSeenRooms(this.roomGraph).map { this.roomsById[it]!!.name }
    }

    fun addRoom(room: DreamRoom) {
        if (this.roomsById[room.uuid] != null && this.roomsById[room.uuid] != room) {
            throw RuntimeException("ROOM IS ATTEMPTING TO BE REASSIGNED DON'T DO THIS")
        }
        this.roomsById[room.uuid] = room
    }

    private fun pullInDeckRoom(existingRoom: DreamRoom): DreamRoom {
        val trySameTag = when ((1..100).random()) {
            in 1..75 -> true
            else -> false
        }
        return if (!trySameTag) {
            inDeckRooms.random()
        } else {
            val sameTag = inDeckRooms.filter { it.tag == existingRoom.tag }
            if (sameTag.isNotEmpty()) {
                sameTag.random()
            } else {
                inDeckRooms.random()
            }
        }

    }

    fun drawAndConnectRoom(existingRoomUuid: String, exitDirection: ExitDirection) {
        // Get all exits from this room that already exist
        this.roomGraph.connectedRooms(existingRoomUuid)?.map {
            val adjacentUuid = it.value

            // Remove the room from the map
            this.mappedRoomsToAbsolutePositions.remove(adjacentUuid)

            // Close the room's doors
            val adjacent = this.roomsById[adjacentUuid]!!
            adjacent.doors.map { door ->
                door.value.getComponent(DoorComponent::class).close(door.value)
            }
        }

        // Actually link the rooms
        val existingRoom = this.roomsById[existingRoomUuid]!!
        // If it's a pre-existing connection, re-use that
        if (this.roomGraph.getConnected(existingRoomUuid, exitDirection) != null) {
            val graphed = this.roomsById[this.roomGraph.getConnected(existingRoomUuid, exitDirection)!!]!!
            mapGraphedRoom(existingRoom, exitDirection, graphed)
        } else {
            val newRoom = pullInDeckRoom(existingRoom)
            if (this.roomsById[newRoom.uuid] == null) {
                this.roomsById[newRoom.uuid] = newRoom
            }
            this.roomGraph.connect(existingRoom.uuid, exitDirection, newRoom.uuid)
            mapGraphedRoom(existingRoom, exitDirection, newRoom)

            // If you are past the seen limit, forget the last room
            val graphedRooms = this.roomGraph.allRooms()
            if (graphedRooms.size > seenHistory.memorySize) {
                val last = graphedRooms.filter { it != newRoom.uuid }
                    .minBy { seenHistory.lastSeenAt(it) ?: Integer.MAX_VALUE }
                if (last != null) {
                    roomGraph.disconnect(this.roomsById[last]!!)

                    // Remove the room from the map
                    this.mappedRoomsToAbsolutePositions.remove(last)

                    // Close the room's doors
                    val lastRoom = this.roomsById[last]!!
                    lastRoom.doors.map { door ->
                        door.value.getComponent(DoorComponent::class).close(door.value)
                    }
                }
            }
        }
    }

    private fun mapGraphedRoom(existingRoom: DreamRoom, exitDirection: ExitDirection, newRoom: DreamRoom) {
        if (existingRoom == newRoom) {
            throw RuntimeException("YO YOU'RE PULLING THE SAME ROOM")
        }
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
            mappedRoomsToAbsolutePositions[newRoom.uuid] = AbsolutePosition(newRoomX, newRoomY)
        } else if (exitDirection == ExitDirection.EAST) {
            val newRoomX = existingDoorAbsolutePosition.x + 1
            val newRoomY = existingDoorAbsolutePosition.y - newRoomDoorPosition.y
            mappedRoomsToAbsolutePositions[newRoom.uuid] = AbsolutePosition(newRoomX, newRoomY)
        } else if (exitDirection == ExitDirection.SOUTH) {
            val newRoomX = existingDoorAbsolutePosition.x - newRoomDoorPosition.x
            val newRoomY = existingDoorAbsolutePosition.y - newRoom.height
            mappedRoomsToAbsolutePositions[newRoom.uuid] = AbsolutePosition(newRoomX, newRoomY)
        } else if (exitDirection == ExitDirection.WEST) {
            val newRoomX = existingDoorAbsolutePosition.x - newRoom.width
            val newRoomY = existingDoorAbsolutePosition.y - newRoomDoorPosition.y
            mappedRoomsToAbsolutePositions[newRoom.uuid] = AbsolutePosition(newRoomX, newRoomY)
        }
    }

    /******************************************************************************************************************
     * Tiles
     ******************************************************************************************************************/

    fun markSeen(pos: AbsolutePosition, time: Int) {
        val roomPosition = absoluteToRoomPosition(pos)
        if (roomPosition != null) {
            this.seenHistory.markSeen(roomPosition.roomUuid, time)
            this.roomsById[roomPosition.roomUuid]!!.getTile(roomPosition)!!.markExplored()
        }
    }

    fun wasSeen(roomPosition: RoomPosition): Boolean {
        return this.roomsById[roomPosition.roomUuid]!!.getTile(roomPosition)!!.explored
    }

    fun markOccupied(roomUuid: String, time: Int) {
        this.seenHistory.markOccupied(roomUuid, time)
    }

    override fun getDreamTileI(pos: AbsolutePosition): DreamTileI? {
        val roomPosition = absoluteToRoomPosition(pos) ?: return null
        return this.roomsById[roomPosition.roomUuid]?.getTile(roomPosition)
    }

    override fun getAllDreamTileIs(): Map<AbsolutePosition, DreamTileI> {
        val acc: MutableMap<AbsolutePosition, DreamTileI> = mutableMapOf()

        this.mappedRoomsToAbsolutePositions.map { uuidToPosition ->
            val room = this.roomsById[uuidToPosition.key]!!
            room.allTiles().map {
                acc[roomToAbsolutePosition(it.key)!!] = it.value
            }
        }
        return acc
    }

    fun getDreamRoomName(roomUuid: String): String {
        return this.roomsById[roomUuid]!!.name
    }

    fun getDreamRoomCommentary(roomUuid: String): String {
        return this.roomsById[roomUuid]!!.commentary
    }

    override val entities: List<Entity>
        get() = roomsById.flatMap { it.value.entities() }

    fun getEntitiesInRoom(roomUuid: String): List<Entity> {
        return  this.roomsById[roomUuid]!!.entities()
    }

    override fun getDoors(roomUuid: String): Map<ExitDirection, Entity> {
        return this.roomsById[roomUuid]!!.doors
    }

    internal fun absoluteToRoomPosition(absolute: AbsolutePosition): RoomPosition? {
        this.mappedRoomsToAbsolutePositions.map {
            val room = this.roomsById[it.key]!!
            val roomPosition = this.mappedRoomsToAbsolutePositions[it.key]!!
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

    internal fun randomUnblockedPosition(): AbsolutePosition? {
        val placementPosition = this.roomsById[this.mappedRoomsToAbsolutePositions.keys.first()]!!.randomPlacementPosition()
        return if (placementPosition == null) {
            null
        } else {
            roomToAbsolutePosition(placementPosition)!!
        }
    }

    internal fun roomToAbsolutePosition(roomPosition: RoomPosition): AbsolutePosition? {
        val roomAbsolutePosition = this.mappedRoomsToAbsolutePositions[roomPosition.roomUuid] ?: return null
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
    internal fun adjacentUnblockedPositionsInSameRoom(pos: AbsolutePosition): List<AbsolutePosition> {
        val roomPosition = absoluteToRoomPosition(pos)!!
        val adjacent = this.roomsById[roomPosition.roomUuid]!!.adjacentUnblockedPositions(roomPosition)
        return adjacent.map { roomToAbsolutePosition(it)!! }
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