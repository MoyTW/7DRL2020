package com.mtw.supplier.ui

import com.mtw.supplier.encounter.rulebook.actions.InspectAction
import com.mtw.supplier.encounter.state.EncounterEndState
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.*
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.*

class GameEndScreen(tileGrid: TileGrid, private val primaryScreen: PrimaryScreen) {
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
    }

    fun display(endState: EncounterEndState) {
        header.detach()
        val headerText = if (endState == EncounterEndState.VICTORY) {
            "You sleep well, tonight."
        } else {
            "You wake suddenly, sweating and shaking."
        }
        header = screen.addComponent(buildHeader(headerText))

        val bodyText = if (endState == EncounterEndState.VICTORY) {
            "You wake in the morning slowly and comfortably. It's unusual for you to wake up before your alarm goes " +
                "off, but it's always a good sign when you do. You stretch in bed a bit. Today will be a good day. "
        } else {
            "It's dark, and you can hear the wheezing in your rapid breaths. " +
                "Your room is wrong. Somehow you've managed to turn yourself around in bed. " +
                "You can't see your alarm clock's readout when you turn your head to the left. " +
                "Instead it's over near your feet. " +
                "Your pillow has vanished, probably onto the floor like it usually does. \n \n " +
                "You gingerly peel the sheets off and step off your bed. You breathe. Breathing is important. " +
                "You recite the multiplication tables in your head. 1*1 is 1. 1*2 is 2. 1*3...3*4...7*3... " +
                "Gradually your breaths slow, and you look over at the time. \n \n " +
                "It's three in the morning. Fuck! Work tomorrow will be hellish, as it always is when you're woken up by " +
                "your nightmares. You bury your head in our hands and a tiny voice inside you asks if you would be " +
                "happier dead."
        }
        val wrappedLines = WordWrapUtil.wordWrap(bodyText, maxTextLen, maxLines - 6)
        for (i in lines.indices) {
            lines[i].text = wrappedLines.getOrNull(i) ?: ""
        }
        val notesWrapped = WordWrapUtil.wordWrap(
            "[You've completed a run. If you're seeing this, I didn't have time to build in restart/save/load," +
                " and you'll have to restart the game to play again. Sorry!]",
            maxTextLen,
            4)
        for (i in notesWrapped.indices) {
            lines[wrappedLines.size + 1 + i].text = notesWrapped[i]
        }

        this.screen.display()
    }
}