package com.mtw.supplier.ui

import com.mtw.supplier.encounter.state.EncounterEndState
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.*
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.*

class IntroductionScreen(tileGrid: TileGrid, private val primaryScreen: PrimaryScreen) {
    private val screen = Screen.create(tileGrid)
    private var header: AttachedComponent
    private val lines: MutableList<Label>

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
        screen.theme = ColorThemes.afterglow()

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
    }

    fun display() {
        header.detach()
        val headerText = "How To Play"
        header = screen.addComponent(buildHeader(headerText))

        val bodyText = "You need to escape your nightmare. If your terror goes too high, you'll wake up; if you empty " +
            "your terror bar you'll sleep well. It's technically possible to get into an unwinnable state if you burn all your " +
            "terror reducers. I'm sorry, I didn't have time to add something in to fix that. \n \n " +
            "Controls: \n " +
            "+ Movement: Numpad or YUBN+HJKL \n " +
            "+ Target: 't' or '/' and '*' \n " +
            "+ Inspect: 'i' \n " +
            "+ Remember: 'r' \n " +
            "+ This Screen: '?'"
        val wrappedLines = WordWrapUtil.wordWrap(bodyText, maxTextLen, maxLines - 6)
        for (i in lines.indices) {
            lines[i].text = wrappedLines.getOrNull(i) ?: ""
        }
        val notesWrapped = WordWrapUtil.wordWrap(
            "[In-game instructions and UI elements will be bracked, like this. Press SPACE to begin.]",
            maxTextLen,
            4)
        for (i in notesWrapped.indices) {
            lines[wrappedLines.size + 1 + i].text = notesWrapped[i]
        }

        this.screen.display()
    }
}