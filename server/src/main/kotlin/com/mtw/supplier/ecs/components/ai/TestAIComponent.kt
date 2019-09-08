package com.mtw.supplier.ecs.components.ai

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.EncounterLocationComponent
import com.mtw.supplier.ecs.components.FactionComponent
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.actions.AttackAction
import com.mtw.supplier.encounter.rulebook.actions.MoveAction
import com.mtw.supplier.encounter.rulebook.actions.WaitAction
import com.mtw.supplier.encounter.state.EncounterStateUtils.aStarWithNewGrid
import com.mtw.supplier.utils.XYCoordinates
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.math.abs


@Serializable
class TestAIComponent : AIComponent() {
    override var _parentId: Int? = null
    override var isActive: Boolean = true

    private fun parentIsHostileTo(parentEntity: Entity, otherEntity: Entity, encounterState: EncounterState): Boolean {
        return parentEntity.getComponent(FactionComponent::class).isHostileTo(otherEntity.id, encounterState)
    }

    override fun decideNextActions(encounterState: EncounterState): List<Action> {
        val parentEntity = encounterState.getEntity(this.parentId)
        // TODO: All of this is a placeholder
        val firstOtherAliveEnemy = encounterState.entities()
            .firstOrNull {
                it != parentEntity &&
                    it.hasComponent(AIComponent::class) &&
                    it.hasComponent(FactionComponent::class) &&
                    this.parentIsHostileTo(parentEntity, it, encounterState)
            }
            ?: return listOf(WaitAction(parentEntity))


        val parentLocation = parentEntity.getComponent(EncounterLocationComponent::class).position
        val firstOtherEntityLocation = firstOtherAliveEnemy.getComponent(EncounterLocationComponent::class).position

        // wow ugly!
        return if (encounterState.arePositionsAdjacent(parentLocation, firstOtherEntityLocation)) {
            listOf(AttackAction(parentEntity, firstOtherAliveEnemy))
        } else  {
            val pathToFirstOtherEntity = aStarWithNewGrid(parentLocation, firstOtherEntityLocation, encounterState)
            if (pathToFirstOtherEntity != null) {
                listOf(MoveAction(parentEntity, pathToFirstOtherEntity[0]))
            } else {
                listOf(WaitAction(parentEntity))
            }
        }
    }
}