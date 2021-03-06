package com.mtw.supplier.encounter.state.map.blueprint

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.ecs.components.ai.*
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
            .addComponent(InspectableComponent(
                "Betty's tomato plants",
                "She likes gardening.",
                mutableListOf()))
    }),
    BETTYS_BACKYARD_HERBS({
        Entity(UUID.randomUUID().toString(), "Betty's homegrown greens")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'h', foregroundRGB = RGB(0, 153, 51)))
            .addComponent(InspectableComponent(
                "Betty's greens",
                "You're not entirely sure what kind of plant this is, but she made potstickers once with " +
                    "these and gave you some. They were pretty good but you cooked them wrong so some of them fell " +
                    "apart. You made sure to thank her anyways.",
                mutableListOf()))
    }),
    BETTYS_BACKYARD_FRUITS({
        Entity(UUID.randomUUID().toString(), "Betty's fruit tree")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'F', foregroundRGB = RGB(0, 150, 51)))
            .addComponent(InspectableComponent(
                "Betty's fruit tree",
                "You know, you should really know what kind of fruit this thing gives by now.",
                mutableListOf()))
    }),
    JULIANNS_DESK({
        Entity(UUID.randomUUID().toString(), "Your old desk")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'D', foregroundRGB = ThemeTag.JULIANN.rgb))
            .addComponent(InspectableComponent(
                "Your old desk",
                "You never used it anyways, so Juliann took it. " +
                    "You're not sure what happened to Alex's old desk.",
                mutableListOf()))
    }),
    JULIANNS_LAPTOP({
        Entity(UUID.randomUUID().toString(), "Juliann's old laptop")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'l', foregroundRGB = ThemeTag.JULIANN.rgb))
            .addComponent(InspectableComponent(
                "Juliann's old laptop",
                "It's a macbook of some persuasion.",
                mutableListOf(
                    InspectEvent("You arrived early once",
                        "Like, silly early. More than half an hour. " +
                            "You weren't really sure to what - you were gonna get dinner together, or something. " +
                            "Juliann let you in, and apologized, and said she had a scheduled a call with this person " +
                            "from Chile she'd met when she was doing her NGO work, and asked if you needed anything. " +
                            "You said no, then sat awkwardly out of camera, fiddling with your phone, as she talked in " +
                            "Spanish to her friend. \n \n " +
                            "You resented this random Chilean, even though he'd done nothing wrong. You resented " +
                            "Juliann a little too, even though she'd done nothing wrong. " +
                            "You tried to kill that feeling but feelings are hard to kill sometimes.",
                        TerrorChangeStats(1, 0, 70, "What a terrible thing to think."),
                        TerrorChangeMemory("Juliann and the Chilean", TerrorChangeStats(2, 0, 80,
                            "You wish you could never be resentful again.")))
                )))
    }),
    JULIANNS_TABLE({
        Entity(UUID.randomUUID().toString(), "Juliann's apartment table")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'T', foregroundRGB = ThemeTag.JULIANN.rgb))
            .addComponent(InspectableComponent(
                "Juliann's apartment table",
                "It's a inexpensive folding table, though it's surprisingly sturdy. She's put a nice " +
                    "tablecloth over it.",
                mutableListOf(
                    InspectEvent("Board games with her friends",
                        "She invited you to play board games, once. It was her and two of her friends. " +
                            "You weren't super charming or anything but as far as you can tell you didn't bring shame " +
                            "upon either Juliann or yourself, and you had a good time, so that was a success.",
                        TerrorChangeStats(-4, 0, 100, "A tiny step on a long path."),
                        TerrorChangeMemory("Board games with Juliann", TerrorChangeStats(-6, 25, 100,
                            "You got better at it eventually.")))
                )))
    }),
    JULIANNS_COOKING_UTENSILS({
        Entity(UUID.randomUUID().toString(), "Juliann's kitchen equipment")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'K', foregroundRGB = ThemeTag.JULIANN.rgb))
            .addComponent(InspectableComponent(
                "Juliann's kitchen equipment",
                "It's an impressive array, to be sure. " +
                    "She's got a battery of pie pans (she loves baking) and some nice knives and some more obscure " +
                    "stuff, like a drum sieve. Got a nice stand mixer, too.",
                mutableListOf(
                    InspectEvent("Juliann loved to cook",
                        "She cooked with her mom, a lot, and she was good at it. " +
                            "You always felt a little jealous. That was something that...you felt mixed messages on. " +
                            "Both your moms took being a wife very seriously, which isn't really something that American " +
                            "girls get told explicitly, and a lot of the messaging is anti-that, but then, " +
                            "it's still there and it still basically expects wives to " +
                            "keep up the house and manage the children, and so the fact that you never really learned " +
                            "to cook felt like a failure. \n \n " +
                            "And then, to double the insult, cooking for yourself became popular and cool and upper " +
                            "class, and like half of the younger men at work are way better cooks than you and ugh.",
                        TerrorChangeStats(2, 0, 75, "Bah!"),
                        TerrorChangeMemory("Cooking is cool, now", TerrorChangeStats(5, 25, 100,
                            "That's how it goes, but you don't like it.")))
                )))
    }),
    JULIANNS_FOLDING_CHAIRS({
        Entity(UUID.randomUUID().toString(), "One of Juliann's chairs")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'c', foregroundRGB = ThemeTag.JULIANN.rgb))
            .addComponent(InspectableComponent(
                "A folding chair",
                "It's cheap and folds for easy storage and transport.",
                mutableListOf()))
    }),
    CURTIS_STREET_YOUR_OLD_DESK({
        Entity(UUID.randomUUID().toString(), "Your old desk")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'D', foregroundRGB = ThemeTag.YOU.rgb))
            .addComponent(InspectableComponent(
                "Your old desk",
                "It's a wooden desk, pretty nice, but a little small for a fully grown adult. " +
                    "There wasn't quite enough leg room for you by the end of high school. It's otherwise unremarkable.",
                mutableListOf(
                    InspectEvent("Studying",
                        "You hated studying in your room. You never much used the desk for that. " +
                            "You would lie on the floor and study, sometimes, or you'd use the living room table. " +
                            "That presented problems when you were fighting with your parents, though. \n \n " +
                            "You never much used the desk for other things, either. " +
                            "Only some light drawing, but you never got enough confidence in your art to think you " +
                            "could make something of it.",
                        null,
                        null)
                )))
    }),
    CURTIS_STREET_ALEXS_OLD_DESK({
        Entity(UUID.randomUUID().toString(), "Alex's old desk")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'D', foregroundRGB = ThemeTag.ALEX.rgb))
            .addComponent(InspectableComponent(
                "Alex's old desk",
                "He, at least, ended up doing well.",
                mutableListOf(
                    InspectEvent("Alex's schoolwork",
                        "He took his classes more seriously. He got some sort of ridiculous GPA in high " +
                            "school, something like 4.3, which was only possible at my school if you took literally " +
                            "all the AP classes possible. And he had a group of friends that he hung out with, many of " +
                            "which he still talks to, in addition to the friends he made in college. " +
                            "And then he got a great job, too, making lots of money, that he enjoys. \n \n " +
                            "You feel like a failure of an older sister. " +
                            "You wish you could've been something other than a burden to him. \n \n " +
                            "You're so, so grateful to him for putting up with you.",
                        TerrorChangeStats(-3, 0, 100, "Studying really did pay off for him."),
                        TerrorChangeMemory("Your brother is so much better", TerrorChangeStats(-8, 0, 100,
                            "You're glad he ended up doing well, despite you.")))
                )))
    }),
    CURTIS_STREET_MY_BED({
        Entity(UUID.randomUUID().toString(), "Your teenage bed")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'B', foregroundRGB = ThemeTag.YOU.rgb))
            .addComponent(InspectableComponent(
                "Your teenaage bed",
                "Not much here but bad memories.",
                mutableListOf(
                    InspectEvent("You and a razor blade",
                        "You only ever had one suicide \"attempt\", and it was a pretty weak one. You took " +
                            "an exacto knife one day, sat in your bed, and scored your wrists. You didn't break the " +
                            "skin. It was mostly to spite your mom. \n \n " +
                            "Your mom freaked out and called the police. " +
                            "You sat in bed as she demanded they come take you away, and then just felt " +
                            "incredibly dumb and ashamed for making the police deal with your mom, who was clearly overreacting. " +
                            "If you were really serious you'd be dead already, couldn't she see that? \n \n " +
                            "You wanted to shout, \"MOM I'M NOT A DANGER TO MYSELF OR OTHERS STOP CALLING THE POLICE!\" \n \n " +
                            "Christ, you were an idiot.",
                        TerrorChangeStats(3, 0, 100, "It's a wonder your mom didn't kill you yourself."),
                        TerrorChangeMemory("Your \"suicide attempt\"", TerrorChangeStats(-6, 0, 100,
                            "You feel terrible but it seems funny to you now."))),
                    InspectEvent("A hole in the wall",
                        "At some point in the midst of the nightmare that was high school you took a dictionary and smashed the wall with it, right behind the head of your bed. " +
                            "It left a sizable dent. Walls are shockingly fragile. Who knew? \n \n " +
                            "You mentioned it to one of the psychiatrists later and smugly added that, of course you " +
                            "didn't punch the wall with your hand, you could hurt yourself that way. \n \n " +
                            "You were a real piece of work, huh.",
                        TerrorChangeStats(-1, 50, 100, "You were a real weird kid."),
                        TerrorChangeMemory("Dictionary versus bedroom wall", TerrorChangeStats(-3, 60, 100,
                            "It's just so absurd.")))
                )))
    }),
    CURTIS_STREET_MY_DRESSER({
        Entity(UUID.randomUUID().toString(), "Your dresser")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'R', foregroundRGB = ThemeTag.YOU.rgb))
            .addComponent(InspectableComponent(
                "Your dresser",
                "The sturdiest dresser you've ever known, your faithful companion.",
                mutableListOf(
                    InspectEvent("You've taken it everywhere",
                        "The dresser is big, wooden, and sturdy. You've taken it everywhere you've moved, except for that one internship in Iowa. " +
                            "Other than that it's followed you from home to college back to your parents' house, to " +
                            "your first apartment, second apartment, third apartment, and finally the house you live in now. " +
                            "It has ample storage space, and is just generally very nice. Never let you down yet.",
                        TerrorChangeStats(-4, 40, 100, "It's a wonder your mom didn't kill you yourself."),
                        null)
                )))
    }),
    CURTIS_STREET_ALEXS_BED({
        Entity(UUID.randomUUID().toString(), "Alex's childhood bed")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'B', foregroundRGB = ThemeTag.ALEX.rgb))
            .addComponent(InspectableComponent(
                "Alex's childhood bed",
                "It is impossible for you to reflect upon this without thinking, \"Wow! that's a creepy " +
                    "thing I did wasn't it?\" Like older siblings don't usually literally steal their younger siblings' " +
                    "beds, much less - like is it weirder if an older brother steals his younger brothers' bed, or an " +
                    "older sister steals her younger brothers' bed? And it's super fucking weird if a brother steals " +
                    "his sisters' bed (which Alex did not do, he's not fucking insane). \n \n " +
                    "Just, like, what was going on with you? Well. A lot of things, frankly. " +
                    "It's a wonder you turned out as high-functioning as you are.",
                mutableListOf(
                    InspectEvent("Stealing your brothers' bed",
                        "At some point you started taking up the habit of just stealing Alex's bed. You'd go to sleep " +
                            "before him, but just...using his bed instead of yours. Or you'd just nap in his bed. Not " +
                            "always, of course, but often enough to be a serious problem. \n \n " +
                            "He wasn't thrilled, but he went along with it. Alex put up with a lot, frankly. \n \n " +
                            "You've never told anybody about this. You wouldn't know how to, at least not without coming across as insane. " +
                            "You don't even know what to make of it yourself.",
                        TerrorChangeStats(-4, 0, 100, "It made you feel safe."),
                        TerrorChangeMemory("Stealing Alex's bed", TerrorChangeStats(-1, 0, 100,
                            "What were you thinking?")))
                )))
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
                            "Honestly you were probably cranky and angry the whole time.")))
                )))
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
                )))
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
                            "More often you'd walk home for lunch and watch episodes of Pokemon, Cardcaptor Sakura, Fruits Basket, or Evangelion on your computer as you ate. \n \n " +
                            "Why the hell did you watch Evangelion? Weird choice, teenage you. You guess misery loves company?",
                        TerrorChangeStats(9, 0, 100, "The loneliness crashes over you like a wave."),
                        TerrorChangeMemory("Lunch and anime", TerrorChangeStats(5, 0, 100, "High school was a real shit time, wasn't it?")))
                )))
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
                        TerrorChangeMemory("High school despair", TerrorChangeStats(6, 0, 100, "You don't want to remember that.")))
                )))
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
        Entity(UUID.randomUUID().toString(), "One of the tiny trash bins")
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
                        TerrorChangeMemory("Running from the camera", TerrorChangeStats(13, 0, 100,
                            "They just wanted pictures of their kids.")))
                )))
    }),
    HOSPITAL_ER_CHAIR({
        Entity(UUID.randomUUID().toString(), "An ER chair")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'c', foregroundRGB = RGB(51, 119, 255)))
            .addComponent(InspectableComponent(
                "An ER chair",
                "An unremarkable chair for a mostly unremarkable ER.",
                mutableListOf()))
    }),
    HOSPITAL_ER_RECEPTIONIST({
        Entity(UUID.randomUUID().toString(), "The ER receptionist")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(false, character = 'R', foregroundRGB = ThemeTag.ER_ENTITY.rgb))
            .addComponent(HospitalErReceptionistAIComponent())
            .addComponent(SpeedComponent(125))
            .addComponent(ActionTimeComponent(125))
            .addComponent(InspectableComponent(
                "A harried receptionist",
                "You don't remember the person, more the idea of a person. She's a wavy figure in blue scrubs, but " +
                    "did she really have blue scrubs? Was it, in fact, a she? Did she pay attention to you and only " +
                    "you?",
                mutableListOf()))
    }),
    HOSPITAL_ER_ELDERLY_COUGHER({
        Entity(UUID.randomUUID().toString(), "An elderly cougher")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(false, character = 'E', foregroundRGB = RGB(255, 0, 0)))
            .addComponent(HospitalErElderlyCougherAIComponent())
            .addComponent(SpeedComponent(250))
            .addComponent(ActionTimeComponent(250))
            .addComponent(InspectableComponent(
                "A dying octogenarian",
                "Lungs wet and full of pus, spewing out fetid air, gasping, dying.",
                mutableListOf()))
    }),
    HOSPITAL_ER_BABY({
        Entity(UUID.randomUUID().toString(), "A baby, in a pram")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(false, character = 'B', foregroundRGB = RGB(255, 0, 0)))
            .addComponent(HospitalErBabyAIComponent())
            .addComponent(SpeedComponent(200))
            .addComponent(ActionTimeComponent(200))
            .addComponent(InspectableComponent(
                "A yellow baby",
                "It looks unhealthy and sickly.",
                mutableListOf()))
    }),
    HOSPITAL_ER_PARENT({
        Entity(UUID.randomUUID().toString(), "A worried parent")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(false, character = 'P', foregroundRGB = RGB(0, 102, 0)))
            .addComponent(HospitalErParentAIComponent())
            .addComponent(SpeedComponent(200))
            .addComponent(ActionTimeComponent(200))
            .addComponent(InspectableComponent(
                "The parents are all frantic",
                "Words are spilling out of their mouths, their eyes full of tears, panic in ruling their voice.",
                mutableListOf()))
    }),
    HOSPITAL_CURTAINS({
        Entity(UUID.randomUUID().toString(), "Hospital curtains")
            .addComponent(CollisionComponent.fog())
            .addComponent(DisplayComponent(true, character = 'c', foregroundRGB = ThemeTag.ER_ENTITY.rgb))
            .addComponent(InspectableComponent(
                "Curtains",
                "They block sight, but not sound. Not that it was much of an issue - you were pretty much " +
                    "alone in the ward. Lucky you.",
                mutableListOf()))
    }),
    HOSPITAL_MACHINERY({
        Entity(UUID.randomUUID().toString(), "A bedside machine")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'M', foregroundRGB = ThemeTag.ER_ENTITY.rgb))
            .addComponent(InspectableComponent(
                "Some sort of machine",
                "You think it measured heart rate, and maybe something to do with the blood. It " +
                    "left you alone, and you left it alone.",
                mutableListOf()))
    }),
    HOSPITAL_BED({
        Entity(UUID.randomUUID().toString(), "A hospital bed")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(true, character = 'B', foregroundRGB = ThemeTag.ER_ENTITY.rgb))
            .addComponent(InspectableComponent(
                "A hospital bed",
                "They never actually restricted you to the bed, and you brought some books. You enjoyed " +
                    "wandering around the ward, dragging your oxygen tank behind you. It was on wheels, of course.",
                mutableListOf(InspectEvent("Wandering around the ward",
                    "You don't remember much, but you do remember two things. First, that it felt safe. " +
                        "Second, that somewhere around the ward, they had a Sega Genesis with Ecco The Dolphin on it. " +
                        "You played it a little, not knowing the name, until one day in college you saw a classmate " +
                        "showing another classmate a video of the game, and you recognized it. \n \n " +
                        "Funny what sticks with you.",
                    TerrorChangeStats(-6, 0, 100, "You like dolphins."),
                    TerrorChangeMemory("The hospital ward", TerrorChangeStats(-15, 0, 100, "Safe and comforting.")))
                )))
    }),
    HOSPITAL_FOOD({
        Entity(UUID.randomUUID().toString(), "Lunch!")
            .addComponent(CollisionComponent.passable())
            .addComponent(DisplayComponent(true, character = 'f', foregroundRGB = ThemeTag.ER_ENTITY.rgb))
            .addComponent(InspectableComponent(
                "Hospital food",
                "You clearly have trash food taste, because you really liked the food.",
                mutableListOf(InspectEvent("They brought you food!",
                    "A real step up from school. You had nothing to do here, and, consequently, nothing that was asked " +
                        "of you. And there were always people checking in. Somehow, you trusted them. " +
                        "Well, they were healthcare professionals, so that seems a safe bet.",
                    TerrorChangeStats(-3, 0, 100, "You liked it here."),
                    TerrorChangeMemory("Lunch at the hospital", TerrorChangeStats(-9, 0, 100, "What's not to like?")))
                )))
    }),
    VEGAS_SMOKE_CLOUD({
        Entity(UUID.randomUUID().toString(), "A cloud of cigarette smoke")
            .addComponent(CollisionComponent.fog())
            .addComponent(DisplayComponent(false, character = 's', foregroundRGB = RGB(128, 0, 0)))
            .addComponent(VegasSmokeCloudAIComponent())
            .addComponent(SpeedComponent(300))
            .addComponent(ActionTimeComponent(300))
            .addComponent(InspectableComponent(
                "A cloud of cigarette smoke",
                "Your eyes water. Your lungs ache. Too much of this will set off you asthma, best be gone.",
                mutableListOf()))
    }),
    VEGAS_SLOT_MACHINE({
        Entity(UUID.randomUUID().toString(), "A flashing slot machine")
            .addComponent(CollisionComponent.fog())
            .addComponent(DisplayComponent(false, character = 'S', foregroundRGB = RGB(128, 0, 0)))
            .addComponent(VegasSlotMachineAIComponent())
            .addComponent(SpeedComponent(300))
            .addComponent(ActionTimeComponent(300))
            .addComponent(InspectableComponent(
                "A flashing slot machine",
                "You don't like slots, or the sounds they make, or the strobing lights they so often have.",
                mutableListOf()))
    }),
    VEGAS_STRIP_COLLEGE_GIRL({
        Entity(UUID.randomUUID().toString(), "A college girl")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(false, character = 'G', foregroundRGB = RGB(128, 0, 0)))
            .addComponent(VegasStripCollegeGirlAIComponent())
            .addComponent(SpeedComponent(100))
            .addComponent(ActionTimeComponent(100))
            .addComponent(InspectableComponent(
                "A laughing girl",
                "You feel like you've failed, standing next to her.",
                mutableListOf(
                    InspectEvent("She's impossibly perfect",
                        "Everything about her is both incredibly beautiful, but unique and authentic. " +
                            "Her style is impeccable, but not derivative. " +
                            "She probably gets men looking her and saying, \"You look beautiful without any makeup on.\" " +
                            "She radiates that impossible effortlessness that only a rare combination of incredible work " +
                            "and natural gifts can achieve.",
                        TerrorChangeStats(15, 0, 100, "You want to run and hide."),
                        TerrorChangeMemory("You feel ashamed of how you look", TerrorChangeStats(15, 0, 100, "You know you shouldn't, but you do."))),
                    InspectEvent("She's surrounded by her friends",
                        "They're laughing, and joking, and smiling. You can't follow what it is that's so hilarious " +
                            "but you suspect you wouldn't really feel the same way if you heard. " +
                            "They look so happy to be here, like they're having the time of their lives.",
                        TerrorChangeStats(20, 0, 100, "You don't belong here with them."),
                        TerrorChangeMemory("College girls in Vegas", TerrorChangeStats(15, 0, 100, "You've never travelled with friends.")))
                    )
                ))
    }),
    VEGAS_STRIP_COLLEGE_BOY({
        Entity(UUID.randomUUID().toString(), "A college-age man")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(false, character = 'M', foregroundRGB = RGB(128, 0, 0)))
            .addComponent(VegasStripCollegeBoyAIComponent())
            .addComponent(SpeedComponent(100))
            .addComponent(ActionTimeComponent(100))
            .addComponent(InspectableComponent(
                "A college-age man",
                "He's out of your league.",
                mutableListOf(
                    InspectEvent("He's impossibly perfect",
                        "He's tall, and his face is covered in a genuine, heartfelt smile. " +
                            "His face is hard to look at directly without turning away in shame. " +
                            "You can see his finely toned muscles on his exposed arms, big enough to be impressive but not so big they're weird. " +
                            "He walks with the carefree strut of somebody who might not have everything today, but will undoutably have everything one day.",
                        TerrorChangeStats(15, 0, 100, "You want to run and hide."),
                        TerrorChangeMemory("You feel ashamed of how you look", TerrorChangeStats(15, 0, 100, "You know you shouldn't, but you do."))),
                    InspectEvent("He's surrounded by her friends",
                        "They're laughing, and joking, and smiling. You can't follow what it is that's so hilarious " +
                            "but you suspect you wouldn't really feel the same way if you heard. " +
                            "They look so happy to be here, like they're having the time of their lives.",
                        TerrorChangeStats(20, 0, 100, "You don't belong here with them."),
                        TerrorChangeMemory("College men in Vegas", TerrorChangeStats(15, 0, 100, "Utterly out of your league.")))
                )))
    }),
    VEGAS_STRIP_MIDDLE_AGED_TOURIST({
        Entity(UUID.randomUUID().toString(), "A middle-aged tourist")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(false, character = 'T', foregroundRGB = RGB(128, 0, 0)))
            .addComponent(VegasStripMiddleAgedTouristAIComponent())
            .addComponent(SpeedComponent(300))
            .addComponent(ActionTimeComponent(300))
            .addComponent(InspectableComponent(
                "A middle-aged tourist",
                "Well-off, but not rich. You doubt the rich walk the strip.",
                mutableListOf()))
    }),
    VEGAS_STRIP_COSTUME_POLICEMAN({
        Entity(UUID.randomUUID().toString(), "A fake policeman")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(false, character = 'P', foregroundRGB = RGB(128, 0, 0)))
            .addComponent(VegasStripPerformerAIComponent(PerformerType.POLICEMAN))
            .addComponent(SpeedComponent(100))
            .addComponent(ActionTimeComponent(100))
            .addComponent(InspectableComponent(
                "A man in a policeman costume",
                "It, uh, it's less of a costume than being mostly naked, better to show off his finely toned body. " +
                    "There's the policeman cap, and then there's a collar with a badge on it and some sort of cut-off hot pants? " +
                    "You're not entirely sure how to describe this article of clothing. You are not nearly cool enough to be familiar with it.",
                mutableListOf(
                    InspectEvent("He twirls a pair of handcuffs",
                        "\"You want to take a picture?\" he asks, looking at you and smiling. You freeze. Uh. \n \n " +
                            "\"No thank you,\" you say, smiling, and move on.",
                        TerrorChangeStats(-2, 40, 100, "Well, it is his job."),
                        TerrorChangeMemory("Picture-taking on the Strip", TerrorChangeStats(-1, 40, 100, "Well that was awkward.")))
                )
            ))
    }),
    VEGAS_STRIP_COSTUME_BIRD_OF_PARADISE({
        Entity(UUID.randomUUID().toString(), "A woman in feathers")
            .addComponent(CollisionComponent.blocker())
            .addComponent(DisplayComponent(false, character = 'B', foregroundRGB = RGB(128, 0, 0)))
            .addComponent(VegasStripPerformerAIComponent(PerformerType.BIRD_OF_PARADISE))
            .addComponent(SpeedComponent(100))
            .addComponent(ActionTimeComponent(100))
            .addComponent(InspectableComponent(
                "A woman wearing mostly feathers",
                "There are feathers on her head, feathers trailing her arms, feathers somehow sticking out of her " +
                    "back on some sort of \"wings\", and what you assume are just enough feathers to keep her being arrested for public indecency, if Vegas has any such laws. " +
                    "You have no idea how this costume stays on without falling apart, but it's deeply impressive.",
                mutableListOf()
            ))
    }),
    SPIDER_SWARM({
        Entity(UUID.randomUUID().toString(), "A carpet of spiders")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(false, character = 'S', foregroundRGB = ThemeTag.STRANGE_PLACE.rgb))
            .addComponent(StrangePlaceSpiderSwarmAIComponent())
            .addComponent(SpeedComponent(100))
            .addComponent(ActionTimeComponent(100))
            .addComponent(InspectableComponent(
                "A writing mass of spiders",
                "They're crawling all around each other, around you, seemingly flowing up from the ground.",
                mutableListOf(
                    InspectEvent("Why did you do that?",
                        "That was a bad idea.",
                        TerrorChangeStats(100, 0, 100, "No, seriously, why?"),
                        null)
                )))
    }),
    ET({
        Entity(UUID.randomUUID().toString(), "A short, wrinkly alien")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(false, character = 'E', foregroundRGB = ThemeTag.STRANGE_PLACE.rgb))
            .addComponent(StrangePlaceETAIComponent())
            .addComponent(SpeedComponent(150))
            .addComponent(ActionTimeComponent(150))
            .addComponent(InspectableComponent(
                "ET, The Extra Terrestrial",
                "You were a bundle of nerves as a kid, huh.",
                mutableListOf(
                    InspectEvent("It's...it's ET.",
                        "When you were a little kid, your parents showed you ET. You don't remember doing " +
                            "this, but your parents told you about it afterwards. There's a scene they described where " +
                            "the kid is first meeting ET, and ET rises out of some bushes. \n \n " +
                            "Apparently when you saw ET, you screamed and ran out of the room. \n \n " +
                            "You were, like, five.",
                        TerrorChangeStats(-30, 50, 100, "Jesus. You can't believe you remember that."),
                        TerrorChangeMemory("Scared of ET", TerrorChangeStats(-15, 50, 100, "What a thing to be scared of.")))
                )))
    }),
    STRANGE_PLACE_VOLCANO({
        Entity(UUID.randomUUID().toString(), "A rumbling volcano")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'V', foregroundRGB = ThemeTag.STRANGE_PLACE.rgb))
            .addComponent(StrangePlaceVolcanoAIComponent())
            .addComponent(SpeedComponent(100))
            .addComponent(ActionTimeComponent(100))
            .addComponent(InspectableComponent(
                "You can feel the heat",
                "You have to warn your parents.",
                mutableListOf(
                    InspectEvent("It rumbles and shakes",
                        "You need to warn your parents, now! Visions of a giant wave of lava sweeping over " +
                            "your house push their way into your mind, your parents roasting and screaming, Alex " +
                            "crisping up in his crib like a potato chip, his tears lost in the roar of the eruption.",
                        TerrorChangeStats(10, 0, 100, "Where's the door!? Why can't you find it!?"),
                        null)
                )))
    }),
    STRANGE_PLACE_LEECH({
        Entity(UUID.randomUUID().toString(), "A gigantic leech")
            .addComponent(CollisionComponent.mover())
            .addComponent(DisplayComponent(true, character = 'L', foregroundRGB = ThemeTag.STRANGE_PLACE.rgb))
            .addComponent(StrangePlaceLeechAIComponent())
            .addComponent(SpeedComponent(150))
            .addComponent(ActionTimeComponent(150))
            .addComponent(InspectableComponent(
                "It pulls itself towards you",
                "You have a vision of it bursting out of your foot and shudder.",
                mutableListOf(
                    InspectEvent("It raises itself up like a snake",
                        "It's in your foot! You bite your lip hard, and taste blood. It's in your foot, it's " +
                            "growing in your foot! You can feel it! You swing your packpack over your front and " +
                            "frantically grab the heaviest textbook you can find. You drop your backpack, grit your " +
                            "teeth, and shut your eyes. Then you slam the textbook down into your foot with all your strength. \n \n " +
                            "For a moment, you cannot think at all. The pain is indescribable. \n \n " +
                            "When you open your eyes, the leech is back where it was, watching you, like a snake.",
                        TerrorChangeStats(25, 0, 100, "Your foot aches, faintly."),
                        TerrorChangeMemory("Parasite dreams", TerrorChangeStats(25, 50, 100, "You shudder, and try not to look at your foot.")))
                )))
    }),
}
