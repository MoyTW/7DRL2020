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
    // You'll have to remember to link/unlink both ways!
    private val roomGraph: MutableMap<DreamRoom, MutableMap<ExitDirection, DreamRoom>> = mutableMapOf()

    internal fun connectRooms(existingRoom: DreamRoom, exitDirection: ExitDirection, newRoom: DreamRoom) {
        if (roomGraph[existingRoom]?.get(exitDirection) != null) {
            throw RuntimeException("COULD NOT LINK: $existingRoom already had an exit link in $exitDirection")
        }
        if (roomGraph[newRoom]?.get(exitDirection.opposite()) != null) {
            throw RuntimeException("COULD NOT LINK: $newRoom already had an exit link in ${exitDirection.opposite()}")
        }

        roomGraph[existingRoom]?.set(exitDirection, newRoom)
        roomGraph[newRoom]?.set(exitDirection.opposite(), existingRoom)
    }

    private fun getConnectedRoomByDirection(room: DreamRoom, direction: ExitDirection): DreamRoom? {
        return roomGraph[room]!![direction]
    }
}