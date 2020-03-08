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
import com.mtw.supplier.encounter.state.map.RoomPosition
import com.mtw.supplier.utils.AbsolutePosition
import kotlinx.serialization.Serializable


@Serializable
class VegasStripMiddleAgedTouristAIComponent: AIComponent() {
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
                in 1..20 -> {
                    val adjacentOpen = encounterState.adjacentUnblockedPositions(parentAbsPos)
                    if (adjacentOpen.isEmpty()) {
                        listOf(WaitAction(parent, null))
                    } else
                        listOf(MoveAction(parent, adjacentOpen.random()))
                    }
                in 21..40 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(-1, 40, 100,
                    "\"Oh, Mildred, let's get a picture!\"")))
                in 41..60 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(-1, 40, 100,
                    "\"Oh look Mildred, Excalibur!\"")))
                in 61..80 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(-1, 40, 100,
                    "\"Just six inches to the left please, James.\"")))
                else -> listOf(TerrifyAction(parent, player, TerrorChangeStats(-1, 40, 100,
                    "\"Oh, James, that's so sweet!\"")))
            }
        }
    }

    private fun nearestPerformer(parentRoomPosition: RoomPosition, parentAbsolutePosition: AbsolutePosition, encounterState: EncounterState): AbsolutePosition {
        return encounterState.entitiesInRoom(parentRoomPosition.roomUuid)
            .mapNotNull { it.getComponent(RoomPositionComponent::class).asAbsolutePosition(encounterState) }
            .minBy { EncounterStateUtils.distanceBetween(it, parentAbsolutePosition)
            }!!
    }

    private fun moveTo(parent: Entity, parentPos: AbsolutePosition, targetPos: AbsolutePosition, encounterState: EncounterState): MoveAction? {
        val path = EncounterStateUtils.aStarWithNewGrid(parentPos, targetPos, encounterState)
        return if (path != null) {
            MoveAction(actor = parent, targetPosition = path[0])
        } else {
            null
        }
    }
}
