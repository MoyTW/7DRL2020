package com.mtw.supplier.ecs.components.ai

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.RoomPositionComponent
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.actions.MoveAction
import com.mtw.supplier.encounter.rulebook.actions.TerrifyAction
import com.mtw.supplier.encounter.rulebook.actions.TerrorChangeStats
import com.mtw.supplier.encounter.rulebook.actions.WaitAction
import com.mtw.supplier.encounter.state.EncounterState
import kotlinx.serialization.Serializable


@Serializable
class HospitalErBabyAIComponent: AIComponent() {
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

        val isInFow = encounterState.fovCache!!.isInFoV(parentAbsPos)

        return if (!isInFow) {
            listOf(WaitAction(parent, null))
        } else {
            when ((1..100).random()) {
                in 1..25 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(1, 0, 60,
                    "The baby's shrill crying unnerves you.")))
                in 26..75 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(4, 0, 70,
                    "The baby's skin is...yellow. That's not right.")))
                else -> listOf(TerrifyAction(parent, player, TerrorChangeStats(10, 0, 80,
                    "Oh my God the baby is somehow scratching its scalp red!")))
            }
        }
    }
}

