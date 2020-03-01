package com.mtw.supplier.encounter.state.map

/** You can only have MAX 5 visible rooms
 *        ROOM
 *         |
 * ROOM - ROOM - ROOM
 *         |
 *        ROOM
 */

class DreamMap() {
    private val roomsById: MutableMap<Int, DreamRoom> = mutableMapOf()
    private val roomGraph: MutableMap<DreamRoom, Pair<ExitDirection, DreamRoom>> = mutableMapOf()

    internal fun connectRooms(oldRoom: DreamRoom, exitDirection: ExitDirection, newRoom: DreamRoom) {
        TODO()
    }

    private fun recalculatePositions() {
        
    }
}