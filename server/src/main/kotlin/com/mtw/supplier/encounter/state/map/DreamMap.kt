package com.mtw.supplier.encounter.state.map

/** You can only have MAX 5 visible rooms
 *        ROOM
 *         |
 * ROOM - ROOM - ROOM
 *         |
 *        ROOM
 */

class DreamMap(
    private val centerRoom: DreamRoom
) {
    private val roomsById: MutableMap<Int, DreamRoom> = mutableMapOf()
    private val roomGraph: MutableMap<DreamRoom, MutableMap<ExitDirection, DreamRoom>> = mutableMapOf()

    internal fun connectRooms(oldRoom: DreamRoom, exitDirection: ExitDirection, newRoom: DreamRoom) {
        TODO()
    }

    private fun getConnectedRoomByDirection(room: DreamRoom, direction: ExitDirection): DreamRoom? {
        return roomGraph[room]!![direction]
    }

    private fun recalculatePositions(primaryRoom: DreamRoom) {
    }
}