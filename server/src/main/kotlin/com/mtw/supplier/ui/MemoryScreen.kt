package com.mtw.supplier.ui

import com.mtw.supplier.ecs.components.PlayerComponent
import com.mtw.supplier.encounter.EncounterRunner
import com.mtw.supplier.encounter.state.EncounterState
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.*
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.*

class MemoryScreen(tileGrid: TileGrid, private val primaryScreen: PrimaryScreen, private val encounterState: EncounterState) {
    private val screen = Screen.create(tileGrid)
    private val exitButton: Button = Components.button()
        .withText("Press Space Or Click On This Button To Continue")
        .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_CENTER)
        .build()
    private val memoryButtons: MutableList<AttachedComponent> // TODO: see if you can color these!

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

        screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { keyboardEvent: KeyboardEvent, _: UIEventPhase ->
            when (keyboardEvent.code) {
                KeyCode.SPACE -> { primaryScreen.display(); UIEventResponse.processed() }
                KeyCode.KEY_A -> { handleKey(keyboardEvent) }
                KeyCode.KEY_B -> { handleKey(keyboardEvent) }
                KeyCode.KEY_C -> { handleKey(keyboardEvent) }
                KeyCode.KEY_D -> { handleKey(keyboardEvent) }
                KeyCode.KEY_E -> { handleKey(keyboardEvent) }
                KeyCode.KEY_F -> { handleKey(keyboardEvent) }
                KeyCode.KEY_G -> { handleKey(keyboardEvent) }
                KeyCode.KEY_H -> { handleKey(keyboardEvent) }
                KeyCode.KEY_I -> { handleKey(keyboardEvent) }
                KeyCode.KEY_J -> { handleKey(keyboardEvent) }
                KeyCode.KEY_K -> { handleKey(keyboardEvent) }
                KeyCode.KEY_L -> { handleKey(keyboardEvent) }
                else -> { UIEventResponse.pass() }
            }
        }

        exitButton.handleComponentEvents(ComponentEventType.ACTIVATED) {
            primaryScreen.display()
            UIEventResponse.processed()
        }
    }
    
    private fun handleKey(keyboardEvent: KeyboardEvent): UIEventResponse {
        val char = keyboardEvent.key.toUpperCase()
        val buttonIdx = keyString.indexOf(char)
        println("BUTTONIDX: " + buttonIdx)

        val playerComponent = encounterState.playerEntity().getComponent(PlayerComponent::class)
        val memory = playerComponent.getMemories().getOrNull(buttonIdx)

        return if (memory != null) {
            playerComponent.removeMemory(memory)
            EncounterRunner.runPlayerTurn(encounterState, memory.remember(encounterState))
            EncounterRunner.runUntilPlayerReady(encounterState)
            primaryScreen.display(true)
            UIEventResponse.processed()
        } else {
            UIEventResponse.pass()
        }
    }

    fun display() {
        memoryButtons.map { it.detach() }
        memoryButtons.clear()
        val memories = encounterState.playerEntity().getComponent(PlayerComponent::class).getMemories()
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