package com.mtw.supplier.ui

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.encounter.EncounterRunner
import com.mtw.supplier.encounter.rulebook.actions.InspectAction
import com.mtw.supplier.encounter.rulebook.actions.MoveAction
import com.mtw.supplier.encounter.rulebook.actions.WaitAction
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.utils.AbsolutePosition
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.builder.component.HBoxBuilder
import org.hexworks.zircon.api.builder.component.LabelBuilder
import org.hexworks.zircon.api.builder.component.VBoxBuilder
import org.hexworks.zircon.api.builder.graphics.LayerBuilder
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.component.*
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.toScreen
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.graphics.TileGraphics
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.*
import java.lang.StringBuilder
import kotlin.math.min

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

data class TileWindows(
    val primaryScreen: PrimaryScreen,
    val inspectScreen: InspectScreen,
    val memoryScreen: MemoryScreen,
    val mapFoWTileGraphics: TileGraphics,
    val mapEntityTileGraphics: TileGraphics,
    val commentaryFragment: CommentaryFragment,
    val statsFragment: StatsFragment,
    val logVBox: VBox
)

object WordWrapUtil {
    fun wordWrap(text: String, width: Int, maxLines: Int): List<String> {
        val lines = mutableListOf<String>()
        val words = text.split(' ').toMutableList()
        while (words.isNotEmpty() && lines.size < maxLines) {
            val sb = StringBuilder(words.removeAt(0))
            while (words.isNotEmpty() && sb.length + words[0].length < width) {
                sb.append(" " + words.removeAt(0))
            }
            lines.add(sb.toString())
        }
        return lines
    }
}

object EditorApp {
    val gameState = GameState()
    val GAME_WIDTH: Int = 60
    val GAME_HEIGHT: Int = 40
    val MAP_WIDTH: Int = 30
    val MAP_HEIGHT: Int = 30
    val COMMENTARY_HEIGHT: Int = 15
    val STATS_HEIGHT = 15
    val LOG_WIDTH: Int = GAME_WIDTH
    val LOG_HEIGHT: Int = GAME_HEIGHT - MAP_HEIGHT


    @JvmStatic
    fun main(args: Array<String>) {
        val tileGrid = SwingApplications.startTileGrid(
            AppConfig.newBuilder()
                .withSize(GAME_WIDTH, GAME_HEIGHT)
                .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
                .build())

        val primaryScreen = PrimaryScreen(tileGrid.toScreen(), gameState.encounterState)

        val inspectScreen = InspectScreen(tileGrid, primaryScreen)
        val memoryScreen = MemoryScreen(tileGrid, primaryScreen, gameState.encounterState)

        val mapFoWTileGraphics: TileGraphics = DrawSurfaces.tileGraphicsBuilder()
            .withSize(Size.create(MAP_WIDTH, MAP_HEIGHT))
            .build()
        val mapEntityTileGraphics: TileGraphics = DrawSurfaces.tileGraphicsBuilder()
            .withSize(Size.create(MAP_WIDTH, MAP_HEIGHT))
            .build()
        val logVBox: VBox = Components.vbox().withSize(LOG_WIDTH, LOG_HEIGHT).withPosition(0, MAP_HEIGHT).build()
        for (y in 0 until LOG_HEIGHT) {
            logVBox.addComponent(Components.label().withSize(LOG_WIDTH, 1).build())
        }
        val commentaryFragment = CommentaryFragment(GAME_WIDTH - MAP_WIDTH, COMMENTARY_HEIGHT, MAP_WIDTH, 0)
        val statsFragment = StatsFragment(GAME_WIDTH - MAP_WIDTH, STATS_HEIGHT, MAP_WIDTH, 0 + COMMENTARY_HEIGHT)

        val windows = TileWindows(primaryScreen, inspectScreen, memoryScreen, mapFoWTileGraphics, mapEntityTileGraphics, commentaryFragment, statsFragment, logVBox)
        // bad bad bad
        primaryScreen.windows = windows

        primaryScreen.addLayer(LayerBuilder.newBuilder().withTileGraphics(mapFoWTileGraphics).build())
        primaryScreen.addLayer(LayerBuilder.newBuilder().withTileGraphics(mapEntityTileGraphics).build())
        primaryScreen.addComponent(logVBox)
        primaryScreen.addFragment(commentaryFragment)
        primaryScreen.addFragment(statsFragment)

        tileGrid.processKeyboardEvents(KeyboardEventType.KEY_PRESSED) { keyboardEvent: KeyboardEvent, uiEventPhase: UIEventPhase ->
            handleKeyPress(keyboardEvent, windows)
            primaryScreen.renderGameState(gameState.encounterState)
            UIEventResponse.pass()
        }

        primaryScreen.renderGameState(gameState.encounterState)
    }
    
