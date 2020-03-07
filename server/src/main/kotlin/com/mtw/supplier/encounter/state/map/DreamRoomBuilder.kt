package com.mtw.supplier.encounter.state.map

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.encounter.rulebook.actions.TerrorChangeStats
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.color.TileColor


enum class EntityBlueprint(val createFn: () -> Entity) {
    BETTYS_LIVING_ROOM_TV({
        Entity(UUID.randomUUID().toString(), "Betty's big TV")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'T', foregroundRGB = RGB(0, 0, 0)))
            .addComponent(InspectableComponent(
                "Betty's big TV",
                "It's not actually that big, by modern standards. It's probably, what, 20 years old by now?",
                mutableListOf(
                    InspectEvent("2000 elections, Bush v. Gore",
                        "You remember watching Bush v. Gore at Juliann's house. Juliann's dad was a staunch " +
                            "Republican, and her mom vaguely Democratic. It was a weird night, especially given " +
                            "Florida. That had seemed so wild at the time.",
                        TerrorChangeStats(2, 0, 50, "You feel glum."),
                        null)
                )))
    }),
    BETTYS_LIVING_ROOM_COUCH({
        Entity(UUID.randomUUID().toString(), "Betty's living room couches")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'C', foregroundRGB = RGB(10, 10, 41)))
    }),
    BETTYS_LIVING_ROOM_BOOKSHELVES({
        Entity(UUID.randomUUID().toString(), "Betty's living room bookshelves")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'S', foregroundRGB = RGB(153, 51, 0)))
    }),
    BETTYS_LIVING_ROOM_DESK({
        Entity(UUID.randomUUID().toString(), "Betty's living room desk")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'D', foregroundRGB = RGB(153, 51, 0)))
    }),
    BETTYS_BACKYARD_LAUNDRY({
        Entity(UUID.randomUUID().toString(), "Laundry drying in the sun")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'L', foregroundRGB = RGB(220, 220, 220)))
    }),
    BETTYS_BACKYARD_TOMATOES({
        Entity(UUID.randomUUID().toString(), "Betty's tomato plants")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 't', foregroundRGB = RGB(153, 0, 0)))
    }),
    BETTYS_BACKYARD_HERBS({
        Entity(UUID.randomUUID().toString(), "Betty's homegrown greens")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'h', foregroundRGB = RGB(0, 153, 51)))
    }),
    BETTYS_BACKYARD_FRUITS({
        Entity(UUID.randomUUID().toString(), "Betty's fruit tree")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'F', foregroundRGB = RGB(0, 150, 51)))
    }),
    CURTIS_STREET_MY_BED({
        Entity(UUID.randomUUID().toString(), "Your Teenage Bed")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'B', foregroundRGB = RGB(0, 173, 238)))
    }),
    CURTIS_STREET_MY_DRESSER({
        Entity(UUID.randomUUID().toString(), "Your Teenage Dresser")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'R', foregroundRGB = RGB(0, 173, 238)))
    }),
    CURTIS_STREET_ALEXS_BED({
        Entity(UUID.randomUUID().toString(), "Alex's Childhood Bed")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'B', foregroundRGB = RGB(0, 139, 0)))
    }),
    CURTIS_STREET_BIG_TV({
        Entity(UUID.randomUUID().toString(), "The Big Television")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'T', foregroundRGB = RGB(0, 0, 0)))
    }),
    CURTIS_STREET_PLAYSTATION({
        Entity(UUID.randomUUID().toString(), "The playstation 2")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'p', foregroundRGB = RGB(0, 0, 0)))
            .addComponent(InspectableComponent(
                "Your old PS2",
                "You have fond memories of it, but when you think longer, you wonder if you hadn't " +
                    "spent so much time reading, and playing games, and watching anime, would you have been " +
                    "better-adjusted? Who knows.",
                mutableListOf(
                    InspectEvent("Middle School, a weekend at home",
                        "You took your blankets downstairs, piled them up on the couch, and stayed up all " +
                            "night playing Final Fantasy X. You had to turn the sound off, so your parents wouldn't " +
                            "know (they enforced a limit of 1 hour game time per night). You feel comfortable and safe" +
                            " when you think about it but - you were also depressed, and cranky, and hateful. The" +
                            " lack of sleep, the obsessive escapism, surely couldn't have helped.",
                        TerrorChangeStats(-2, 0, 100, "You feel comforted, despite all that."),
                        TerrorChangeMemory("FFX All-Nighter", TerrorChangeStats(-7, 0, 100, "You wished you could be more like Yuna."))),
                    InspectEvent("Playing Armored Core with Alex after school",
                        "Alex was always more driven than you. More disciplined, more social. He would beat games a " +
                            "lot quicker than you. He would practice more, too, if there was a competitive aspect. " +
                            "You were older, but he was still better than you at games for as long as you can " +
                            "remember. He was better in a lot of other ways, too.",
                        TerrorChangeStats(-5, 0, 100, "You wish Alex were closer."),
                        TerrorChangeMemory("Gaming with Alex", TerrorChangeStats(-15, 0, 100, "One of your fondest childhood memories."))),
                    InspectEvent("Final Fantasy 7",
                        "You didn't play it until years after it came out. In fact, you played FFX first." +
                            " You knew that Aeris would die, but not really how, so despite that you were surprised " +
                            "when it happened. You watched Advent Children, too, and were shocked by how pretty it " +
                            "was. Still, FF7 wasn't that impactful for you. It was nice, you guess.",
                        TerrorChangeStats(-2, 50, 100, "What was the point of all that, in the end?"),
                        TerrorChangeMemory("FF7", TerrorChangeStats(-2, 40, 100, "Did playing FF7 really help you much, in the end?")))
                )))
    }),
    CURTIS_STREET_GAMECUBE({
        Entity(UUID.randomUUID().toString(), "The gamecube")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'g', foregroundRGB = RGB(0, 0, 0)))
    }),
    CURTIS_STREET_ANIME_SHELF({
        Entity(UUID.randomUUID().toString(), "Your anime shelf")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'S', foregroundRGB = RGB(153, 51, 0)))
    }),
    CURTIS_STREET_DDS_COUCH({
        Entity(UUID.randomUUID().toString(), "A Segment Of The TV Couch")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'C', foregroundRGB = RGB(26, 26, 255)))
    }),
    CURTIS_STREET_MIDDLE_TOILET({
        Entity(UUID.randomUUID().toString(), "The Middle Toilet")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'O', foregroundRGB = RGB(0, 51, 51)))
    }),
    CURTIS_STREET_MIDDLE_SHOWER({
        Entity(UUID.randomUUID().toString(), "The Middle Shower")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'S', foregroundRGB = RGB(204, 255, 255)))
    }),
    CURTIS_STREET_MIDDLE_SINK({
        Entity(UUID.randomUUID().toString(), "The Middle Sink")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'I', foregroundRGB = RGB(0, 102, 0)))
    }),
    CURTIS_STREET_SMALL_PLASITC_TRASH_BIN({
        Entity(UUID.randomUUID().toString(), "One Of The Tiny Trash Bins")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'b', foregroundRGB = RGB(0, 10, 26)))
    }),
    CURTIS_STREET_MIDDLE_MIRROR({
        Entity(UUID.randomUUID().toString(), "Your Favourite Mirror")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'm', foregroundRGB = RGB(255, 255, 255)))
    }),
    HOSPITAL_ER_CHAIR({
        Entity(UUID.randomUUID().toString(), "An ER chair")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'c', foregroundRGB = RGB(51, 119, 255)))
    }),
    HOSPITAL_ER_RECEPTIONIST({
        Entity(UUID.randomUUID().toString(), "The ER receptionist")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(false, character = 'R', foregroundRGB = RGB(153, 187, 255)))
    }),
    HOSPITAL_ER_ELDERLY_COUGHER({
        Entity(UUID.randomUUID().toString(), "An elderly cougher")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(false, character = 'G', foregroundRGB = RGB(255, 0, 0)))
    }),
    HOSPITAL_ER_BABY({
        Entity(UUID.randomUUID().toString(), "A baby, in a pram")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(false, character = 'B', foregroundRGB = RGB(255, 0, 0)))
    }),
    HOSPITAL_ER_PARENT({
        Entity(UUID.randomUUID().toString(), "A worried parent")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(false, character = 'P', foregroundRGB = RGB(0, 102, 0)))
    }),
    HOSPITAL_CURTAINS({
        Entity(UUID.randomUUID().toString(), "Hospital curtains")
            .addComponent(CollisionComponent.fog())
            .addComponent(DisplayComponent(true, character = 'c', foregroundRGB = RGB(51, 153, 255)))
    }),
    HOSPITAL_MACHINERY({
        Entity(UUID.randomUUID().toString(), "A bedside machine")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'M', foregroundRGB = RGB(51, 153, 255)))
    }),
    HOSPITAL_BED({
        Entity(UUID.randomUUID().toString(), "A hospital bed")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'B', foregroundRGB = RGB(51, 153, 255)))
    }),
    HOSPITAL_FOOD({
        Entity(UUID.randomUUID().toString(), "Lunch!")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'f', foregroundRGB = RGB(51, 153, 255)))
    }),
    A_FAMILIAR_FIGURE({
        Entity(UUID.randomUUID().toString(), "A familiar figure")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(false, character = 'P', foregroundRGB = RGB(0, 0, 0)))
            .addComponent(com.mtw.supplier.ecs.components.ai.FamiliarFigureAIComponent())
            .addComponent(SpeedComponent(150))
            .addComponent(ActionTimeComponent(150))
    }),
    AN_UNFAMILIAR_STREET_LIGHT({
        Entity(UUID.randomUUID().toString(), "A harsh light")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'L', foregroundRGB = RGB(0, 0, 0)))
    }),
    A_FAMILIAR_CAR({
        Entity(UUID.randomUUID().toString(), "A car you know well")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'C', foregroundRGB = RGB(0, 0, 0)))
    }),
    A_FAMILIAR_CAR_ALEXANDER({
        Entity(UUID.randomUUID().toString(), "Alexander")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(false, character = 'A', foregroundRGB = RGB(0, 51, 0)))
    })
}

