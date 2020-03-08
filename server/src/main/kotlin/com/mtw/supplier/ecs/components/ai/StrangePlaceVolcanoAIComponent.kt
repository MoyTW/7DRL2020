package com.mtw.supplier.ecs.components.ai

import com.mtw.supplier.ecs.components.RoomPositionComponent
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.actions.TerrifyAction
import com.mtw.supplier.encounter.rulebook.actions.TerrorChangeStats
import com.mtw.supplier.encounter.rulebook.actions.WaitAction
import com.mtw.supplier.encounter.state.EncounterState
import kotlinx.serialization.Serializable

@Serializable
class StrangePlaceVolcanoAIComponent: AIComponent() {
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
            listOf(TerrifyAction(parent, player, TerrorChangeStats(2, 0, 100, "You can't see it, but you can feel it quaking.")))
        } else {
            when ((1..100).random()) {
                in 1..50 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(5, 0, 100,
                    "You can see the red near its rim.")))
                else -> listOf(TerrifyAction(parent, player, TerrorChangeStats(8, 0, 100,
                    "It coughs, spitting out ash and smoke.")))
            }
        }
    }
}