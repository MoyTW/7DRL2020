package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.CollisionComponent
import com.mtw.supplier.ecs.components.DisplayComponent
import com.mtw.supplier.ecs.components.DoorComponent
import com.mtw.supplier.ecs.components.RGB
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.color.TileColor


enum class EntityBlueprint(val createFn: () -> Entity) {
    CURTIS_STREET_BEDROOM_MY_BED({
        Entity(UUID.randomUUID().toString(), "My Teenage Bed")
            .addComponent(CollisionComponent.defaultFighter())
            .addComponent(DisplayComponent(character = 'B', foregroundRGB = RGB(0, 173, 238)))
        }),
    CURTIS_STREET_BEDROOM_MY_DRESSER({
        Entity(UUID.randomUUID().toString(), "My Teenage Dresser")
            .addComponent(CollisionComponent.defaultFighter())
            .addComponent(DisplayComponent(character = 'D', foregroundRGB = RGB(0, 173, 238)))
        }),
    CURTIS_STREET_BEDROOM_ALEXS_BED({
        Entity(UUID.randomUUID().toString(), "Alex's Childhood Bed")
            .addComponent(CollisionComponent.defaultBlocker())
            .addComponent(DisplayComponent(character = 'B', foregroundRGB = RGB(0, 139, 0)))
    }),
}

data class DreamRoomBlueprintData(
    val minWidth: Int,
    val maxWidth: Int,
    val minHeight: Int,
    val maxHeight: Int,
    val wallColor: TileColor,
    val floorColor: TileColor,
    val entities: List<EntityBlueprint>
)

enum class DreamRoomBlueprint(val blueprintData: DreamRoomBlueprintData) {
    CURTIS_STREET_BEDROOM(DreamRoomBlueprintData(
        minWidth = 10,
        maxWidth = 12,
        minHeight = 8,
        maxHeight = 8,
        wallColor = TileColor.create(225, 198, 153), // Beige-ish
        floorColor = TileColor.create(133, 94, 66),
        entities = listOf(
            EntityBlueprint.CURTIS_STREET_BEDROOM_MY_BED,
            EntityBlueprint.CURTIS_STREET_BEDROOM_MY_DRESSER,
            EntityBlueprint.CURTIS_STREET_BEDROOM_ALEXS_BED
        )))
}

class CurtisStreetDownDownstairs() {
    // Has a hardwood floor
    // Has a big TV
    // Has a big couch
}

class CurtisStreetDadsRoom() {
    // Has a huge bed
    // Has a dresser
    // Has a closet full of clothes
}

class CurtisStreetMomsRoom() {
    // Has a door to the attic
    // Has a small bed
    // Has a desk
}

class CutisStreetLivingRoom() {
    // Is very large
    // Has a big table and many chairs
    // Has a waiting area with sofa, fancy chairs
}

class DreamRoomBuilder(
    var width: Int? = null,
    var height: Int? = null,
    var floorColor: TileColor? = TileColor.transparent(),
    var wallColor: TileColor = TileColor.transparent(),
    var exits: List<ExitDirection> = ExitDirection.ALL_DIRECTIONS,
    val entityBlueprints: MutableList<EntityBlueprint> = mutableListOf()
) {
    private val roomUuid: String = UUID.randomUUID().toString()
    private val doors: MutableMap<ExitDirection, Entity> = mutableMapOf()

    fun withEntityBlueprint(blueprint: EntityBlueprint): DreamRoomBuilder {
        this.entityBlueprints.add(blueprint)
        return this
    }

    fun withDreamRoomBlueprint(blueprint: DreamRoomBlueprint): DreamRoomBuilder {
        val data = blueprint.blueprintData
        this.width = (data.minWidth..data.maxWidth).random()
        this.height = (data.minHeight..data.maxHeight).random()
        this.floorColor = data.floorColor
        this.wallColor = data.wallColor
        this.entityBlueprints.addAll(data.entities)
        return this
    }

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
                .addComponent(DisplayComponent(backgroundRGB = RGB.fromTileColor(this.wallColor)))
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
        val room = DreamRoom(roomUuid, width!!, height!!, doors, nodes)

        buildWalls(room)
        for (blueprint in this.entityBlueprints) {
            val entity = blueprint.createFn()
            room.placeEntity(entity, room.randomPlacementPosition(), false)
        }

        return room
    }
}