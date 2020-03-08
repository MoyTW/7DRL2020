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
class HospitalErElderlyCougherAIComponent: AIComponent() {
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

        return if (!isInFow) {
            listOf(WaitAction(parent, null))
        } else if (!isAdjacent) {
            when ((1..100).random()) {
                in 1..25 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(3, 0, 75,
                    "The octogenarian hacks and coughs.")))
                in 26..40 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(2, 0, 90,
                    "The octogenarian stares at you, eyes full of terror.")))
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
            when ((1..100).random()) {
                in 1..25 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(5, 0, 75,
                    "The octogenarian's rasping breath is loud.")))
                in 26..75 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(7, 0, 95,
                    "\"please\" the octogenarian whispers, \"i can't breathe\"")))
                in 76..95 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(10, 0, 100,
                    "The octogenarian's wet cough sounds like death.")))
                else -> listOf(TerrifyAction(parent, player, TerrorChangeStats(20, 0, 100,
                    "The octogenarian falls over, dead")))
                }
            }
        }

    private fun moveToPlayer(parent: Entity, parentPos: AbsolutePosition, playerPos: AbsolutePosition,
                             encounterState: EncounterState): MoveAction? {
        val path = EncounterStateUtils.aStarWithNewGrid(parentPos, playerPos, encounterState)
        return if (path != null) {
            MoveAction(actor = parent, targetPosition = path[0])
        } else {
            null
        }
    }
}
