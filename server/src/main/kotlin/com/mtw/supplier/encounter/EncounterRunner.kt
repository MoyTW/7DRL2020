package com.mtw.supplier.encounter

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.ecs.components.ai.AIComponent
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.encounter.rulebook.Rulebook
import com.mtw.supplier.encounter.state.EncounterEndState
import org.slf4j.LoggerFactory

object EncounterRunner {
    private val logger = LoggerFactory.getLogger(EncounterRunner::class.java)

    private fun ticksToNextEvent(encounterState: EncounterState): Int {
        var ticksToNext = 99999999
        for (entity in encounterState.entities()) {
            val toNext = entity.getComponentOrNull(ActionTimeComponent::class)?.ticksUntilTurn
            if (toNext != null && toNext < ticksToNext) {
                ticksToNext = toNext
            }
        }
        return ticksToNext
    }

    private fun passTimeAndGetReadyEntities(encounterState: EncounterState, ticks: Int): MutableList<Entity> {
        val readyEntities = mutableListOf<Entity>()

        for (entity in encounterState.entities()) {
            val actionTimeComponent = entity.getComponentOrNull(ActionTimeComponent::class)
            if (actionTimeComponent != null) {
                actionTimeComponent.passTime(ticks)
                if (actionTimeComponent.isReady()) {
                    readyEntities.add(entity)
                }
            }
        }

        return readyEntities
    }

    fun runPlayerTurn(encounterState: EncounterState, playerAction: Action) {
        runPlayerTurn(encounterState, listOf(playerAction))
    }

    fun runPlayerTurn(encounterState: EncounterState, playerActions: List<Action>) {
        if (encounterState.endState != EncounterEndState.ONGOING) { return }

        // Move the player
        Rulebook.resolveActions(playerActions, encounterState)
        val speedComponent = playerActions[0].actor.getComponent(SpeedComponent::class)
        playerActions[0].actor.getComponent(ActionTimeComponent::class).endTurn(speedComponent)

        // Check for victory condition
        if (encounterState.playerEntity().getComponent(TerrorComponent::class).currentTerror <= 0) {
            encounterState.endState = EncounterEndState.VICTORY
        } else if (encounterState.playerEntity().getComponent(TerrorComponent::class).currentTerror >= 100) {
            encounterState.endState = EncounterEndState.DEFEAT
        }

        // Update the FoV for the player
        encounterState.calculatePlayerFoVAndMarkExploration()
    }

    fun runUntilPlayerReady(encounterState: EncounterState) {
        if (encounterState.endState != EncounterEndState.ONGOING) { return }

        var isPlayerReady = runNextActiveTick(encounterState)
        while (!isPlayerReady && encounterState.endState == EncounterEndState.ONGOING) {
            isPlayerReady = runNextActiveTick(encounterState)
        }
        encounterState.calculatePlayerFoVAndMarkExploration()
    }

    fun runNextActiveTick(encounterState: EncounterState): Boolean {
        if (encounterState.endState != EncounterEndState.ONGOING) { return false }

        // Run the clock until the next entity is ready
        val ticksToNext = ticksToNextEvent(encounterState)
        val readyEntities = passTimeAndGetReadyEntities(encounterState, ticksToNext)
        encounterState.advanceTime(ticksToNext)

        // If the player is the next ready entity, abort
        if (readyEntities.first().hasComponent(PlayerComponent::class)) {
            return true
        }

        //logger.info("========== START OF TURN ${encounterState.currentTime} ==========")
        // TODO: Caching of various iterables, if crawling nodes is slow?
        while (readyEntities.isNotEmpty() && !readyEntities.first().hasComponent(PlayerComponent::class)) {
            val entity = readyEntities.first()
            readyEntities.removeAt(0)
            if (entity.hasComponent(AIComponent::class)) {
                val nextActions = entity.getComponent(AIComponent::class).decideNextActions(encounterState)
                //logger.debug("Actions: $nextActions")
                Rulebook.resolveActions(nextActions, encounterState)
                val speedComponent = entity.getComponent(SpeedComponent::class)
                entity.getComponent(ActionTimeComponent::class).endTurn(speedComponent)
            }
        }

        // lol
        val remainingAIEntities = encounterState.entities().filter {
            it.hasComponent(AIComponent::class) && it.hasComponent(FactionComponent::class)
        }

        //logger.info("========== END OF TURN ${encounterState.currentTime} ==========")
        return false
    }
}
