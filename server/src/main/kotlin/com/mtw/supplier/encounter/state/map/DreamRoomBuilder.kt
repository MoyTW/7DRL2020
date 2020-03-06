package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.ecs.components.ai.*
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.color.TileColor


enum class EntityBlueprint(val createFn: () -> Entity) {
    BETTYS_LIVING_ROOM_TV({
        Entity(UUID.randomUUID().toString(), "Betty's big TV")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'T', foregroundRGB = RGB(0, 0, 0)))
    }),
    BETTYS_LIVING_ROOM_COUCH({
        Entity(UUID.randomUUID().toString(), "Betty's living room couches")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'C', foregroundRGB = RGB(10, 10, 41)))
    }),
    BETTYS_LIVING_ROOM_BOOKSHELVES({
        Entity(UUID.randomUUID().toString(), "Betty's living room bookshelves")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'S', foregroundRGB = RGB(153, 51, 0)))
    }),
    BETTYS_BACKYARD_LAUNDRY({
        Entity(UUID.randomUUID().toString(), "Laundry drying in the sun")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'L', foregroundRGB = RGB(220, 220, 220)))
    }),
    BETTYS_BACKYARD_TOMATOES({
        Entity(UUID.randomUUID().toString(), "Betty's tomato plants")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(character = 't', foregroundRGB = RGB(153, 0, 0)))
    }),
    BETTYS_BACKYARD_HERBS({
        Entity(UUID.randomUUID().toString(), "Betty's homegrown greens")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(character = 'h', foregroundRGB = RGB(0, 153, 51)))
    }),
    BETTYS_BACKYARD_FRUITS({
        Entity(UUID.randomUUID().toString(), "Betty's fruit tree")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'F', foregroundRGB = RGB(0, 150, 51)))
    }),
    CURTIS_STREET_MY_BED({
        Entity(UUID.randomUUID().toString(), "Your Teenage Bed")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(character = 'B', foregroundRGB = RGB(0, 173, 238)))
    }),
    CURTIS_STREET_MY_DRESSER({
        Entity(UUID.randomUUID().toString(), "Your Teenage Dresser")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(character = 'D', foregroundRGB = RGB(0, 173, 238)))
    }),
    CURTIS_STREET_ALEXS_BED({
        Entity(UUID.randomUUID().toString(), "Alex's Childhood Bed")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'B', foregroundRGB = RGB(0, 139, 0)))
    }),
    CURTIS_STREET_BIG_TV({
        Entity(UUID.randomUUID().toString(), "The Big Television")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'T', foregroundRGB = RGB(0, 0, 0)))
    }),
    CURTIS_STREET_PLAYSTATION({
        Entity(UUID.randomUUID().toString(), "The playstation 2")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(character = 'p', foregroundRGB = RGB(0, 0, 0)))
    }),
    CURTIS_STREET_GAMECUBE({
        Entity(UUID.randomUUID().toString(), "The gamecube")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(character = 'g', foregroundRGB = RGB(0, 0, 0)))
    }),
    CURTIS_STREET_ANIME_SHELF({
        Entity(UUID.randomUUID().toString(), "Your anime shelf")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(character = 'A', foregroundRGB = RGB(153, 51, 0)))
    }),
    CURTIS_STREET_DDS_COUCH({
        Entity(UUID.randomUUID().toString(), "A Segment Of The TV Couch")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'C', foregroundRGB = RGB(26, 26, 255)))
    }),
    CURTIS_STREET_MIDDLE_TOILET({
        Entity(UUID.randomUUID().toString(), "The Middle Toilet")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(character = 'O', foregroundRGB = RGB(0, 51, 51)))
    }),
    CURTIS_STREET_MIDDLE_SHOWER({
        Entity(UUID.randomUUID().toString(), "The Middle Shower")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'S', foregroundRGB = RGB(204, 255, 255)))
    }),
    CURTIS_STREET_MIDDLE_SINK({
        Entity(UUID.randomUUID().toString(), "The Middle Sink")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(character = 'I', foregroundRGB = RGB(0, 102, 0)))
    }),
    CURTIS_STREET_SMALL_PLASITC_TRASH_BIN({
        Entity(UUID.randomUUID().toString(), "One Of The Tiny Trash Bins")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(character = 'b', foregroundRGB = RGB(0, 10, 26)))
    }),
    CURTIS_STREET_MIDDLE_MIRROR({
        Entity(UUID.randomUUID().toString(), "Your Favourite Mirror")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(character = 'm', foregroundRGB = RGB(255, 255, 255)))
    }),
    HOSPITAL_ER_CHAIR({
        Entity(UUID.randomUUID().toString(), "An ER chair")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(character = 'c', foregroundRGB = RGB(51, 119, 255)))
    }),
    HOSPITAL_ER_RECEPTIONIST({
        Entity(UUID.randomUUID().toString(), "The ER receptionist")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(character = 'R', foregroundRGB = RGB(153, 187, 255)))
    }),
    HOSPITAL_ER_ELDERLY_COUGHER({
        Entity(UUID.randomUUID().toString(), "An elderly cougher")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(character = 'G', foregroundRGB = RGB(255, 0, 0)))
    }),
    HOSPITAL_ER_BABY({
        Entity(UUID.randomUUID().toString(), "A baby, in a pram")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(character = 'B', foregroundRGB = RGB(255, 0, 0)))
    }),
    HOSPITAL_ER_PARENT({
        Entity(UUID.randomUUID().toString(), "A worried parent")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(character = 'P', foregroundRGB = RGB(0, 102, 0)))
    }),
    HOSPITAL_CURTAINS({
        Entity(UUID.randomUUID().toString(), "Hospital curtains")
            .addComponent(CollisionComponent.fog())
            .addComponent(DisplayComponent(character = 'c', foregroundRGB = RGB(51, 153, 255)))
    }),
    HOSPITAL_MACHINERY({
        Entity(UUID.randomUUID().toString(), "A bedside machine")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(character = 'M', foregroundRGB = RGB(51, 153, 255)))
    }),
    HOSPITAL_BED({
        Entity(UUID.randomUUID().toString(), "A hospital bed")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'B', foregroundRGB = RGB(51, 153, 255)))
    }),
    HOSPITAL_FOOD({
        Entity(UUID.randomUUID().toString(), "Lunch!")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(character = 'f', foregroundRGB = RGB(51, 153, 255)))
    }),
    AN_UNFAMILIAR_STREET_LIGHT({
        Entity(UUID.randomUUID().toString(), "A harsh light")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'L', foregroundRGB = RGB(0, 0, 0)))
    }),
    AN_UNFAMILIAR_CAR({
        Entity(UUID.randomUUID().toString(), "A car, strangely twisted")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'C', foregroundRGB = RGB(0, 0, 0)))
    }),
    A_FAMILIAR_FIGURE({
        Entity(UUID.randomUUID().toString(), "A familiar figure")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'P', foregroundRGB = RGB(0, 0, 0)))
            .addComponent(com.mtw.supplier.ecs.components.ai.FamiliarFigureAIComponent())
            .addComponent(SpeedComponent(150))
            .addComponent(ActionTimeComponent(150))
    }),
    ALEXANDER({
        Entity(UUID.randomUUID().toString(), "Alexander")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(character = 'A', foregroundRGB = RGB(0, 51, 0)))
    })
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
    BETTYS_LIVING_ROOM(DreamRoomBlueprintData(
        "Juliann's old house",
        "Well, actually Juliann's mom's house. You used to come here all the time, when you were kids. " +
            "You never see her anymore. Strange. Is that resentment you feel?",
        minWidth = 10, maxWidth = 10,
        minHeight = 14, maxHeight = 14,
        wallColor = TileColor.create(128, 128, 255),
        floorColor = TileColor.create(133, 94, 66),
        entities = listOf(
            EntityBlueprint.BETTYS_LIVING_ROOM_TV, 
            EntityBlueprint.BETTYS_LIVING_ROOM_COUCH,
            EntityBlueprint.BETTYS_LIVING_ROOM_COUCH,
            EntityBlueprint.BETTYS_LIVING_ROOM_BOOKSHELVES,
            EntityBlueprint.BETTYS_LIVING_ROOM_BOOKSHELVES
        ))),
    BETTYS_BACKYARD(DreamRoomBlueprintData(
        "Juliann's old backyard",
        "Betty and Lawrence never liked answering the front door, so when you were going over to " +
            "Juliann's house you always went through the backyard.",
        minWidth = 10, maxWidth = 10,
        minHeight = 14, maxHeight = 14,
        wallColor = TileColor.create(128, 128, 255),
        floorColor = TileColor.create(133, 94, 66),
        entities = listOf(
            EntityBlueprint.BETTYS_BACKYARD_LAUNDRY,
            EntityBlueprint.BETTYS_BACKYARD_LAUNDRY,
            EntityBlueprint.BETTYS_BACKYARD_TOMATOES,
            EntityBlueprint.BETTYS_BACKYARD_HERBS,
            EntityBlueprint.BETTYS_BACKYARD_FRUITS
        ))),
    CURTIS_STREET_BEDROOM(DreamRoomBlueprintData(
        "Your old bedroom",
        "You shared it with Alex until you left for college, aside from those years you stole the TV " +
            "room for yourself. You always found it kind of weird that your parents didn't mind making a brother and " +
            "sister share rooms.",
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
        "The down-downstairs TV room",
        "When you were fighting with your parents, you abandoned your room. You slept on this couch, " +
            "which you liked better than your bed. You used to try and play video games on the TV, deep in the night " +
            "when you parents were asleep.",
        minWidth = 7, maxWidth = 9,
        minHeight = 14, maxHeight = 16,
        wallColor = TileColor.create(255, 153, 204), // light-pink-ish
        floorColor = TileColor.create(255, 153, 204), // hopefully hardwood-ish
        entities = listOf(
            EntityBlueprint.CURTIS_STREET_BIG_TV,
            EntityBlueprint.CURTIS_STREET_GAMECUBE,
            EntityBlueprint.CURTIS_STREET_PLAYSTATION,
            EntityBlueprint.CURTIS_STREET_ANIME_SHELF,
            EntityBlueprint.CURTIS_STREET_DDS_COUCH,
            EntityBlueprint.CURTIS_STREET_DDS_COUCH,
            EntityBlueprint.CURTIS_STREET_DDS_COUCH
        ))),
    CURTIS_STREET_MIDDLE_BATHROOM(DreamRoomBlueprintData(
        "The middle bathroom",
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
            EntityBlueprint.CURTIS_STREET_SMALL_PLASITC_TRASH_BIN,
            EntityBlueprint.CURTIS_STREET_MIDDLE_MIRROR
        ))),
    ASTHMA_HOSPITAL_EMERGENCY_ROOM(DreamRoomBlueprintData(
        "Emergency Room",
        "Your wheezing is terrifying in your young ears. Every breath results in a huge, hacking cough. " +
            "Your mom is frantic. You can't breathe. You're dying.",
        minWidth = 25, maxWidth = 30,
        minHeight = 45, maxHeight = 50,
        wallColor = TileColor.create(15, 15, 15),
        floorColor = TileColor.create(0, 0, 0), // hopefully wood veneer-ish
        entities = listOf(
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_CHAIR,
            EntityBlueprint.HOSPITAL_ER_RECEPTIONIST,
            EntityBlueprint.HOSPITAL_ER_ELDERLY_COUGHER,
            EntityBlueprint.HOSPITAL_ER_ELDERLY_COUGHER,
            EntityBlueprint.HOSPITAL_ER_ELDERLY_COUGHER,
            EntityBlueprint.HOSPITAL_ER_ELDERLY_COUGHER,
            EntityBlueprint.HOSPITAL_ER_BABY,
            EntityBlueprint.HOSPITAL_ER_BABY,
            EntityBlueprint.HOSPITAL_ER_PARENT,
            EntityBlueprint.HOSPITAL_ER_PARENT
        ))),
    ASTHMA_HOSPITAL_WARD(DreamRoomBlueprintData(
        "Pediatric Ward",
        "They've hooked some tubing up your nose and given you an oxygen tank to wheel around. The tank " +
            "is no trouble, and you like the hospital food. And you don't need to go to school. You decide you like " +
            "being hospitalized, after all.",
        minWidth = 5, maxWidth = 5,
        minHeight = 9, maxHeight = 9,
        wallColor = TileColor.create(210, 210, 210),
        floorColor = TileColor.create(255, 255, 255),
        entities = listOf(
            EntityBlueprint.HOSPITAL_CURTAINS,
            EntityBlueprint.HOSPITAL_MACHINERY,
            EntityBlueprint.HOSPITAL_BED,
            EntityBlueprint.HOSPITAL_FOOD
        ))),
    AN_UNFAMILIAR_STREET(DreamRoomBlueprintData(
        "An unfamiliar street",
        "You don't feel safe here.",
        minWidth = 28, maxWidth = 36,
        minHeight = 5, maxHeight = 5,
        wallColor = TileColor.create(0, 0, 0),
        floorColor = TileColor.create(105, 105, 105),
        entities = listOf(
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_CAR,
            EntityBlueprint.AN_UNFAMILIAR_STREET_LIGHT,
            EntityBlueprint.AN_UNFAMILIAR_STREET_LIGHT,
            EntityBlueprint.AN_UNFAMILIAR_STREET_LIGHT,
            EntityBlueprint.AN_UNFAMILIAR_STREET_LIGHT,
            EntityBlueprint.A_FAMILIAR_FIGURE
        ))),
    AN_UNFAMILIAR_APARTMENT(DreamRoomBlueprintData(
        "An unfamiliar apartment",
        "You're trapped. The walls are too close; he is too close. You can't breathe.",
        minWidth = 6, maxWidth = 6,
        minHeight = 6, maxHeight = 6,
        wallColor = TileColor.create(0, 0, 0),
        floorColor = TileColor.create(255, 255, 255),
        entities = listOf(
            EntityBlueprint.A_FAMILIAR_FIGURE
        ))),
    A_FAMILIAR_CAR(DreamRoomBlueprintData(
        "A familiar car",
        "You felt safe in here, once, but now - you feel the stress, in your heart, in your rasping " +
            "breath. Alex is here, he grabs your hand. You'll be safe. You'll be safe as long as you stay with Alex.",
        minWidth = 4, maxWidth = 4,
        minHeight = 4, maxHeight = 4,
        wallColor = TileColor.create(0, 0, 0),
        floorColor = TileColor.create(255, 255, 255),
        entities = listOf(
            EntityBlueprint.A_FAMILIAR_FIGURE,
            EntityBlueprint.ALEXANDER
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
        this.name = data.name
        this.commentary = data.commentary
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
                .addComponent(CollisionComponent.blocker())
                .addComponent(DoorComponent(direction))
            doors[direction] = door
            door
        } else {
            Entity(UUID.randomUUID().toString(), "Wall")
                .addComponent(CollisionComponent.blocker())
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