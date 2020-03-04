package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.CollisionComponent
import com.mtw.supplier.ecs.components.DisplayComponent
import com.mtw.supplier.ecs.components.DoorComponent
import com.mtw.supplier.ecs.components.RGB
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.color.TileColor


enum class EntityBlueprint(val createFn: () -> Entity) {
    CURTIS_STREET_MY_BED({
        Entity(UUID.randomUUID().toString(), "Your Teenage Bed")
            .addComponent(CollisionComponent.defaultFighter())
            .addComponent(DisplayComponent(character = 'B', foregroundRGB = RGB(0, 173, 238)))
        }),
    CURTIS_STREET_MY_DRESSER({
        Entity(UUID.randomUUID().toString(), "Your Teenage Dresser")
            .addComponent(CollisionComponent.defaultFighter())
            .addComponent(DisplayComponent(character = 'D', foregroundRGB = RGB(0, 173, 238)))
        }),
    CURTIS_STREET_ALEXS_BED({
        Entity(UUID.randomUUID().toString(), "Alex's Childhood Bed")
            .addComponent(CollisionComponent.defaultBlocker())
            .addComponent(DisplayComponent(character = 'B', foregroundRGB = RGB(0, 139, 0)))
    }),
    CURTIS_STREET_BIG_TV({
        Entity(UUID.randomUUID().toString(), "The Big Television")
            .addComponent(CollisionComponent.defaultBlocker())
            .addComponent(DisplayComponent(character = 'T', foregroundRGB = RGB(0, 0, 0)))
    }),
    CURTIS_STREET_DDS_COUCH({
        Entity(UUID.randomUUID().toString(), "A Segment Of The TV Couch")
            .addComponent(CollisionComponent.defaultBlocker())
            .addComponent(DisplayComponent(character = 'C', foregroundRGB = RGB(26, 26, 255)))
    }),
    CURTIS_STREET_MIDDLE_TOILET({
        Entity(UUID.randomUUID().toString(), "The Middle Toilet")
            .addComponent(CollisionComponent.defaultFighter())
            .addComponent(DisplayComponent(character = 'O', foregroundRGB = RGB(0, 51, 51)))
    }),
    CURTIS_STREET_MIDDLE_SHOWER({
        Entity(UUID.randomUUID().toString(), "The Middle Shower")
            .addComponent(CollisionComponent.defaultBlocker())
            .addComponent(DisplayComponent(character = 'S', foregroundRGB = RGB(204, 255, 255)))
    }),
    CURTIS_STREET_MIDDLE_SINK({
        Entity(UUID.randomUUID().toString(), "The Middle Sink")
            .addComponent(CollisionComponent.defaultFighter())
            .addComponent(DisplayComponent(character = 's', foregroundRGB = RGB(0, 102, 0)))
    }),
    CURTIS_STREET_SMALL_PLASITC_TRASH_BIN({
        Entity(UUID.randomUUID().toString(), "One Of The Tiny Trash Bins")
            .addComponent(CollisionComponent.defaultPassable())
            .addComponent(DisplayComponent(character = 'b', foregroundRGB = RGB(0, 10, 26)))
    }),
}

data class DreamRoomBlueprintData(
    val name: String,
    val commentary: String,
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
        "Your childhood and teenage bedroom",
        "You shared it with your brother until you left for college, aside from those years you stole " +
            "the TV room for yourself.",
        minWidth = 8, maxWidth = 8,
        minHeight = 5, maxHeight = 5,
        wallColor = TileColor.create(225, 198, 153), // Beige-ish
        floorColor = TileColor.create(133, 94, 66), // hopefully wood veneer-ish
        entities = listOf(
            EntityBlueprint.CURTIS_STREET_MY_BED,
            EntityBlueprint.CURTIS_STREET_MY_DRESSER,
            EntityBlueprint.CURTIS_STREET_ALEXS_BED
        ))),
    CURTIS_STREET_DOWN_DOWNSTAIRS(DreamRoomBlueprintData(
        "Down-downstairs at your parents' house",
        "When you were fighting with my parents, abandoned your room. You slept on this couch, which you" +
            " liked better than your bed. You don't remember why, but it feels nostalgic.",
        minWidth = 7, maxWidth = 9,
        minHeight = 14, maxHeight = 16,
        wallColor = TileColor.create(255, 153, 204), // light-pink-ish
        floorColor = TileColor.create(255, 153, 204), // hopefully hardwood-ish
        entities = listOf(
            EntityBlueprint.CURTIS_STREET_BIG_TV,
            EntityBlueprint.CURTIS_STREET_DDS_COUCH,
            EntityBlueprint.CURTIS_STREET_DDS_COUCH,
            EntityBlueprint.CURTIS_STREET_DDS_COUCH
        ))),
    CURTIS_STREET_MIDDLE_BATHROOM(DreamRoomBlueprintData(
        "The middle bathroom at your parents' house",
        "This was your favorite bathroom. The shower was nice and hot, there was plenty of space near " +
            "the sink, and the mirror was huge and always clear. You feel comfortable here.",
        minWidth = 5, maxWidth = 5,
        minHeight = 7, maxHeight = 7,
        wallColor = TileColor.create(255, 153, 204), // light-pink-ish
        floorColor = TileColor.create(255, 153, 204), // hopefully hardwood-ish
        entities = listOf(
            EntityBlueprint.CURTIS_STREET_MIDDLE_TOILET,
            EntityBlueprint.CURTIS_STREET_MIDDLE_SHOWER,
            EntityBlueprint.CURTIS_STREET_MIDDLE_SINK,
            EntityBlueprint.CURTIS_STREET_SMALL_PLASITC_TRASH_BIN
        ))),
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
    var name: String? = null,
    var commentary: String? = null,
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
        val room = DreamRoom(roomUuid,
            name ?: "Somewhere strange",
            commentary ?: "You know nothing about this place.",
            width!!,
            height!!,
            doors,
            nodes
        )

        buildWalls(room)
        for (blueprint in this.entityBlueprints) {
            val entity = blueprint.createFn()
            room.placeEntity(entity, room.randomPlacementPosition(), false)
        }

        return room
    }
}