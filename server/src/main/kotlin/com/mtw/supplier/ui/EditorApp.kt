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
import com.mtw.supplier.utils.XYCoordinates
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.toScreen
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



object EditorApp {
    val gameState = GameState()
    val WIDTH: Int = 40
    val HEIGHT: Int = 40
    val CENTER = XYCoordinates(WIDTH / 2, HEIGHT / 2)
    private var cameraX: Int = 0
    private var cameraY: Int = 0
    
    @JvmStatic
    fun main(args: Array<String>) {
        val tileGrid = SwingApplications.startTileGrid(
            AppConfig.newBuilder()
                .withSize(40, 40)
                .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
                .build())

        val screen = tileGrid.toScreen()

        screen.display()
        screen.theme = ColorThemes.arc()

        tileGrid.processKeyboardEvents(KeyboardEventType.KEY_PRESSED) { keyboardEvent: KeyboardEvent, uiEventPhase: UIEventPhase ->
            handleKeyPress(keyboardEvent)
            renderGameState(screen)
            UIEventResponse.pass()
        }

        renderGameState(screen)
    }

    private fun draw(screen: Screen, tile: Tile, pos: XYCoordinates) {
        val screenPos = toCameraCoordinates(pos)
        screen.draw(tile, Position.create(screenPos.x, screen.height - screenPos.y - 1))
    }

    private fun renderFoWTiles(screen: Screen) {
        val tiles = gameState.encounterState.getEncounterTileMap()
        val fov = gameState.encounterState.fovCache

        val unexploredTile = Tile.newBuilder()
            .withBackgroundColor(ANSITileColor.BLACK)
            .build()
        val exploredTile = Tile.newBuilder()
            .withBackgroundColor(ANSITileColor.GRAY)
            .build()
        val visibleTile = Tile.newBuilder()
            .withBackgroundColor(ANSITileColor.WHITE)
            .build()
        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {
                val tile = tiles.getDreamTileI(x, y)
                val drawTile = when {
                    tile?.explored == false-> { unexploredTile }
                    !fov!!.isInFoV(XYCoordinates(x, y)) -> { exploredTile }
                    else -> { visibleTile }
                }
                draw(screen, drawTile, XYCoordinates(x, y))
            }
        }
    }

    private fun renderNonPathAIEntities(screen: Screen, encounterState: EncounterState) {
        val enemyTile = Tile.newBuilder()
            .withCharacter('s')
            .withBackgroundColor(ANSITileColor.RED)
            .buildCharacterTile()

        val nonPathAiEntities = encounterState.entities()
            .filter { it.hasComponent(EncounterLocationComponent::class) &&
                it.hasComponent(AIComponent::class) &&
                !it.hasComponent(PathAIComponent::class) }
        nonPathAiEntities.map {
            val entityPos = it.getComponent(EncounterLocationComponent::class).roomPosition
            draw(screen, enemyTile, entityPos)
        }
    }

    private fun renderDoors(screen: Screen, encounterState: EncounterState) {
        val doorTile = Tile.newBuilder()
            .withCharacter('%')
            .withForegroundColor(ANSITileColor.BLUE)
            .buildCharacterTile()
        val doors = encounterState.entities().filter { it.hasComponent(DoorComponent::class) }
        doors.map {
            draw(screen, doorTile, it.getComponent(EncounterLocationComponent::class).roomPosition)
        }
    }

    private fun renderPlayer(screen: Screen, encounterState: EncounterState) {
        val playerTile = Tile.newBuilder()
            .withCharacter('@')
            .withForegroundColor(ANSITileColor.GREEN)
            .withBackgroundColor(ANSITileColor.WHITE)
            .buildCharacterTile()
        val playerPos = encounterState.playerEntity().getComponent(EncounterLocationComponent::class).roomPosition
        draw(screen, playerTile, playerPos)
    }

    private fun toCameraCoordinates(pos: XYCoordinates): XYCoordinates {
        return XYCoordinates(pos.x - cameraX + CENTER.x, pos.y - cameraY + CENTER.y)
    }

    private fun renderGameState(screen: Screen) {
        screen.clear()
        // Render the tiles
        val playerPos = gameState.encounterState.playerEntity().getComponent(EncounterLocationComponent::class).roomPosition
        cameraX = playerPos.x
        cameraY = playerPos.y

        renderFoWTiles(screen)
        renderNonPathAIEntities(screen, gameState.encounterState)
        renderDoors(screen, gameState.encounterState)
        renderPlayer(screen, gameState.encounterState)
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
        val action = WaitAction(encounterState!!.playerEntity())
        EncounterRunner.runPlayerTurn(encounterState!!, action)
        EncounterRunner.runUntilPlayerReady(encounterState!!)
    }

    internal fun postMoveAction(direction: Direction) {
        val oldPlayerPos = encounterState!!.playerEntity().getComponent(EncounterLocationComponent::class).roomPosition
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

        state.placeEntity(scout, XYCoordinates(10, 10))
            .placeEntity(player, XYCoordinates(25, 25))
        EncounterRunner.runUntilPlayerReady(state)
        return state
    }
}