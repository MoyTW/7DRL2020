package com.mtw.supplier.ui

import com.mtw.supplier.Serializers
import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.ecs.components.ai.AIComponent
import com.mtw.supplier.ecs.components.ai.EnemyScoutAIComponent
import com.mtw.supplier.ecs.components.ai.PathAIComponent
import com.mtw.supplier.encounter.EncounterRunner
import com.mtw.supplier.encounter.rulebook.actions.MoveAction
import com.mtw.supplier.encounter.rulebook.actions.WaitAction
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.utils.XYCoordinates
import javafx.scene.Group
import javafx.scene.control.ListView
import javafx.scene.control.ScrollPane
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import tornadofx.*

enum class Direction(val dx: Int, val dy: Int) {
    N(0, 1),
    NE(1, 1),
    E(1, 0),
    SE(1, -1),
    S(0, -1),
    SW(-1, -1),
    W(-1, 0),
    NW(-1, 1)
}

class GameScreen: View() {
    private val SERVER_PORT = 8080

    private val json = Json(JsonConfiguration.Stable.copy(prettyPrint = true),
        context = Serializers.componentSerializersModuleBuilder())

    private var mainScrollPane: ScrollPane by singleAssign()
    private var regionLinesStackpane: StackPane by singleAssign()
    private var logListView: ListView<String> by singleAssign()

    private var encounterState: EncounterState? = null

    override val root = borderpane {
        top {
            menubar {
                menu("File") {
                    item("Refersh", "Shortcut+R").action {
                        encounterState = generateNewGameState()
                        encounterStateRender()
                    }
                    item("Quit", "Shortcut+Q").action {
                        println("QUIT")
                    }
                }
            }
        }
        center {
            mainScrollPane = scrollpane {
                stackpane {
                    regionLinesStackpane = stackpane ()
                    // TODO: Figure out a better way
                    keyboard {
                        addEventFilter(KeyEvent.KEY_PRESSED) { handleKeyPress(it) }
                    }
                }
            }
        }
        bottom {
            logListView = listview<String> {
                this.maxHeight = 200.0
            }
        }
        encounterState = generateNewGameState()
        encounterStateRender()
    }

    private fun handleKeyPress(event: KeyEvent) {
        when (event.code) {
            KeyCode.NUMPAD1 -> { postMoveAction(Direction.SW); encounterStateRender() }
            KeyCode.NUMPAD2 -> { postMoveAction(Direction.S); encounterStateRender() }
            KeyCode.NUMPAD3 -> { postMoveAction(Direction.SE); encounterStateRender() }
            KeyCode.NUMPAD4 -> { postMoveAction(Direction.W); encounterStateRender() }
            KeyCode.NUMPAD5 -> { postWaitAction(); encounterStateRender() }
            KeyCode.NUMPAD6 -> { postMoveAction(Direction.E); encounterStateRender() }
            KeyCode.NUMPAD7 -> { postMoveAction(Direction.NW); encounterStateRender() }
            KeyCode.NUMPAD8 -> { postMoveAction(Direction.N); encounterStateRender() }
            KeyCode.NUMPAD9 -> { postMoveAction(Direction.NE); encounterStateRender() }
            else -> {}
        }
    }

    private fun postWaitAction() {
        val action = WaitAction(encounterState!!.playerEntity())
        EncounterRunner.runPlayerTurn(encounterState!!, action)
        EncounterRunner.runUntilPlayerReady(encounterState!!)
    }

    private fun postMoveAction(direction: Direction) {
        val oldPlayerPos = encounterState!!.playerEntity().getComponent(EncounterLocationComponent::class).position
        val newPlayerPos = oldPlayerPos.copy(
            x = oldPlayerPos.x + direction.dx, y = oldPlayerPos.y + direction.dy)

        val action = MoveAction(encounterState!!.playerEntity(), newPlayerPos)
        EncounterRunner.runPlayerTurn(encounterState!!, action)
        EncounterRunner.runUntilPlayerReady(encounterState!!)
    }

    private final fun generateNewGameState(): EncounterState {
        val state = EncounterState(40, 40)

        val activatedAi = EnemyScoutAIComponent()
        activatedAi.isActive = true
        val scout = Entity(state.getNextEntityId(), "Scout")
            .addComponent(activatedAi)
            .addComponent(HpComponent(10, 10))
            .addComponent(FighterComponent(0, 0, 0))
            .addComponent(FactionComponent(0))
            .addComponent(CollisionComponent.defaultFighter())
            .addComponent(ActionTimeComponent(75))
            .addComponent(SpeedComponent(75))
        val player = Entity(state.getNextEntityId(), "player")
            .addComponent(PlayerComponent())
            .addComponent(HpComponent(50, 50))
            .addComponent(FighterComponent(5, 100, 100))
            .addComponent(FactionComponent(2))
            .addComponent(CollisionComponent.defaultFighter())
            .addComponent(ActionTimeComponent(100))
            .addComponent(SpeedComponent(100))

        state.placeEntity(scout, XYCoordinates(10, 10))
            .placeEntity(player, XYCoordinates(25, 25))
        EncounterRunner.runUntilPlayerReady(state)
        return state
    }

    private fun mapXToScreenXCenter(x: Int): Double {
        return (x * TILE_SIZE) + (TILE_SIZE / 2f)
    }

