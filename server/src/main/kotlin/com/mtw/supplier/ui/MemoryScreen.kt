package com.mtw.supplier.ui

import com.mtw.supplier.ecs.components.PlayerComponent
import com.mtw.supplier.encounter.rulebook.actions.InspectAction
import com.mtw.supplier.encounter.state.EncounterState
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.*
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.*

class MemoryScreen(tileGrid: TileGrid, private val primaryScreen: Screen, private val encounterState: EncounterState) {
    private val screen = Screen.create(tileGrid)
    private val exitButton: Button = Components.button()
        .withText("Press Space Or Click On This Button To Continue")
        .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_CENTER)
        .build()
    private val memoryButtons: MutableList<AttachedComponent>

    private val textPad: Int = 6
    private val keyString: String = "ABCDEFGHIJKL"


    init {
        screen.addComponent(exitButton)
        screen.theme = ColorThemes.monokaiBlue()

        screen.addComponent(Components.header()
            .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
            .withText("Your Memories")
            .withAlignmentWithin(screen, ComponentAlignment.TOP_CENTER)
            .build())

        memoryButtons = mutableListOf()

        screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { keyboardEvent: KeyboardEvent, uiEventPhase: UIEventPhase ->
            if (keyboardEvent.code == KeyCode.SPACE) {
                primaryScreen.display()
                UIEventResponse.processed()
            } else {
                UIEventResponse.pass()
            }
        }

        exitButton.handleComponentEvents(ComponentEventType.ACTIVATED) {
            primaryScreen.display()
            UIEventResponse.processed()
        }
    }

    fun display() {
        memoryButtons.map { it.detach() }
        memoryButtons.clear()
        val memories = encounterState.playerEntity().getComponent(PlayerComponent::class).memories
        for (i in memories.indices) {
            val button = Components.button()
                .withText(keyString[i] + ") " + memories[i].name)
                .withPosition(textPad, i*2 + 4)
                .build()
            memoryButtons.add(screen.addComponent(button))
        }

        this.screen.display()
    }
}