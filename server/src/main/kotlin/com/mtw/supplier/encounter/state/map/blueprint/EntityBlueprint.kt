package com.mtw.supplier.encounter.state.map.blueprint

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.encounter.rulebook.actions.TerrorChangeStats
import org.hexworks.cobalt.core.api.UUID


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
                        TerrorChangeMemory("Bush, and the years since", TerrorChangeStats(4, 0, 80,
                            "Not worth thinking about."))),
                    InspectEvent("The 1984 Dune movie",
                        "You have no idea how it came to be but you watched the 1984 version of Dune once at Juliann's house. " +
                            "Betty made hot chocolate. It was great and you both loved it.",
                        TerrorChangeStats(-2, 0, 100, "Good times."),
                        TerrorChangeMemory("Dune, 1984", TerrorChangeStats(-1, 0, 100,
                            "That was such a weird movie.")))
                )))
    }),
    BETTYS_LIVING_ROOM_COUCH({
        Entity(UUID.randomUUID().toString(), "Betty's living room couches")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'C', foregroundRGB = RGB(10, 10, 41)))
            .addComponent(InspectableComponent(
                "Betty's couches",
                "They weren't particularly nice, but they were clean.",
                mutableListOf()))
    }),
    BETTYS_LIVING_ROOM_BOOKSHELVES({
        Entity(UUID.randomUUID().toString(), "Betty's living room bookshelves")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'S', foregroundRGB = RGB(153, 51, 0)))
            .addComponent(InspectableComponent(
                "Betty's living room bookshelves",
                "Alongside Juliann's childhood picture books, it was mostly textbooks. Betty and Lawrence " +
                    "took school very seriously, as immigrant Chinese often do. You didn't have it so bad but Juliann " +
                    "had it awful, though Juliann was very studious and intelligent so she never disappointed. Not " +
                    "until later, when she went off to follow her passions instead of making lots of money with her " +
                    "math degree.",
                mutableListOf(
                    InspectEvent("The bookshelf, full of schoolbooks",
                        "Your eyes catch the titles of some of Juliann's old textbooks. Years after you'd " +
                            "both graduated, you'd gone with Betty into her garage to fetch - something, you don't " +
                            "know what. She was moving some boxes out of the way. " +
                            "You asked her what was in there, and she said it was Juliann's old schoolwork. \n \n " +
                            "There were, like, seven boxes of it. \n \n " +
                            "At least your parents weren't like that. Though, maybe they should have been.",
                        TerrorChangeStats(2, 0, 60, "Just a tad creepy."),
                        TerrorChangeMemory("Betty, with Juliann's schoolwork", TerrorChangeStats(9, 0, 100,
                            "The pressure on Juliann was immense.")))
                )))
    }),
    BETTYS_LIVING_ROOM_DESK({
        Entity(UUID.randomUUID().toString(), "Betty's living room desk")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'D', foregroundRGB = RGB(153, 51, 0)))
            .addComponent(InspectableComponent(
                "Betty's living room desk",
                "Incredibly unexceptional, as far as desks go. Covered with paperwork from Betty's business, usually.",
                mutableListOf(
                    InspectEvent("Your mom and Betty, arguing",
                        "Betty took debts very seriously. Somehow Betty owed your mom twenty dollars, probably because " +
                            "they went out to eat together, so one day they were talking and Betty tried to give your " +
                            "mom the money. Your mom was like, oh, it's no big deal! but Betty said it was, and your " +
                            "mom wouldn't back down, and so on, and so on, with them becoming increasingly loud. " +
                            "Eventually, Betty just slapped the twenty dollars down on the table and left the house, " +
                            "even though it was *her house*. \n \n " +
                            "You were trying not to die from laughter the whole time.",
                        TerrorChangeStats(-6, 0, 100, "Hahahahahahaha!"),
                        TerrorChangeMemory("Betty and your mom", TerrorChangeStats(-4, 0, 100,
                            "You hope you can have a friend like that.")))
                )))
    }),
    BETTYS_BACKYARD_LAUNDRY({
        Entity(UUID.randomUUID().toString(), "Laundry drying in the sun")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'L', foregroundRGB = RGB(220, 220, 220)))
            .addComponent(InspectableComponent(
                "Betty's laundry drying poles",
                "She owned a dryer, but she preferred the sun anyways, when it was bright out. Come to think " +
                    "of it, your mom did too.",
                mutableListOf()))
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
            .addComponent(InspectableComponent(
                "The big television",
                "Your parents used to have a smaller one, but they went and bought a new television after " +
                    "you left for college. It was the largest television you'd ever seen in your life.",
                mutableListOf(
                    InspectEvent("A movie with Cesario",
                        "There was a guy, Cesario, in college, who in hindsight was extremely obviously trying to " +
                            "date you, but at the time you literally could not conceive of the possibility. " +
                            "So even after you met his parents, and he met yours, and he " +
                            "was staying over at your place over the weekend and watching a movie with you that " +
                            "you went to the local rental store together to pick, you did " +
                            "not pick up on what was going on. \n \n " +
                            "Eventually, he gave up. You don't know what happened to him.",
                        TerrorChangeStats(-10, 0, 100, "When years later you realized, you felt so happy."),
                        TerrorChangeMemory("A movie with Cesario", TerrorChangeStats(-7, 0, 100,
                            "Honestly you were probably cranky and angry the whole time."))))))
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
                ))
            )
    }),
    CURTIS_STREET_GAMECUBE({
        Entity(UUID.randomUUID().toString(), "The gamecube")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'g', foregroundRGB = RGB(0, 0, 0)))
            .addComponent(InspectableComponent(
                "Your old Gamecube",
                "What games other than Animal Crossing and Smash did this thing even have?",
                mutableListOf(
                    InspectEvent("Alex, practicing Smash",
                        "He was, like, double as good as you at Smash. He was seriously insane. " +
                            "You never even really figured out what wavedashing was but your brother knew. " +
                            "He explained it to you and you kept forgetting. Eventually he just gave up. He ended up" +
                            "going and competing in Smash tournaments (though you don't think he ever won). \n \n " +
                            "You'd sometimes just sit on the couch and watch him practice.",
                        TerrorChangeStats(-4, 0, 100, "Ah, how nostalgic."),
                        TerrorChangeMemory("Watching Alex practice Smash", TerrorChangeStats(-7, 0, 100, "Those were good times."))),
                    InspectEvent("Animal Crossing",
                        "You loved Animal Crossing but there was always this weird pressure to it. " +
                            "Like, the grim ticking of the clock made it anxiety-inducing in a way that you don't think was intended. " +
                            "You can't remember many specifics, though, just flashes - fishing for fossils, the creepy gyroids, Tom Nook. " +
                            "Weird, that you have such an emotional reaction to it but can recall so little.",
                        null,
                        TerrorChangeMemory("Animal Crossing", TerrorChangeStats(2, 0, 60, "You feel guilty leaving them behind.")))
                ))
            )
    }),
    CURTIS_STREET_ANIME_SHELF({
        Entity(UUID.randomUUID().toString(), "Your anime and manga shelf")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'S', foregroundRGB = RGB(153, 51, 0)))
            .addComponent(InspectableComponent(
                "Wasted time and effort",
                "If you'd had more normal hobbies, would you have had more friends? Ha! " +
                    "\"More\" - you had no friends from your high school.",
                mutableListOf(
                    InspectEvent("High school anime club",
                        "It wasn't really a thing. There was one meeting. You don't remember what you watched. You " +
                            "can't even remember any of the people there. \n \n " +
                            "I guess clubs weren't really much of a thing in your school generally, but still! " +
                            "What a pitiful bunch you must have been. \n \n " +
                            "You suppose you should feel hurt but it's just so comical and distant you can't even pull up the strength to care.",
                        null,
                        null),
                    InspectEvent("Lunch, alone",
                        "Sometimes you'd go eat with the nerds who played D&D together, but you weren't really part of their group. " +
                            "They were always, well, nice, but looking back on it it was probably just because you were a girl and not because of any real personal connection. " +
                            "Isn't that depressing? You think you felt that way then, too. \n \n " +
                            "More often you'd walk home for lunch and watch episodes of Pokemon, Sailor Moon, Fruits Basket, or Evangelion on your computer as you ate. \n \n " +
                            "Why the hell did you watch Evangelion? Weird choice, teenage you. You guess misery loves company?",
                        TerrorChangeStats(9, 0, 100, "The loneliness crashes over you like a wave."),
                        TerrorChangeMemory("Lunch and anime", TerrorChangeStats(5, 0, 100, "High school was a real shit time, wasn't it?"))))))
    }),
    CURTIS_STREET_DDS_COUCH({
        Entity(UUID.randomUUID().toString(), "A segment of the TV couch")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'C', foregroundRGB = RGB(26, 26, 255)))
            .addComponent(InspectableComponent(
                "Bad memories and restless nights",
                "You'd rather just not think about all those years. You weren't a great person to be " +
                    "around, then.",
                mutableListOf(
                    InspectEvent("Back on the couch after college",
                        "You didn't find a job out of college. You didn't know how. " +
                            "You had a few interviews, but they went poorly. " +
                            "You can't blame the interviewers. You were at best low in self-confidence, at worst " +
                            "neurotic and self-flagellating. It's kind of incredible what a self-destructive wreck you" +
                            "were. You weren't even socially adept enough to hide it for a few hours. Or maybe, you " +
                            "just didn't care. \n \n " +
                            "There's a gnawing guilt, at the edge of your brain, for what you put your parents through. " +
                            "Those times after college where you moved back in with them were not good for either you " +
                            "or them. \n \n " +
                            "But that was a long time ago. You've got a job, and you're better.",
                        null,
                        TerrorChangeMemory("Failing to get a job", TerrorChangeStats(10, 5, 100, "Even now you feel like a fraud."))),
                    InspectEvent("You slept on this couch in high school",
                        "When you think about it you realize you don't really know why you decided to do that. " +
                            "That was during the middle of high school, when you were depressed. " +
                            "The whole period's hazy in your memory. " +
                            "Your grandpa was living in the guest bedroom, next door to the TV room, and you always felt " +
                            "uneasy when he went into his room in the evening. Like you'd failed him by deciding to sleep " +
                            "on the couch. He would never say that, but you wondered if he felt it. \n \n " +
                            "But that was a long time ago. He's passed, and you're better.",
                        null,
                        TerrorChangeMemory("High school despair", TerrorChangeStats(6, 0, 100, "You don't want to remember that.")))))
            )
    }),
    CURTIS_STREET_MIDDLE_TOILET({
        Entity(UUID.randomUUID().toString(), "The middle toilet")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'O', foregroundRGB = RGB(0, 51, 51)))
            .addComponent(InspectableComponent(
                "Your favourite toilet",
                "It was large, nice, and clean (that last part was because everybody in the house was very" +
                    " clean). Your mom worked very hard at that.",
                mutableListOf())
            )
    }),
    CURTIS_STREET_MIDDLE_SHOWER({
        Entity(UUID.randomUUID().toString(), "Your favourite shower")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'S', foregroundRGB = RGB(204, 255, 255)))
            .addComponent(InspectableComponent(
                "Your favourite shower",
                "For a while, the plumbing was kind of broken and it would never go below a certain heat. " +
                    "The heat was fine by you, though, you liked hot showers. The dermatologist said you shouldn't " +
                    "take hot showers, and you shouldn't take long showers, but you defied both commandments. " +
                    "You went through absurd amounts of moisturizing cream as a result.",
                mutableListOf(
                    InspectEvent("A feeling of warmth and safety",
                        "Nobody will ever bother you when you're showering. It's warm, and the water is hot, and you " +
                            "wish you could stay here, not doing anything, not dealing with anything, not worrying.",
                        TerrorChangeStats(-4, 0, 100, "It's safe, here."),
                        null)))
            )
    }),
    CURTIS_STREET_MIDDLE_SINK({
        Entity(UUID.randomUUID().toString(), "The middle sink")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'I', foregroundRGB = RGB(0, 102, 0)))
            .addComponent(InspectableComponent(
                "The sink in the middle bathroom",
                "It's a nice sink. Mom kept it clean.",
                mutableListOf()
            ))
    }),
    CURTIS_STREET_SMALL_PLASITC_TRASH_BIN({
        Entity(UUID.randomUUID().toString(), "One Of The Tiny Trash Bins")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'b', foregroundRGB = RGB(0, 10, 26)))
            .addComponent(InspectableComponent(
                "One of the tiny trash bins",
                "Mom would empty every trash bin in the house every night, like clockwork. She washed " +
                    "dishes, did laundry, and cleaned the house like it was her job. Well, you suppose it was, to " +
                    "her.",
                mutableListOf()
            ))
    }),
    CURTIS_STREET_MIDDLE_MIRROR({
        Entity(UUID.randomUUID().toString(), "A huge mirror")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'm', foregroundRGB = RGB(255, 255, 255)))
            .addComponent(InspectableComponent(
                "A gigantic, wall-length mirror",
                "Somehow you avoided looking at yourself almost entirely right up until, what? Five years ago?",
                mutableListOf(
                    InspectEvent("A fat, ugly idiot with eyes full of resentment",
                        "Did you really look like that, or is that what you think you would have looked like, if " +
                            "you'd seen yourself? You don't know. You screamed at your parents whenever they took " +
                            "your picture. The only ones you have are family photos, taken with your " +
                            "extended family. Of course you didn't scream then, that would have been disgraceful. " +
                            "But you never went back and looked at those photos. \n \n " +
                            "Your cousins would have photos of their family and extended family on display in their " +
                            "house, full of smiling kids and parents. Your parents never put any photos up in your " +
                            "house. They knew you'd throw a tantrum if they did.",
                        TerrorChangeStats(1, 0, 100, "You think it's probably an illusion."),
                        TerrorChangeMemory("Running from the camera", TerrorChangeStats(13, 0, 100, "They just wanted pictures of their kids."))))
            ))
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
            .addComponent(DisplayComponent(false, character = '!', foregroundRGB = RGB(0, 0, 0)))
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
