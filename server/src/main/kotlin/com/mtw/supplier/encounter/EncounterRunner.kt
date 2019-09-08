package com.mtw.supplier.encounter

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.ecs.components.ai.AIComponent
import com.mtw.supplier.ecs.components.ai.PathAIComponent
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.encounter.rulebook.Rulebook
import com.mtw.supplier.encounter.rulebook.actions.FireProjectileAction
import com.mtw.supplier.encounter.rulebook.actions.ProjectileType
import com.mtw.supplier.utils.LinePathBuilder
import com.mtw.supplier.utils.PathBuilder
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

    private fun fireLaser(encounterState: EncounterState, player: Entity) {
        val hostileEntities = encounterState.entities().filter {
            it.hasComponent(AIComponent::class) && it.hasComponent(FactionComponent::class) }
        // TODO: Range and FOV stuff
        if (hostileEntities.isNotEmpty()) {
            val playerPos = player.getComponent(EncounterLocationComponent::class).position

            val target = hostileEntities[0]
            val pathBuilder = LinePathBuilder(target.getComponent(EncounterLocationComponent::class).position)
            // TODO: Do damage
            //Rulebook.resolveAction(FireProjectileAction(player, 0, pathBuilder, 0, ProjectileType.LASER), encounterState)
        }
    }

    fun runPlayerTurn(encounterState: EncounterState, playerAction: Action) {
        if (encounterState.completed) { return }

        // Move the player
        Rulebook.resolveAction(playerAction, encounterState)
        val speedComponent = playerAction.actor.getComponent(SpeedComponent::class)
        playerAction.actor.getComponent(ActionTimeComponent::class).endTurn(speedComponent)

        // Update the FoV for the player
        encounterState.calculatePlayerFoVAndMarkExploration()

        // Shoot the player's laser
        fireLaser(encounterState, playerAction.actor)
    }

    fun runUntilPlayerReady(encounterState: EncounterState) {
        if (encounterState.completed) { return }

        var isPlayerReady = runNextActiveTick(encounterState)
        while (!isPlayerReady && !encounterState.completed) {
            isPlayerReady = runNextActiveTick(encounterState)
        }
        encounterState.calculatePlayerFoVAndMarkExploration()
    }

    fun runNextActiveTick(encounterState: EncounterState): Boolean {
        if (encounterState.completed) { return false }

        // Run the clock until the next entity is ready
        val ticksToNext = ticksToNextEvent(encounterState)
        val readyEntities = passTimeAndGetReadyEntities(encounterState, ticksToNext)
        encounterState.advanceTime(ticksToNext)

        // If the player is the next ready entity, abort
        if (readyEntities.first().hasComponent(PlayerComponent::class)) {
            return true
        }

        logger.info("========== START OF TURN ${encounterState.currentTime} ==========")
        // TODO: Caching of various iterables, if crawling nodes is slow?
        while (readyEntities.isNotEmpty() && !readyEntities.first().hasComponent(PlayerComponent::class)) {
            val entity = readyEntities.first()
            readyEntities.removeAt(0)
            if (entity.hasComponent(AIComponent::class)) {
                val nextActions = entity.getComponent(AIComponent::class).decideNextActions(encounterState)
                logger.debug("Actions: $nextActions")
                Rulebook.resolveActions(nextActions, encounterState)
                val speedComponent = entity.getComponent(SpeedComponent::class)
                entity.getComponent(ActionTimeComponent::class).endTurn(speedComponent)
            }
        }

        // lol
        val remainingAIEntities = encounterState.entities().filter {
            it.hasComponent(AIComponent::class) && it.hasComponent(FactionComponent::class)
        }
        val anyHostileRelationships = remainingAIEntities.any { leftEntity ->
            val faction = leftEntity.getComponent(FactionComponent::class)
            remainingAIEntities.any { rightEntity ->
                faction.isHostileTo(rightEntity.id, encounterState)
            } || faction.isHostileTo(encounterState.playerEntity().id, encounterState)
        }
        if (!anyHostileRelationships) {
            logger.info("!!!!!!!!!! ENCOUNTER HAS NO REMAINING HOSTILES, SHOULD END! !!!!!!!!!!")
            encounterState.completeEncounter()
        }

        logger.info("========== END OF TURN ${encounterState.currentTime} ==========")
        return false
    }

    fun runEncounter(encounterState: EncounterState, timeLimit: Int = 1000) {
        when {
            encounterState.completed -> throw CannotRunCompletedEncounterException()
            encounterState.currentTime >= timeLimit -> throw CannotRunTimeLimitedException()
            else -> {
                while (!encounterState.completed && encounterState.currentTime < timeLimit) {
                    this.runNextActiveTick(encounterState)
                }
            }
        }
    }

    class CannotRunCompletedEncounterException : Exception("Cannot run next turn on a completed encounter!")
    class CannotRunTimeLimitedException : Exception("Cannot run next turn on an encounter past the time limit!")
}