    private fun handleKeyPress(event: KeyboardEvent, windows: TileWindows): Boolean {
        return when (event.code) {
            KeyCode.NUMPAD_1 -> { gameState.postMoveAction(Direction.SW); true }
            KeyCode.KEY_B -> { gameState.postMoveAction(Direction.SW); true }
            KeyCode.NUMPAD_2 -> { gameState.postMoveAction(Direction.S); true }
            KeyCode.KEY_J -> { gameState.postMoveAction(Direction.S); true }
            KeyCode.NUMPAD_3 -> { gameState.postMoveAction(Direction.SE); true }
            KeyCode.KEY_N -> { gameState.postMoveAction(Direction.SE); true }
            KeyCode.NUMPAD_4 -> { gameState.postMoveAction(Direction.W); true }
            KeyCode.KEY_H -> { gameState.postMoveAction(Direction.W); true }
            KeyCode.NUMPAD_5 -> { gameState.postWaitAction(); true }
            KeyCode.PERIOD -> { gameState.postWaitAction(); true }
            KeyCode.NUMPAD_6 -> { gameState.postMoveAction(Direction.E); true }
            KeyCode.KEY_L -> { gameState.postMoveAction(Direction.E); true }
            KeyCode.NUMPAD_7 -> { gameState.postMoveAction(Direction.NW); true }
            KeyCode.KEY_Y -> { gameState.postMoveAction(Direction.NW); true }
            KeyCode.NUMPAD_8 -> { gameState.postMoveAction(Direction.N); true }
            KeyCode.KEY_K -> { gameState.postMoveAction(Direction.N); true }
            KeyCode.NUMPAD_9 -> { gameState.postMoveAction(Direction.NE); true }
            KeyCode.KEY_U -> { gameState.postMoveAction(Direction.NE); true }

            KeyCode.DIVIDE -> { gameState.targetPrevious(); true}
            KeyCode.MULTIPLY -> { gameState.targetNext(); true}

            KeyCode.KEY_I -> { gameState.inspectTarget(windows.inspectScreen); true}
            KeyCode.KEY_R -> { gameState.displayMemories(windows.memoryScreen); true}

            else -> { false }
        }
    }
}

class GameState {
    var encounterState: EncounterState = generateNewGameState()

    /**
     * direction should be 1, -1
     */
    private fun target(direction: Int) {
        val player = encounterState.playerEntity()
        val visible = encounterState.fovCache!!.visiblePositions
        val visibleEntities = visible.mapNotNull { encounterState.getVisibleEntityAtPosition(it) }
            .filterNot { it.id == player.id }
            // dumbest filtering ever
            .filterNot { it.name.toUpperCase() == "DOOR" }
            .filterNot { it.name.toUpperCase() == "WALL" }
            .sortedBy { it.name }
        if (visibleEntities.isNotEmpty()) {
            val currentTarget = player.getComponent(PlayerComponent::class).targeted
            if (currentTarget == null) {
                player.getComponent(PlayerComponent::class).targeted = visibleEntities[0]
            } else {
                var nextTarget = visibleEntities.indexOf(currentTarget) + direction
                if (nextTarget == visibleEntities.size) {
                    nextTarget = 0
                } else if (nextTarget < 0) {
                    nextTarget = visibleEntities.size - 1
                }
                player.getComponent(PlayerComponent::class).targeted = visibleEntities[nextTarget]
            }
        }
    }

    internal fun targetPrevious() {
        target(-1)
    }

    internal fun targetNext() {
        target(1)
    }

    internal fun clearTarget() {
        encounterState.playerEntity().getComponent(PlayerComponent::class).targeted = null
    }

    internal fun inspectTarget(inspectScreen: InspectScreen) {
        val target = encounterState.playerEntity().getComponent(PlayerComponent::class).targeted
        if (target != null) {
            val action = InspectAction(encounterState.playerEntity(), target)
            EncounterRunner.runPlayerTurn(encounterState, action)
            EncounterRunner.runUntilPlayerReady(encounterState)

            if (action.completed) {
                inspectScreen.display(action)
            }
        }
    }

    internal fun displayMemories(memoryScreen: MemoryScreen) {
        memoryScreen.display()
    }
        
    internal fun postWaitAction() {
        val action = WaitAction(encounterState.playerEntity(), null)
        EncounterRunner.runPlayerTurn(encounterState, action)
        EncounterRunner.runUntilPlayerReady(encounterState)
    }

    internal fun postMoveAction(direction: Direction) {
        val oldPlayerPos = encounterState.playerEntity().getComponent(RoomPositionComponent::class).roomPosition
        val newPlayerPos = oldPlayerPos.copy(
            x = oldPlayerPos.x + direction.dx, y = oldPlayerPos.y + direction.dy)

        val action = MoveAction(encounterState.playerEntity(), encounterState.roomToAbsolutePosition(newPlayerPos)!!)
        EncounterRunner.runPlayerTurn(encounterState, action)
        EncounterRunner.runUntilPlayerReady(encounterState)

        // Clear target after move
        clearTarget()
    }

    private fun generateNewGameState(): EncounterState {
        val state = EncounterState()

        val player = Entity(UUID.randomUUID().toString(), "player")
            .addComponent(PlayerComponent())
            .addComponent(DisplayComponent(foregroundRGB = RGB(0, 0, 255), character = '@'))
            .addComponent(TerrorComponent())
            .addComponent(CollisionComponent.mover())
            .addComponent(ActionTimeComponent(100))
            .addComponent(SpeedComponent(100))

        state.placeEntity(player, state.randomUnblockedPosition()!!)
        EncounterRunner.runUntilPlayerReady(state)
        return state
    }
}