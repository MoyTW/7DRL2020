package com.mtw.supplier.encounter.rulebook

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.ecs.components.ai.AIComponent
import com.mtw.supplier.encounter.rulebook.actions.*
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.encounter.state.EncounterMessageLog
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt

object Rulebook {

    fun resolveActions(actions: List<Action>, encounterState: EncounterState) {
        actions.forEach { resolveAction(it, encounterState) }
    }

    fun resolveAction(action: Action, encounterState: EncounterState) {
        when (action.actionType) {
            ActionType.ATTACK -> resolveAttackAction(action as AttackAction, encounterState)
            ActionType.MOVE -> resolveMoveAction(action as MoveAction, encounterState)
            ActionType.USE_ITEM -> TODO()
            ActionType.WAIT -> resolveWaitAction(action as WaitAction, encounterState.messageLog)
            ActionType.SELF_DESTRUCT -> resolveSelfDestructionAction(action as SelfDestructAction, encounterState)
        }
    }

    private fun resolveAttackAction(action: AttackAction, encounterState: EncounterState) {
        val attacker = action.actor
        val attackerPos = attacker.getComponent(RoomPositionComponent::class)
            .asAbsolutePosition(encounterState)!! // TODO: this isn't...always true

        val defender = action.target
        val defenderPos = defender.getComponent(RoomPositionComponent::class)
            .asAbsolutePosition(encounterState)!! // TODO: this isn't...always true

        // TODO: Range & visibility & such
        if (!encounterState.arePositionsAdjacent(attackerPos, defenderPos)) {
            encounterState.messageLog.logAction(action, "INVALID", "[${action.actor.name}] cannot reach [${action.target.name}]")
        } else {
            val attackerFighter = attacker.getComponent(FighterComponent::class)
            val defenderFighter = defender.getComponent(FighterComponent::class)

            // TODO: Properly controlled randomness
            val r = Random(4)
            val d100Roll = r.nextInt(100) + 1

            // TODO: Shamelessly stealing POE because why not but maybe actually consider mechanics
            val modifiedAttackRoll = d100Roll + attackerFighter.toHit - defenderFighter.toDodge
            when {
                modifiedAttackRoll < 30 -> {
                    encounterState.messageLog.logAction(action, "MISS", "(raw=$d100Roll,final=$modifiedAttackRoll) [${action.actor.name}] missed [${action.target.name}]")
                }
                modifiedAttackRoll in 31..50 -> {
                    val damage = ceil(attackerFighter.hitDamage * .5).roundToInt()
                    encounterState.messageLog.logAction(action, "GRAZE", "(raw=$d100Roll,final=$modifiedAttackRoll) [${action.actor.name}] grazed [${action.target.name}] for $damage damage!")
                    applyDamage(damage, defender, encounterState.messageLog)
                }
                modifiedAttackRoll in 51..100 -> {
                    val damage = attackerFighter.hitDamage
                    encounterState.messageLog.logAction(action, "HIT", "(raw=$d100Roll,final=$modifiedAttackRoll) [${action.actor.name}] hit [${action.target.name}] for $damage damage!")
                    applyDamage(damage, defender, encounterState.messageLog)
                }
                modifiedAttackRoll > 100 -> {
                    val damage = ceil(attackerFighter.hitDamage * 1.25).roundToInt()
                    encounterState.messageLog.logAction(action, "CRIT", "(raw=$d100Roll,final=$modifiedAttackRoll) [${action.actor.name}] critically hit [${action.target.name}] for $damage damage!")
                    applyDamage(damage, defender, encounterState.messageLog)
                }
            }
        }
    }

    // TODO: Better rules
    private fun applyDamage(damage: Int, entity: Entity, messageLog: EncounterMessageLog) {
        val hpComponent = entity.getComponent(HpComponent::class)
        hpComponent.removeHp(damage)
        if (hpComponent.currentHp < 0) {
            // TODO: "No AI == dead" is a sketchy definition of dead!
            entity.removeComponent(AIComponent::class)
            messageLog.logEvent("DEATH", "[${entity.name}] is dead!")
        }
    }