    private fun mapYToScreenYCenter(y: Int): Double {
        return -(y * TILE_SIZE + (TILE_SIZE / 2f))
    }

    companion object {
        val TILE_SIZE = 20.0
    }

    private fun mapShapes(encounterState: EncounterState?): Group {
        val UNEXPLORED_COLOR = Color.BLACK
        val EXPLORED_COLOR = Color.GRAY
        val VISIBLE_COLOR = Color.DARKGRAY
        val PROJECTILE_COLOR = Color.ORANGERED
        val PROJECTILE_PATH_COLOR = Color.ORCHID
        val ENEMY_COLOR = Color.DARKRED
        val PLAYER_COLOR = Color.GREEN

        if (encounterState == null) {
            return group()
        }

        val tiles = encounterState.getEncounterTileMap()
        val fov = encounterState.fovCache

        val g = group()

        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {

                val tile = tiles.getTileView(x, y)
                g.group {
                    rectangle {
                        this.x = x * TILE_SIZE
                        this.y = -y * TILE_SIZE - TILE_SIZE
                        width = TILE_SIZE
                        height = TILE_SIZE
                        stroke = Color.WHITE
                        fill = when {
                            tile?.explored == false -> { UNEXPLORED_COLOR }
                            !fov!!.isInFoV(XYCoordinates(x, y)) -> { EXPLORED_COLOR }
                            else -> { VISIBLE_COLOR }
                        }
                    }
                }
            }
        }
        val mapEntities = encounterState.entities().filter { it.hasComponent(EncounterLocationComponent::class) }
        val nonPathAIEntities = mapEntities.filter { it.hasComponent(AIComponent::class) && !it.hasComponent(PathAIComponent::class) }
        val pathEntities = mapEntities.filter { it.hasComponent(PathAIComponent::class) }

        pathEntities.map {
            val path = it.getComponent(PathAIComponent::class).path
            val projectileSpeed = it.getComponent(SpeedComponent::class).speed
            val projectileTicks = it.getComponent(ActionTimeComponent::class)!!.ticksUntilTurn

            val playerSpeed = encounterState.playerEntity().getComponent(SpeedComponent::class).speed
            val playerTicks = encounterState.playerEntity().getComponent(ActionTimeComponent::class)!!.ticksUntilTurn
            if (projectileTicks <= playerTicks) {
                val turns = ((playerTicks - projectileTicks) + playerSpeed) / projectileSpeed
                val stops = path.project(turns)
                if (stops.size > 1) {
                    for (stop in stops.subList(1, stops.size)) {
                        g.group {
                            rectangle {
                                this.x = stop.x * TILE_SIZE
                                this.y = -(stop.y * TILE_SIZE + TILE_SIZE)
                                width = TILE_SIZE
                                height = TILE_SIZE
                                stroke = Color.WHITE
                                fill = PROJECTILE_PATH_COLOR
                            }
                        }
                    }
                }
            }
        }
        pathEntities.map {
            val path = it.getComponent(PathAIComponent::class).path
            val projectileSpeed = it.getComponent(SpeedComponent::class).speed
            val projectileTicks = it.getComponent(ActionTimeComponent::class)!!.ticksUntilTurn

            val playerSpeed = encounterState.playerEntity().getComponent(SpeedComponent::class).speed
            val playerTicks = encounterState.playerEntity().getComponent(ActionTimeComponent::class)!!.ticksUntilTurn
            if (projectileTicks <= playerTicks) {
                val turns = ((playerTicks - projectileTicks) + playerSpeed) / projectileSpeed
                val stops = path.project(turns)
                if (stops.isNotEmpty()) {
                    for (stop in stops) {
                        g.group {
                            line(mapXToScreenXCenter(stops.first().x),
                                mapYToScreenYCenter(stops.first().y),
                                mapXToScreenXCenter(stops.last().x),
                                mapYToScreenYCenter(stops.last().y))
                        }
                    }
                }
            }
        }
        pathEntities.map {
            val entityPos = it.getComponent(EncounterLocationComponent::class).position
            g.group {
                circle {
                    centerX = mapXToScreenXCenter(entityPos.x)
                    centerY = mapYToScreenYCenter(entityPos.y)
                    radius = TILE_SIZE / 6
                    fill = PROJECTILE_COLOR
                }
            }
        }

        nonPathAIEntities.map {
            val entityPos = it.getComponent(EncounterLocationComponent::class).position
            g.group {
                circle {
                    centerX = mapXToScreenXCenter(entityPos.x)
                    centerY = mapYToScreenYCenter(entityPos.y)
                    radius = TILE_SIZE / 2 - 1
                    fill = ENEMY_COLOR
                }
            }
        }

        val playerPos = encounterState.playerEntity().getComponent(EncounterLocationComponent::class).position
        g.group {
            circle {
                centerX = mapXToScreenXCenter(playerPos.x)
                centerY = mapYToScreenYCenter(playerPos.y)
                radius = TILE_SIZE / 2 - 1
                fill = PLAYER_COLOR
            }
        }
        return g
    }

    private fun encounterStateRender() {
        regionLinesStackpane.replaceChildren(mapShapes(this.encounterState))
        val messages = encounterState?.messageLog?.getMessages()?.reversed()
        if (messages != null) {
            logListView.items.clear()
            logListView.items.addAll(messages)
            logListView.scrollTo(messages.size - 1)
        }
    }
}
