package com.mtw.supplier.ecs.components.ai

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.RoomPositionComponent
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.actions.MoveAction
import com.mtw.supplier.encounter.rulebook.actions.TerrifyAction
import com.mtw.supplier.encounter.rulebook.actions.WaitAction
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.encounter.state.EncounterStateUtils
import com.mtw.supplier.utils.AbsolutePosition
import kotlinx.serialization.Serializable

@Serializable
class FamiliarFigureAIComponent: AIComponent() {
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
        val playerAbsPos = playerRoomPos.asAbsolutePosition(encounterState)!!

        val isAdjacent = encounterState.arePositionsAdjacent(parentAbsPos, playerAbsPos)

        if (!isAdjacent) {
            return when ((1..100).random()) {
                in 1..25 -> listOf(WaitAction(parent, "He's not paying you any attention."))
                in 26..50 -> listOf(moveToPlayer(parent, parentAbsPos, playerAbsPos, encounterState) ?: WaitAction(parent, "He can't find a way to you."))
                in 51..75 -> listOf(TerrifyAction(parent, player, 2, 0, 70,
                    "He looks you up and down, then grins. \"Hi, kid\"."))
                in 76..90 -> listOf(TerrifyAction(parent, player, 3, 0, 75,
                    "He calls out to you. You feel a creeping dread."))
                else -> {
                    val move = moveToPlayer(parent, parentAbsPos, playerAbsPos, encounterState)
                    if (move != null) {
                        listOf(move, TerrifyAction(parent, player, 5, 0, 80, "\"Hey!\" he yells, walking towards you."))
                    } else {
                        listOf(TerrifyAction(parent, player, 0, 0, 100,"\"Hey!\" he yells, but he can't get to you."))
                    }
                }
            }
        } else {
            return when ((1..100).random()) {
                in 1..25 -> listOf(WaitAction(parent, null))
                in 26..50 -> listOf(TerrifyAction(parent, player, 3, 0, 80,
                    "He converses, politely. He can act normal, in public."))
                in 51..75 -> listOf(TerrifyAction(parent, player, 5, 0, 90,
                    "He is standing far too close as he speaks."))
                in 76..90 -> listOf(TerrifyAction(parent, player, 8, 0, 100,
                    "You're screaming."))
                else -> listOf(TerrifyAction(parent, player, 10, 0, 100, "You can't breathe."))
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