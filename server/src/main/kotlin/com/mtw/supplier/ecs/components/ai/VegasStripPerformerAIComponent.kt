package com.mtw.supplier.ecs.components.ai

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.RoomPositionComponent
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.actions.MoveAction
import com.mtw.supplier.encounter.rulebook.actions.TerrifyAction
import com.mtw.supplier.encounter.rulebook.actions.TerrorChangeStats
import com.mtw.supplier.encounter.rulebook.actions.WaitAction
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.encounter.state.EncounterStateUtils
import com.mtw.supplier.utils.AbsolutePosition
import kotlinx.serialization.Serializable


enum class PerformerType {
    POLICEMAN,
    BIRD_OF_PARADISE
}

@Serializable
class VegasStripPerformerAIComponent(val performerType: PerformerType): AIComponent() {
    override var _parentId: String? = null
    override var isActive: Boolean = true

    override fun decideNextActions(encounterState: EncounterState): List<Action> {
        val parent = this.getParent(encounterState)
        val parentRoomPos = parent.getComponent(RoomPositionComponent::class)

        val player = encounterState.playerEntity()
        val playerRoomPos = player.getComponent(RoomPositionComponent::class)

        if (parentRoomPos.roomUuid != playerRoomPos.roomUuid) {
            return listOf(WaitAction(parent, null))
        }

        val parentAbsPos = parentRoomPos.asAbsolutePosition(encounterState)!!

        val isInFow = encounterState.fovCache?.isInFoV(parentAbsPos) ?: return listOf(WaitAction(parent, null))

        return if (!isInFow) {
            listOf(WaitAction(parent, null))
        } else {
            when ((1..100).random()) {
                in 1..50 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(-1, 60, 100,
                    "The performer strikes a pose.")))
                else -> {
                    val adjacent = encounterState.adjacentUnblockedPositions(parentAbsPos)
                    if (adjacent.isNotEmpty()) {
                        listOf(MoveAction(parent, adjacent.random()))
                    } else {
                        listOf(WaitAction(parent, null))
                    }
                }
            }
        }
    }
}

