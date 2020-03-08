package com.mtw.supplier.encounter.state.map.blueprint

import com.mtw.supplier.ecs.components.RGB
import kotlinx.serialization.Serializable
import org.hexworks.zircon.api.color.TileColor

@Serializable
enum class ThemeTags(val tileColor: TileColor) {
    // Entity Tags
    YOU(TileColor.create(0, 0, 255)),
    ALEX(TileColor.create(0, 51, 0)),
    ER_ENTITY(TileColor.create(51, 153, 255)),

    // Room Tags
    CURTIS_ST(TileColor.create(255, 153, 204)),
    JULIANN(TileColor.create(128, 128, 255)),
    HOSPITAL(TileColor.create(15, 15, 15)),
    VEGAS(TileColor.create(230, 0, 0)),

    // Entity & Room Tags
    FAMILIAR_FIGURE(TileColor.create(0, 0, 0));

    val rgb: RGB
        get() = RGB.fromTileColor(this.tileColor)
}

data class DreamRoomBlueprintData(
    val name: String,
    val commentary: String,
    val minWidth: Int,
    val maxWidth: Int,
    val minHeight: Int,
    val maxHeight: Int,
    val wallColor: TileColor,
    val entities: List<EntityBlueprint>,
    val tags: List<ThemeTags>
)

