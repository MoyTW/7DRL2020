package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.encounter.state.map.blueprint.DreamRoomBlueprint
import com.mtw.supplier.encounter.state.map.blueprint.EntityBlueprint
import com.mtw.supplier.encounter.state.map.blueprint.ThemeTag
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.color.TileColor


class DreamRoomBuilder(
    var name: String? = null,
    var commentary: String? = null,
    var width: Int? = null,
    var height: Int? = null,
    var wallColor: TileColor = TileColor.transparent(),
    var exits: List<ExitDirection> = ExitDirection.ALL_DIRECTIONS,
    val entityBlueprints: MutableList<EntityBlueprint> = mutableListOf(),
    var tag: ThemeTag? = null
) {
    private val roomUuid: String = UUID.randomUUID().toString()
    private val doors: MutableMap<ExitDirection, Entity> = mutableMapOf()

    fun withEntityBlueprint(blueprint: EntityBlueprint): DreamRoomBuilder {
        this.entityBlueprints.add(blueprint)
        return this
    }

    fun withDreamRoomBlueprint(blueprint: DreamRoomBlueprint): DreamRoomBuilder {
        val data = blueprint.blueprintData
        this.name = data.name
        this.commentary = data.commentary
        this.width = (data.minWidth..data.maxWidth).random()
        this.height = (data.minHeight..data.maxHeight).random()
        this.wallColor = data.wallColor
        this.entityBlueprints.addAll(data.entities)
        this.tag = data.tag
        return this
    }

    private fun doorOrWall(isDoor: Boolean, direction: ExitDirection): Entity {
        return if (isDoor) {
            val door = Entity(UUID.randomUUID().toString(), "Door")
                .addComponent(CollisionComponent.blocker())
                .addComponent(DoorComponent(direction))
                .addComponent(DisplayComponent(true, backgroundRGB = RGB.fromTileColor(this.wallColor),
                    foregroundRGB = RGB(255, 255, 0), character = '%'))
            doors[direction] = door
            door
        } else {
            Entity(UUID.randomUUID().toString(), "Wall")
                .addComponent(CollisionComponent.blocker())
                .addComponent(DisplayComponent(true, backgroundRGB = RGB.fromTileColor(this.wallColor)))
        }
    }

    private fun buildWalls(room: DreamRoom) {
        // North wall
        val northExitX = if(exits.contains(ExitDirection.NORTH)) { (1 until width!! - 1).random() } else { null }
        for (x in 0 until this.width!!) {
            room.placeEntity(doorOrWall(x == northExitX, ExitDirection.NORTH), RoomPosition(x, height!! - 1, roomUuid), false)
        }
        // East
        val eastExitY = if(exits.contains(ExitDirection.EAST))  { (1 until height!! - 1).random() } else { null }
        for (y in 0 until height!! - 1) {
            room.placeEntity(doorOrWall(y == eastExitY, ExitDirection.EAST), RoomPosition(width!! - 1, y, roomUuid), false)
        }
        // South
        val southExitX = if(exits.contains(ExitDirection.SOUTH))  { (1 until width!! - 1).random() } else { null }
        for (x in 0 until width!! - 1) {
            room.placeEntity(doorOrWall(x == southExitX, ExitDirection.SOUTH), RoomPosition(x, 0, roomUuid), false)
        }
        // West
        val westExitX = if(exits.contains(ExitDirection.WEST))  { (1 until height!! - 1).random() } else { null }
        for (y in 1 until height!! - 1) {
            room.placeEntity(doorOrWall(y == westExitX, ExitDirection.WEST), RoomPosition(0, y, roomUuid), false)
        }
    }

    fun build(): DreamRoom {
        if (width == null || height == null) {
            TODO("null width or height")
        }

        val nodes: Array<Array<DreamTile>> = Array(width!!) { Array(height!!) { DreamTile() } }
        val room = DreamRoom(roomUuid,
            name ?: "Somewhere strange",
            commentary ?: "You know nothing about this place.",
            width!!,
            height!!,
            doors,
            nodes,
            tag!!
        )

        buildWalls(room)
        for (blueprint in this.entityBlueprints) {
            val entity = blueprint.createFn()
            room.placeEntity(entity, room.randomPlacementPosition()!!, false)
        }

        return room
    }
}