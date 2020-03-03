package com.mtw.supplier.ui

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.ecs.components.ai.AIComponent
import com.mtw.supplier.ecs.components.ai.EnemyScoutAIComponent
import com.mtw.supplier.ecs.components.ai.PathAIComponent
import com.mtw.supplier.encounter.EncounterRunner
import com.mtw.supplier.encounter.rulebook.actions.MoveAction
import com.mtw.supplier.encounter.rulebook.actions.WaitAction
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.utils.AbsolutePosition
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.component.Label
import org.hexworks.zircon.api.component.VBox
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.toScreen
import org.hexworks.zircon.api.graphics.TileGraphics
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.*

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
    val screen: Screen,
    val mapGraphics: TileGraphics,
    val logVBox: VBox
)

object EditorApp {
    val gameState = GameState()
    val MAP_WIDTH: Int = 30
    val MAP_HEIGHT: Int = 30
    val MAP_CENTER = AbsolutePosition(MAP_WIDTH / 2, MAP_HEIGHT / 2)
    val LOG_WIDTH: Int = 30
    val LOG_HEIGHT: Int = 10
    private var cameraX: Int = 0
    private var cameraY: Int = 0

    @JvmStatic
    fun main(args: Array<String>) {
        val tileGrid = SwingApplications.startTileGrid(
            AppConfig.newBuilder()
                .withSize(30, 40)
                .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
                .build())

        val screen = tileGrid.toScreen()

        screen.display()
        screen.theme = ColorThemes.arc()

        val mapGraphics: TileGraphics = DrawSurfaces.tileGraphicsBuilder()
            .withSize(Size.create(MAP_WIDTH, MAP_HEIGHT))
            .build()
        val logVBox: VBox = Components.vbox().withSize(LOG_WIDTH, LOG_HEIGHT).withPosition(0, MAP_HEIGHT).build()
        for (y in 0 until LOG_HEIGHT) {
            logVBox.addComponent(Components.label().withSize(LOG_WIDTH, 1).build())
        }

        val windows = TileWindows(screen, mapGraphics, logVBox)

        screen.addComponent(logVBox)

        tileGrid.processKeyboardEvents(KeyboardEventType.KEY_PRESSED) { keyboardEvent: KeyboardEvent, uiEventPhase: UIEventPhase ->
            handleKeyPress(keyboardEvent)
            renderGameState(windows, gameState.encounterState)
            UIEventResponse.pass()
        }

        renderGameState(windows, gameState.encounterState)
    }

    private fun draw(tileGraphics: TileGraphics, tile: Tile, pos: AbsolutePosition) {
        val screenPos = toCameraCoordinates(pos)
        tileGraphics.draw(tile, Position.create(screenPos.x, tileGraphics.height - screenPos.y - 1))
    }

    private fun renderFoWTiles(tileGraphics: TileGraphics, encounterState: EncounterState) {
        val tiles = encounterState.getDreamMapI()
        val fov = encounterState.fovCache

        val unexploredTile = Tile.newBuilder()
            .withBackgroundColor(ANSITileColor.BLACK)
            .build()
        val exploredTile = Tile.newBuilder()
            .withBackgroundColor(ANSITileColor.GRAY)
            .build()
        val visibleTile = Tile.newBuilder()
            .withBackgroundColor(ANSITileColor.WHITE)
            .build()
        tiles.getAllDreamTileIs().map { (pos, tile) ->
            val drawTile = when {
                !tile.explored -> { unexploredTile }
                !fov!!.isInFoV(pos) -> { exploredTile }
                else -> { visibleTile }
            }
            draw(tileGraphics, drawTile, pos)
        }
    }

    private fun renderNonPathAIEntities(tileGraphics: TileGraphics, encounterState: EncounterState) {
        val enemyTile = Tile.newBuilder()
            .withCharacter('s')
            .withBackgroundColor(ANSITileColor.RED)
            .buildCharacterTile()

        val nonPathAiEntities = encounterState.entities()
            .filter { it.hasComponent(RoomPositionComponent::class) &&
                it.hasComponent(AIComponent::class) &&
                !it.hasComponent(PathAIComponent::class) }
        nonPathAiEntities.map {
            val entityPos = it.getComponent(RoomPositionComponent::class).asAbsolutePosition(encounterState)
            if (entityPos != null) {
                draw(tileGraphics, enemyTile, entityPos)
            }
        }
    }

