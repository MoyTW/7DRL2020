package com.mtw.supplier.ecs.components.ai

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.InspectableComponent
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
class StrangePlaceETAIComponent: AIComponent() {
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
        val isInFow = encounterState.fovCache?.isInFoV(parentAbsPos) ?: return listOf(WaitAction(parent, null))

        return if (!isInFow) {
            val move = moveToPlayer(parent, parentAbsPos, playerAbsPos, encounterState)
            if (move != null) {
                listOf(move)
            } else {
                listOf(WaitAction(parent, null))
            }
        } else if (!isAdjacent) {
            when ((1..100).random()) {
                in 1..50 -> listOf(WaitAction(parent, "The alien looks around."))
                else -> {
                    val move = moveToPlayer(parent, parentAbsPos, playerAbsPos, encounterState)
                    val etSeen = (etEventSeen(parent))

                    if (move != null) {
                        if (etSeen) {
                            listOf(move, TerrifyAction(parent, player, TerrorChangeStats(-2, 50, 100,
                                "Aww, ET's walking towards you!")))
                        } else {
                            listOf(move, TerrifyAction(parent, player, TerrorChangeStats(2, 0, 80,
                                "The alien's coming towards you!")))
                        }
                    } else {
                        listOf(WaitAction(parent, "The alien looks at the sky."))
                    }
                }
            }
        } else {
            if (!etEventSeen(parent)) {
                when ((1..100).random()) {
                    in 1..90 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(10, 0, 100,
                        "The alien mumbles ominously.")))
                    else -> {
                        listOf(TerrifyAction(parent, player, TerrorChangeStats(20, 0, 100,
                            "Fuck the alien's trying to touch you!")))
                    }
                }
            } else {
                when ((1..100).random()) {
                    in 1..90 -> listOf(TerrifyAction(parent, player, TerrorChangeStats(-5, 50, 100,
                        "\"ET...phone...home.\" You grin.")))
                    else -> {
                        listOf(TerrifyAction(parent, player, TerrorChangeStats(-10, 50, 100,
                            "Oh, ET's doing the finger thing!")))
                    }
                }
            }

        }
    }

    private fun etEventSeen(parent: Entity): Boolean {
        return parent.getComponent(InspectableComponent::class).inspectEvents.isEmpty()
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