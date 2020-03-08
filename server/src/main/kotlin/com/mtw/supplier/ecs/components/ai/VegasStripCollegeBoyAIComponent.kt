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
class VegasStripCollegeBoyAIComponent: AIComponent() {
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
            val nearestPerformer = nearestPerformer(parentRoomPos.roomPosition, parentAbsPos, encounterState) ?: return listOf(WaitAction(parent, null))
            val move = moveTo(parent, parentAbsPos, nearestPerformer, encounterState)
            return if (move != null) {
                listOf(move)
            } else {
                listOf(WaitAction(parent, null))
            }
        } else {
            when ((1..100).random()) {
                in 1..25 -> {
                    val talk = TerrifyAction(parent, player, TerrorChangeStats(2, 0, 75,
                        "\"Daaaaam!\" yells the college-aged man, pointing."))
                    val nearestPerformer = nearestPerformer(parentRoomPos.roomPosition, parentAbsPos, encounterState) ?: return listOf(talk)
                    val move = moveTo(parent, parentAbsPos, nearestPerformer, encounterState)
                    return if (move != null) {
                        listOf(move, talk)
                    } else {
                        listOf(talk)
                    }
                }
                in 26..50 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(3, 0, 80,
                    "The joshing of the college-aged men rings in your ears.")))
                in 51..75 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(5, 0, 80,
                    "\"Where's my camera!\" yells one of the college-aged men.")))
                in 76..95 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(5, 0, 80,
                    "\"Fuck yeah!\" calls one of the college-aged men.")))
                else -> listOf(TerrifyAction(parent, player, TerrorChangeStats(10, 0, 100,
                    "One of the college-aged men looks directly at you.")))
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