enum class DreamRoomBlueprint(val blueprintData: DreamRoomBlueprintData) {
    BETTYS_LIVING_ROOM(DreamRoomBlueprintData(
        "Juliann's old living room",
        "Well, actually Juliann's mom's living room. You used to come here all the time, when you were" +
            " kids. You never see her anymore. She always has something scheduled. She's very popular.",
        minWidth = 10, maxWidth = 10,
        minHeight = 14, maxHeight = 14,
        wallColor = ThemeTags.JULIANN.tileColor,
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
        tags = listOf(ThemeTags.JULIANN))),
    BETTYS_BACKYARD(DreamRoomBlueprintData(
        "Juliann's old backyard",
        "Betty and Lawrence never liked answering the front door, so when you were going over to " +
            "Juliann's house you always went through the backyard.",
        minWidth = 10, maxWidth = 10,
        minHeight = 14, maxHeight = 14,
        wallColor = ThemeTags.JULIANN.tileColor,
        entities = listOf(
            EntityBlueprint.BETTYS_BACKYARD_LAUNDRY,
            EntityBlueprint.BETTYS_BACKYARD_LAUNDRY,
            EntityBlueprint.BETTYS_BACKYARD_TOMATOES,
            EntityBlueprint.BETTYS_BACKYARD_HERBS,
            EntityBlueprint.BETTYS_BACKYARD_FRUITS
        ),
        tags = listOf(ThemeTags.JULIANN))),
    JULIANNS_APARTMENT(DreamRoomBlueprintData(
        "Juliann's old apartment",
        "It's small, but lively. There are two bedrooms and you know she was sharing it with a college " +
            "friend, but you never met her roommate. It's clean and well-organized.",
        minWidth = 8, maxWidth = 8,
        minHeight = 8, maxHeight = 8,
        wallColor = ThemeTags.JULIANN.tileColor,
        entities = listOf(
            EntityBlueprint.JULIANNS_DESK,
            EntityBlueprint.JULIANNS_LAPTOP,
            EntityBlueprint.JULIANNS_TABLE,
            EntityBlueprint.JULIANNS_COOKING_UTENSILS,
            EntityBlueprint.JULIANNS_FOLDING_CHAIRS
        ),
        tags = listOf(ThemeTags.JULIANN))),
    CURTIS_STREET_BEDROOM(DreamRoomBlueprintData(
        "Your old bedroom",
        "You shared it with Alex until you left for college, aside from those years you stole the TV " +
            "room for yourself. You always found it kind of weird that your parents didn't mind making a brother and " +
            "sister share rooms.",
        minWidth = 8, maxWidth = 8,
        minHeight = 5, maxHeight = 5,
        wallColor = TileColor.create(255, 153, 204),
        // hopefully wood veneer-ish
        entities = listOf(
            EntityBlueprint.CURTIS_STREET_YOUR_OLD_DESK,
            EntityBlueprint.CURTIS_STREET_ALEXS_OLD_DESK,
            EntityBlueprint.CURTIS_STREET_SMALL_PLASITC_TRASH_BIN,
            EntityBlueprint.CURTIS_STREET_MY_BED,
            EntityBlueprint.CURTIS_STREET_MY_DRESSER,
            EntityBlueprint.CURTIS_STREET_ALEXS_BED
        ),
        tags = listOf(ThemeTags.CURTIS_ST))),
    CURTIS_STREET_DOWN_DOWNSTAIRS(DreamRoomBlueprintData(
        "The down-downstairs TV room",
        "When you were fighting with your parents, you abandoned your room. You slept on this couch, " +
            "which you liked better than your bed. You used to try and play video games on the TV, deep in the night " +
            "when you parents were asleep.",
        minWidth = 7, maxWidth = 9,
        minHeight = 14, maxHeight = 16,
        wallColor = TileColor.create(255, 153, 204), // light-pink-ish
        // hopefully hardwood-ish
        entities = listOf(
            EntityBlueprint.CURTIS_STREET_SMALL_PLASITC_TRASH_BIN,
            EntityBlueprint.CURTIS_STREET_BIG_TV,
            EntityBlueprint.CURTIS_STREET_GAMECUBE,
            EntityBlueprint.CURTIS_STREET_PLAYSTATION,
            EntityBlueprint.CURTIS_STREET_ANIME_SHELF,
            EntityBlueprint.CURTIS_STREET_DDS_COUCH,
            EntityBlueprint.CURTIS_STREET_DDS_COUCH,
            EntityBlueprint.CURTIS_STREET_DDS_COUCH
        ),
        tags = listOf(ThemeTags.CURTIS_ST))),
    CURTIS_STREET_MIDDLE_BATHROOM(DreamRoomBlueprintData(
        "The middle bathroom",
        "This was your favorite bathroom. The shower was nice and hot, there was plenty of space near " +
            "the sink, and the mirror was huge and always clear. You feel comfortable here.",
        minWidth = 5, maxWidth = 5,
        minHeight = 7, maxHeight = 7,
        wallColor = TileColor.create(255, 153, 204), // light-pink-ish
        // hopefully hardwood-ish
        entities = listOf(
            EntityBlueprint.CURTIS_STREET_SMALL_PLASITC_TRASH_BIN,
            EntityBlueprint.CURTIS_STREET_MIDDLE_TOILET,
            EntityBlueprint.CURTIS_STREET_MIDDLE_SHOWER,
            EntityBlueprint.CURTIS_STREET_MIDDLE_SINK,
            EntityBlueprint.CURTIS_STREET_MIDDLE_MIRROR
        ),
        tags = listOf(ThemeTags.CURTIS_ST))),
    ASTHMA_HOSPITAL_EMERGENCY_ROOM(DreamRoomBlueprintData(
        "Emergency Room",
        "Your wheezing is terrifying in your young ears. Every breath results in a huge, hacking cough. " +
            "Your mom is frantic. You can't breathe. You're dying.",
        minWidth = 25, maxWidth = 30,
        minHeight = 45, maxHeight = 50,
        wallColor = ThemeTags.HOSPITAL.tileColor,
        // hopefully wood veneer-ish
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
        ), tags = listOf(ThemeTags.HOSPITAL))),
    ASTHMA_HOSPITAL_WARD(DreamRoomBlueprintData(
        "Pediatric Ward",
        "They've hooked some tubing up your nose and given you an oxygen tank to wheel around. The tank " +
            "is no trouble, and you like the hospital food. And you don't need to go to school. You decide you like " +
            "being hospitalized, after all.",
        minWidth = 5, maxWidth = 5,
        minHeight = 9, maxHeight = 9,
        wallColor = ThemeTags.HOSPITAL.tileColor,
        entities = listOf(
            EntityBlueprint.HOSPITAL_CURTAINS,
            EntityBlueprint.HOSPITAL_MACHINERY,
            EntityBlueprint.HOSPITAL_BED,
            EntityBlueprint.HOSPITAL_FOOD
        ),
        tags = listOf(ThemeTags.HOSPITAL))),
    VEGAS_THE_STRIP(DreamRoomBlueprintData(
        "The Vegas Strip",
        "The strip is alive at night and you hate it. There are misters spraying water onto patrons, and " +
            "beautiful, buff men in some sort of weird policeman fetish costumes and impossibly tall and thin women " +
            "in entirely too little clothing and you want to shrink away and hide.",
        minWidth = 50, maxWidth = 60,
        minHeight = 6, maxHeight = 6,
        wallColor = ThemeTags.FAMILIAR_FIGURE.tileColor,
        entities = listOf(
            EntityBlueprint.VEGAS_STRIP_COLLEGE_GIRL,
            EntityBlueprint.VEGAS_STRIP_COLLEGE_GIRL,
            EntityBlueprint.VEGAS_STRIP_COLLEGE_GIRL,
            EntityBlueprint.VEGAS_STRIP_COLLEGE_GIRL,
            EntityBlueprint.VEGAS_STRIP_COLLEGE_GIRL,
            EntityBlueprint.VEGAS_STRIP_COSTUME_POLICEMAN,
            EntityBlueprint.VEGAS_STRIP_COSTUME_POLICEMAN,
            EntityBlueprint.VEGAS_STRIP_COSTUME_POLICEMAN,
            EntityBlueprint.VEGAS_STRIP_COSTUME_BIRD_OF_PARADISE,
            EntityBlueprint.VEGAS_STRIP_COSTUME_BIRD_OF_PARADISE,
            EntityBlueprint.VEGAS_STRIP_COSTUME_BIRD_OF_PARADISE,
            EntityBlueprint.VEGAS_STRIP_COLLEGE_BOY,
            EntityBlueprint.VEGAS_STRIP_COLLEGE_BOY,
            EntityBlueprint.VEGAS_STRIP_COLLEGE_BOY,
            EntityBlueprint.VEGAS_STRIP_COLLEGE_BOY,
            EntityBlueprint.VEGAS_STRIP_COLLEGE_BOY,
            EntityBlueprint.VEGAS_STRIP_COLLEGE_BOY,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST,
            EntityBlueprint.VEGAS_STRIP_MIDDLE_AGED_TOURIST
        ),
        tags = listOf(ThemeTags.VEGAS))),
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
