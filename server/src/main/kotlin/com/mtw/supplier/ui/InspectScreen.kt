package com.mtw.supplier.ui

import com.mtw.supplier.encounter.rulebook.actions.InspectAction
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.*
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.*

class InspectScreen(tileGrid: TileGrid, private val primaryScreen: Screen) {
    private val screen = Screen.create(tileGrid)
    private val exitButton: Button = Components.button()
        .withText("Press Space Or Click On This Button To Continue")
        .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_CENTER)
        .build()
    private val lines: MutableList<Label>
    private var header: AttachedComponent

    private val maxLines: Int = screen.height - 6
    private val textPad: Int = 6
    private val maxTextLen: Int = screen.width - textPad * 2


    private fun buildHeader(text: String): Header {
        return Components.header()
            .withText(text)
            .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
            .withAlignmentWithin(screen, ComponentAlignment.TOP_CENTER)
            .build()
    }

    init {
        screen.addComponent(exitButton)
        screen.theme = ColorThemes.monokaiBlue()

        header = screen.addComponent(buildHeader(""))

        lines = mutableListOf()
        for (i in 0 until maxLines) {
            val label = Components.label()
                .withSize(maxTextLen, 1)
                .withPosition(textPad, i + 4)
                .build()
            lines.add(label)
            screen.addComponent(label)
        }

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

    fun display(action: InspectAction) {
        header.detach()
        header = screen.addComponent(buildHeader(action.headerText!!))

        val wrappedLines = WordWrapUtil.wordWrap(action.bodyText!!, maxTextLen, maxLines - 3)
        for (i in lines.indices) {
            lines[i].text = wrappedLines.getOrNull(i) ?: ""
        }
        if (action.memory != null) {
            lines[wrappedLines.size + 1].text = "Memory gained!"
            lines[wrappedLines.size + 2].text = "+ ${action.memory!!.name}"
        }

        this.screen.display()
    }
}