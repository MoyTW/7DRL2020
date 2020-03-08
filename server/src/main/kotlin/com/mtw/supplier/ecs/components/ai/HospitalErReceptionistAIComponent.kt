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

@Serializable
class HospitalErReceptionistAIComponent: AIComponent() {
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
        val playerAbsPos = playerRoomPos.asAbsolutePosition(encounterState)!!

        val isInFow = encounterState.fovCache!!.isInFoV(parentAbsPos)
        val isAdjacent = encounterState.arePositionsAdjacent(parentAbsPos, playerAbsPos)

        if (!isInFow) {
            val playerMove = moveToPlayer(parent, parentAbsPos, playerAbsPos, encounterState)
            return if (playerMove != null) {
                listOf(playerMove)
            } else {
                listOf(WaitAction(parent, null))
            }
        } else if (!isAdjacent) {
            return when ((1..100).random()) {
                in 1..25 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(1, 0, 60,
                    "The receptionist looks around urgently for your mother")))
                else -> {
                    val playerMove = moveToPlayer(parent, parentAbsPos, playerAbsPos, encounterState)
                    if (playerMove != null) {
                        listOf(playerMove)
                    } else {
                        listOf(WaitAction(parent, null))
                    }
                }
            }
        } else {
            return when ((1..100).random()) {
                in 1..50 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(-3, 60, 100,
                    "She speaks calmly and soothingly to you.")))
                in 51..75 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(-2, 70, 100,
                    "You can't hear what she's saying, but it reassures you.")))
                in 76..95 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(-4, 50, 100,
                    "She'll get you help.")))
                else -> {
                    listOf(WaitAction(parent, null))
                }
            }
        }
    }

    private fun moveToPlayer(parent: Entity, parentPos: AbsolutePosition, playerPos: AbsolutePosition,
                             encounterState: EncounterState): MoveAction? {
        val path = EncounterStateUtils.aStarWithNewGrid(parentPos, playerPos, encounterState)
        if (path != null) {
            return MoveAction(actor = parent, targetPosition = path[0])
        } else {
            return null
        }
    }
}