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

class CommentaryFragment(val width: Int, val height: Int, positionX: Int, positionY: Int): Fragment {
    private var header: AttachedComponent
    private val lines: MutableList<AttachedComponent>
    override val root = VBoxBuilder.newBuilder().withSize(width, height).withPosition(positionX, positionY).build()
    private val maxTextLen: Int
        get() = this.width - 2
    private val maxCommentaryLines: Int
        get() = this.height - 3

    init {
        header = root.addComponent(LabelBuilder.newBuilder()
            .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
            .build())
        lines = mutableListOf()
    }

    fun setText(newHeader: String, newCommentary: String) {
        root.clear()
        header.detach()
        header = root.addComponent(LabelBuilder.newBuilder()
            .withText(newHeader.substring(0, min(newHeader.length, this.maxTextLen)))
            .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
            .build())

        for (line in lines) {
            line.detach()
        }
        lines.clear()
        val wrappedLines = WordWrapUtil.wordWrap(newCommentary, this.maxTextLen, this.maxCommentaryLines)
        /**
         * Ok, so, this is the set of lines which is causing the "blank screen on startup" bug. I don't know how to fix
         * it, and I don't have the time to properly debug it. Essentially, sometimes the for loop stalls out in the
         * middle and the screen just never finishes rendering.
         *
         * If you put a Thread.sleep(n) in here, it always renders the rest of the screen but still sometimes can't
         * finish pushing the lines in. Then it stalls, and never runs the game logic because the first time this
         * happens is at the start of the game in the manual render call. And "render" isn't even the right term...well
         * anyways. It seems to happen on startup and kill the game, but it might happen in other calls and just not be
         * noticable.
         */
        for (wrappedLine in wrappedLines) {
            lines.add(root.addComponent(LabelBuilder.newBuilder()
                .withSize(maxTextLen + 2, 1)
                .withDecorations(ComponentDecorations.side(' ', ' '))
                .withText(wrappedLine)
                .build()))
        }
    }
}

class StatsFragment(val width: Int, val height: Int, positionX: Int, positionY: Int): Fragment {
    private val nightmareBar: ProgressBar
    override val root = VBoxBuilder.newBuilder().withSize(width, height).withPosition(positionX, positionY).build()
    private val lines: MutableList<Label>

    private val maxTextLen: Int
        get() = this.width - 2
    private val maxHistoryLines: Int
        get() = this.height - 6

    init {
        nightmareBar = Components.progressBar()
            .withRange(100)
            .withNumberOfSteps(width - 2)
            .withDecorations(ComponentDecorations.box())
            .build()
        nightmareBar.progress = 50.0
        root.addComponent(nightmareBar)

        val barLabels = HBoxBuilder.newBuilder().withSize(width, 2).build()
        val leftLabel = Components.label()
            .withText("Nightmare")
            .withPosition(1, 0)
            .build()
        val rightLabel = Components.label().withText("Dream")
            .withPosition(width - "Nightmare".length - "Dream".length - 2, 0)
            .withColorTheme(ColorThemes.monokaiBlue())
            .build()
        barLabels.addComponent(leftLabel)
        barLabels.addComponent(rightLabel)
        root.addComponent(barLabels)

        val historyHeader = Components.header().withText("I remember...").build()
        root.addComponent(historyHeader)

        lines = mutableListOf()
        for (i in 0 until maxHistoryLines) {
            val historyLabel = Components.label()
                .withSize(maxTextLen, 1)
                .withDecorations(ComponentDecorations.side(' ', ' '))
                .build()
            root.addComponent(historyLabel)
            lines.add(historyLabel)
        }
    }

    fun setStats(playerTerrorPercentage: Double, history: List<String>) {
        this.nightmareBar.progress = playerTerrorPercentage

        lines.map { it.text = "" }
        for (i in 0 until min(maxHistoryLines, history.size)) {
            lines[i].text = history[i]
        }
    }
}

class PrimaryScreen(private val screen: Screen, private val encounterState: EncounterState) {
    val MAP_WIDTH: Int = 30
    val MAP_HEIGHT: Int = 30
    val MAP_CENTER = AbsolutePosition(MAP_WIDTH / 2, MAP_HEIGHT / 2)

    private var cameraX: Int = 0
    private var cameraY: Int = 0

    var windows: TileWindows? = null // bad bad bad

    init {
        screen.display()
        screen.theme = ColorThemes.arc()
    }

    fun addLayer(layer: Layer) {
        this.screen.addLayer(layer)
    }

    fun addComponent(component: Component) {
        this.screen.addComponent(component)
    }

    fun addFragment(fragment: Fragment) {
        this.screen.addFragment(fragment)
    }

    fun display(dirty: Boolean = false) {
        if (dirty) {
            this.renderGameState(encounterState)
        }
        this.screen.display()
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

    private fun renderDisplayEntities(tileGraphics: TileGraphics, encounterState: EncounterState) {
        val targetedEntity = encounterState.playerEntity().getComponent(PlayerComponent::class).targeted
        encounterState.entities()
            .filter { it.hasComponent(RoomPositionComponent::class) && it.hasComponent(DisplayComponent::class) }
            .map {
                val entityPos = it.getComponent(RoomPositionComponent::class).asAbsolutePosition(encounterState)
                if (entityPos != null && encounterState.getVisibleEntityAtPosition(entityPos) == it) {
                    if (targetedEntity == it) {
                        // lol this is pretty slapdash, oh well
                        val builder = it.getComponent(DisplayComponent::class).tileBuilder().withModifiers(Modifiers.blink())
                        draw(tileGraphics, builder.build(), entityPos)
                    } else {
                        draw(tileGraphics, it.getComponent(DisplayComponent::class).toTile(), entityPos)
                    }
                }
            }
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

    fun renderGameState(encounterState: EncounterState) {
        renderGameState(this.windows!!, encounterState)
    }

    private fun renderGameState(windows: TileWindows, encounterState: EncounterState) {
        // Draw the map
        windows.mapFoWTileGraphics.clear()
        windows.mapEntityTileGraphics.clear()
        val playerPos = encounterState.playerEntity().getComponent(RoomPositionComponent::class)
            .asAbsolutePosition(encounterState)!!
        cameraX = playerPos.x
        cameraY = playerPos.y

        renderFoWTiles(windows.mapFoWTileGraphics, encounterState)
        renderDisplayEntities(windows.mapEntityTileGraphics, encounterState)

        // Set the commentary
        windows.commentaryFragment.setText(encounterState.currentCommentaryHeader(), encounterState.currentCommentaryText())
        // Set the stats
        windows.statsFragment.setStats(encounterState.playerTerrorPercentage(), encounterState.lastSeenRoomNames())

        // Draw the log
        renderLog(windows.logVBox, encounterState)
    }
}