enum class RoomTags {
    CURTIS_ST,
    JULIANN
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
    val entities: List<EntityBlueprint>,
    val tags: List<RoomTags> = listOf()
)

enum class DreamRoomBlueprint(val blueprintData: DreamRoomBlueprintData) {
    BETTYS_LIVING_ROOM(DreamRoomBlueprintData(
        "Juliann's old living room",
        "Well, actually Juliann's mom's living room. You used to come here all the time, when you were" +
            " kids. You never see her anymore. She always has something scheduled. She's very popular socially.",
        minWidth = 10, maxWidth = 10,
        minHeight = 14, maxHeight = 14,
        wallColor = TileColor.create(128, 128, 255),
        floorColor = TileColor.create(133, 94, 66),
        entities = listOf(
            EntityBlueprint.BETTYS_LIVING_ROOM_TV, 
            EntityBlueprint.BETTYS_LIVING_ROOM_COUCH,
            EntityBlueprint.BETTYS_LIVING_ROOM_COUCH,
            EntityBlueprint.BETTYS_LIVING_ROOM_COUCH,
            EntityBlueprint.BETTYS_LIVING_ROOM_COUCH,
            EntityBlueprint.BETTYS_LIVING_ROOM_BOOKSHELVES,
            EntityBlueprint.BETTYS_LIVING_ROOM_BOOKSHELVES,
            EntityBlueprint.BETTYS_LIVING_ROOM_BOOKSHELVES,
            EntityBlueprint.BETTYS_LIVING_ROOM_DESK
        ),
        tags = listOf(RoomTags.JULIANN))),
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
        ),
        tags = listOf(RoomTags.JULIANN))),
    /*CURTIS_STREET_BEDROOM(DreamRoomBlueprintData(
        "Your old bedroom",
        "You shared it with Alex until you left for college, aside from those years you stole the TV " +
            "room for yourself. You always found it kind of weird that your parents didn't mind making a brother and " +
            "sister share rooms.",
        minWidth = 8, maxWidth = 8,
        minHeight = 5, maxHeight = 5,
        wallColor = TileColor.create(255, 153, 204),
        floorColor = TileColor.create(133, 94, 66), // hopefully wood veneer-ish
        entities = listOf(
            EntityBlueprint.CURTIS_STREET_MY_BED,
            EntityBlueprint.CURTIS_STREET_MY_DRESSER,
            EntityBlueprint.CURTIS_STREET_ALEXS_BED
        ),
        tags = listOf(RoomTags.CURTIS_ST))),*/
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
        ),
        tags = listOf(RoomTags.CURTIS_ST))),
    /*CURTIS_STREET_MIDDLE_BATHROOM(DreamRoomBlueprintData(
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
        ),
        tags = listOf(RoomTags.CURTIS_ST))),*/
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
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
            EntityBlueprint.A_FAMILIAR_CAR,
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
            EntityBlueprint.A_FAMILIAR_CAR_ALEXANDER
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
    val entityBlueprints: MutableList<EntityBlueprint> = mutableListOf(),
    val tags: MutableList<RoomTags> = mutableListOf()
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
        this.tags.addAll(data.tags)
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
            tags
        )

        buildWalls(room)
        for (blueprint in this.entityBlueprints) {
            val entity = blueprint.createFn()
            room.placeEntity(entity, room.randomPlacementPosition()!!, false)
        }

        return room
    }
}