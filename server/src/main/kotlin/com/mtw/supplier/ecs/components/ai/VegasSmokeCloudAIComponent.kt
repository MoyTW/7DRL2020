package com.mtw.supplier.ecs.components.ai

import com.mtw.supplier.ecs.components.RoomPositionComponent
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.actions.TerrifyAction
import com.mtw.supplier.encounter.rulebook.actions.TerrorChangeStats
import com.mtw.supplier.encounter.rulebook.actions.WaitAction
import com.mtw.supplier.encounter.state.EncounterState
import kotlinx.serialization.Serializable

@Serializable
class VegasSmokeCloudAIComponent: AIComponent() {
    override var _parentId: String? = null
    override var isActive: Boolean = true

    override fun decideNextActions(encounterState: EncounterState): List<Action> {
        val parent = this.getParent(encounterState)
        val parentRoomPos = parent.getComponent(RoomPositionComponent::class)

        val player = encounterState.playerEntity()
        val playerRoomPos = player.getComponent(RoomPositionComponent::class)

        if (parentRoomPos.roomUuid != playerRoomPos.roomUuid) {
            return mutableListOf(WaitAction(parent, null))
        }

        val parentAbsPos = parentRoomPos.asAbsolutePosition(encounterState)!!

        val isInFow = encounterState.fovCache?.isInFoV(parentAbsPos) ?: return listOf(WaitAction(parent, null))

        return if (!isInFow) {
            listOf(WaitAction(parent, null))
        } else {
            when ((1..100).random()) {
                in 1..95 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(1, 0, 80,
                    "The smoke is getting into your lungs. It's getting worse.")))
                else -> listOf(TerrifyAction(parent, player, TerrorChangeStats(5, 0, 100,
                    "Your wheezing is loud in your ears. You need to get out.")))
            }
        }
    }
}