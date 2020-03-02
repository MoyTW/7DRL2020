package com.mtw.supplier.ecs.components.ai

import com.mtw.supplier.ecs.components.EncounterLocationComponent
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.actions.MoveAction
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.encounter.state.EncounterStateUtils
import kotlinx.serialization.Serializable

@Serializable
class EnemyScoutAIComponent(): AIComponent() {
    override var _parentId: String? = null
    override var isActive: Boolean = false

    override fun decideNextActions(encounterState: EncounterState): List<Action> {
        if (!isActive) { return listOf() }

        val actions = mutableListOf<Action>()

        val parent = encounterState.getEntity(this.parentId)
        val parentPos = parent.getComponent(EncounterLocationComponent::class).roomPosition
        val playerPos = encounterState.playerEntity().getComponent(EncounterLocationComponent::class).roomPosition

        // Close distance
        if (EncounterStateUtils.distanceBetween(parentPos, playerPos) >= 5f) {
            val path = EncounterStateUtils.aStarWithNewGrid(parentPos, playerPos, encounterState)
            if (path != null) {
                actions.add(MoveAction(actor = parent, targetPosition = path[0]))
            }
        }
        return actions
    }
}