    private fun renderDoors(tileGraphics: TileGraphics, encounterState: EncounterState) {
        val doorTile = Tile.newBuilder()
            .withCharacter('%')
            .withForegroundColor(ANSITileColor.BLUE)
            .buildCharacterTile()
        val doors = encounterState.entities().filter { it.hasComponent(DoorComponent::class) }
        doors.map {
            val position = it.getComponent(RoomPositionComponent::class).asAbsolutePosition(encounterState)
            if (position != null) {
                draw(tileGraphics, doorTile, position)
            }
        }
    }

    private fun renderPlayer(tileGraphics: TileGraphics, encounterState: EncounterState) {
        val playerTile = Tile.newBuilder()
            .withCharacter('@')
            .withForegroundColor(ANSITileColor.GREEN)
            .withBackgroundColor(ANSITileColor.WHITE)
            .buildCharacterTile()
        val playerPos = encounterState.playerEntity().getComponent(RoomPositionComponent::class)
            .asAbsolutePosition(encounterState)
        draw(tileGraphics, playerTile, playerPos!!)
    }

    private fun toCameraCoordinates(pos: AbsolutePosition): AbsolutePosition {
        return AbsolutePosition(pos.x - cameraX + MAP_CENTER.x, pos.y - cameraY + MAP_CENTER.y)
    }

    private fun renderLog(logVBox: VBox, encounterState: EncounterState) {
        val last10 = encounterState.messageLog.getMessages(10)

        var y = 9
        for (child in logVBox.children) {
            (child as Label).text = last10.getOrNull(y) ?: ""
            y -= 1
        }
    }

    private fun renderGameState(windows: TileWindows, encounterState: EncounterState) {
        // Draw the map
        windows.mapGraphics.clear()
        val playerPos = encounterState.playerEntity().getComponent(RoomPositionComponent::class).asAbsolutePosition(encounterState)!!
        cameraX = playerPos.x
        cameraY = playerPos.y

        renderFoWTiles(windows.mapGraphics, encounterState)
        renderNonPathAIEntities(windows.mapGraphics, encounterState)
        renderDoors(windows.mapGraphics, encounterState)
        renderPlayer(windows.mapGraphics, encounterState)

        // Draw the log
        renderLog(windows.logVBox, encounterState)

        // Draw the screen
        windows.screen.clear()
        windows.screen.draw(windows.mapGraphics, Position.zero())
    }
    
    private fun handleKeyPress(event: KeyboardEvent): Boolean {
        return when (event.code) {
            KeyCode.NUMPAD_1 -> { gameState.postMoveAction(Direction.SW); true }
            KeyCode.NUMPAD_2 -> { gameState.postMoveAction(Direction.S); true }
            KeyCode.NUMPAD_3 -> { gameState.postMoveAction(Direction.SE); true }
            KeyCode.NUMPAD_4 -> { gameState.postMoveAction(Direction.W); true }
            KeyCode.NUMPAD_5 -> { gameState.postWaitAction(); true }
            KeyCode.NUMPAD_6 -> { gameState.postMoveAction(Direction.E); true }
            KeyCode.NUMPAD_7 -> { gameState.postMoveAction(Direction.NW); true }
            KeyCode.NUMPAD_8 -> { gameState.postMoveAction(Direction.N); true }
            KeyCode.NUMPAD_9 -> { gameState.postMoveAction(Direction.NE); true }
            else -> { false }
        }
    }
}

class GameState {
    var encounterState: EncounterState = generateNewGameState()
        
    internal fun postWaitAction() {
        val action = WaitAction(encounterState.playerEntity())
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
    }

    private final fun generateNewGameState(): EncounterState {
        val state = EncounterState(40, 40)

        val activatedAi = EnemyScoutAIComponent()
        activatedAi.isActive = true
        val scout = Entity(UUID.randomUUID().toString(), "Scout")
            .addComponent(activatedAi)
            .addComponent(HpComponent(10, 10))
            .addComponent(FighterComponent(0, 0, 0))
            .addComponent(FactionComponent(0))
            .addComponent(CollisionComponent.defaultFighter())
            .addComponent(ActionTimeComponent(75))
            .addComponent(SpeedComponent(75))
        val player = Entity(UUID.randomUUID().toString(), "player")
            .addComponent(PlayerComponent())
            .addComponent(HpComponent(50, 50))
            .addComponent(FighterComponent(5, 100, 100))
            .addComponent(FactionComponent(2))
            .addComponent(CollisionComponent.defaultFighter())
            .addComponent(ActionTimeComponent(100))
            .addComponent(SpeedComponent(100))

        state.placeEntity(scout, AbsolutePosition(1, 1))
            .placeEntity(player, AbsolutePosition(3, 3))
        EncounterRunner.runUntilPlayerReady(state)
        return state
    }
}