    private fun openDoor(action: MoveAction, encounterState: EncounterState) {
        val door = encounterState.getBlockingEntityAtPosition(action.targetPosition)!!
        val doorDoor = door.getComponent(DoorComponent::class)
        val doorCollision = door.getComponent(CollisionComponent::class)
        // If closed, open. You can't collide with a closed door so once it's open it remains open until you go to another door.
        if (doorDoor.closed) {
            doorCollision.blocksMovement = false
            doorCollision.blocksVision = false
            doorDoor.closed = false
            encounterState.messageLog.logAction(action, "SUCCESS", "${action.actor.name} opened ${door.name}")

            // If you're opened you also need to close all other doors
            val otherDoors = encounterState.getDreamMapI()
                .getDoors(door.getComponent(RoomPositionComponent::class).roomUuid)
                .filter { it.key != doorDoor.direction }
            for (otherDoor in otherDoors) {
                otherDoor.value.getComponent(DoorComponent::class).closed = true
                otherDoor.value.getComponent(CollisionComponent::class).blocksMovement = true
                otherDoor.value.getComponent(CollisionComponent::class).blocksVision = true
                encounterState.messageLog.logEvent("DOOR CLOSED", "The door to the ${doorDoor.direction} slams shut!")
            }
        }
    }

    private fun resolveMoveAction(action: MoveAction, encounterState: EncounterState) {
        val currentPosition = action.actor
            .getComponent(RoomPositionComponent::class)
            .asAbsolutePosition(encounterState)!!  // TODO: this isn't...always true

        val targetNodeSameAsCurrentNode = currentPosition == action.targetPosition
        val targetNodeBlocked = encounterState.positionBlocked(action.targetPosition)
        val targetNodeAdjacent = encounterState.arePositionsAdjacent(currentPosition, action.targetPosition)

        if (targetNodeSameAsCurrentNode) {
            encounterState.messageLog.logAction(action, "INVALID", "Target node ${action.targetPosition} and source node are identical!")
        } else if (encounterState.getBlockingEntityAtPosition(action.targetPosition)?.hasComponent(DoorComponent::class) == true) {
            openDoor(action, encounterState)
        }  else if (targetNodeBlocked) {
            val collisionComponent = action.actor.getComponent(CollisionComponent::class)
            if (collisionComponent.attackOnHit) {
                val blockingEntity = encounterState.getBlockingEntityAtPosition(action.targetPosition)
                if (blockingEntity != null) {
                    resolveAction(AttackAction(action.actor, blockingEntity), encounterState)
                }
            }
            if (collisionComponent.selfDestructOnHit) {
                resolveAction(SelfDestructAction(action.actor), encounterState)
            } else {
                encounterState.messageLog.logAction(action, "INVALID", "Target node ${action.targetPosition} blocked!")
            }
        } else if (!targetNodeAdjacent) {
            encounterState.messageLog.logAction(action, "INVALID", "Current node $currentPosition is not adjacent to target node ${action.targetPosition}!")
        } else {
            encounterState.teleportEntity(action.actor, action.targetPosition)
            encounterState.messageLog.logAction(action, "SUCCESS", "${action.actor.name} $currentPosition to ${action.targetPosition}")
        }
    }

    private fun resolveWaitAction(action: WaitAction, messageLog: EncounterMessageLog) {
        messageLog.logAction(action, "SUCCESS", "[${action.actor.name}] is waiting!")
    }

    private fun resolveSelfDestructionAction(action: SelfDestructAction, encounterState: EncounterState) {
        encounterState.removeEntity(action.actor)
        encounterState.messageLog.logAction(action, "SUCCESS", "[${action.actor.name}] self-destructed!")
    }
}
