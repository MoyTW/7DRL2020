package com.mtw.supplier.encounter.state

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.CollisionComponent
import com.mtw.supplier.ecs.components.RoomPositionComponent
import com.mtw.supplier.ecs.components.PlayerComponent
import com.mtw.supplier.ecs.components.TerrorComponent
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.ActionType
import com.mtw.supplier.encounter.state.map.*
import com.mtw.supplier.utils.AbsolutePosition
import kotlinx.serialization.Serializable
import kotlin.math.min

@Serializable
class EncounterMessageLog {
    private val LOG_LENGTH = 100
    private val eventLog: MutableList<String> = mutableListOf()

    private fun addEntry(text: String) {
        if (eventLog.size >= LOG_LENGTH) {
            eventLog.removeAt(LOG_LENGTH - 1)
        }
        eventLog.add(0, text)
    }

    fun logAction(action: Action, status: String, text: String) {
        if (action.actionType == ActionType.MOVE) { return }
        val actionString = text //"[${action.actor.name}]:[${action.actionType.name}]:[$status] $text"
        addEntry(actionString)
    }

    fun logEvent(eventType: String, text: String) {
        //val eventString = "<$eventType> $text"
        addEntry(text)
    }

    fun getMessages(n: Int? = null): List<String> {
        return if (n != null) {
            eventLog.subList(0, min(eventLog.size, n))
        } else {
            eventLog
        }
    }
}

@Serializable
class EncounterState(
    private var _currentTime: Int = 0,
    private var _completed: Boolean = false,
    private var entityIdIdx: Int = 0 // TODO: uh this be dumb tho
) {
    val messageLog: EncounterMessageLog = EncounterMessageLog()
    var fovCache: FoVCache? = null

    val currentTime: Int
        get() = this._currentTime

    val completed: Boolean
        get() = this._completed

    fun calculatePlayerFoVAndMarkExploration() {
        this.fovCache = FoVCache.computeFoV(this.dreamMap,
            this.playerEntity().getComponent(RoomPositionComponent::class).asAbsolutePosition(this)!!,
            5
        ) // TOOD: Vision radius
        for (pos in this.fovCache!!.visiblePositions) {
            dreamMap.markSeen(pos, this.currentTime)
        }
        dreamMap.markOccupied(this.playerEntity().getComponent(RoomPositionComponent::class).roomUuid, this.currentTime)
    }

    fun getNextEntityId(): Int {
        entityIdIdx += 1
        return entityIdIdx
    }

    // TODO: Map sizing
    private val dreamMap: DreamMap = DreamMapBuilder().build()

    fun drawAndConnectRoom(exitDirection: ExitDirection) {
        val playerRoom = this.playerEntity().getComponent(RoomPositionComponent::class).roomUuid
        this.dreamMap.drawAndConnectRoom(playerRoom, exitDirection)
    }

    fun getDreamMapI(): DreamMapI {
        return dreamMap
    }

    fun playerTerrorPercentage(): Double {
        val terrorComponent = this.playerEntity().getComponent(TerrorComponent::class)
        if (terrorComponent.currentTerror <= terrorComponent.minTerror) {
            return 0.0
        }
        val num = terrorComponent.currentTerror - terrorComponent.minTerror
        val denom = terrorComponent.maxTerror - terrorComponent.minTerror
        return num.toDouble() / denom.toDouble() * 100
    }

    fun lastSeenRoomNames(): List<String> {
        return dreamMap.lastSeenRoomNames()
    }

    fun advanceTime(timeDiff: Int) {
        this._currentTime += timeDiff
    }

    fun completeEncounter() {
        if (this._completed) {
            throw EncounterCannotBeCompletedTwiceException()
        }
        this._completed = true
    }
    class EncounterCannotBeCompletedTwiceException: Exception("Encounter cannot be completed twice!")


    // TODO: Possibly maintain internal list?
    fun entities(): List<Entity> {
        return this.dreamMap.entities
    }

    fun playerEntity(): Entity {
        return this.entities().first { it.hasComponent(PlayerComponent::class) }
    }

    fun currentRoomName(): String {
        return this.dreamMap.getDreamRoomName(this.playerEntity().getComponent(RoomPositionComponent::class).roomUuid)
    }

    fun currentRoomCommentary(): String {
        return this.dreamMap.getDreamRoomCommentary(this.playerEntity().getComponent(RoomPositionComponent::class).roomUuid)
    }

    fun getEntity(entityId: String): Entity {
        return entities().firstOrNull { it.id == entityId } ?: throw EntityIdNotFoundException(entityId)
    }
    class EntityIdNotFoundException(entityId: String): Exception("Entity id $entityId could not be found!")

    fun getBlockingEntityAtPosition(pos: AbsolutePosition): Entity? {
        return this.dreamMap.getEntitiesAtPosition(pos).firstOrNull { it.getComponentOrNull(CollisionComponent::class)?.blocksMovement ?: false }
    }

    fun positionBlocked(pos: AbsolutePosition): Boolean {
        return this.dreamMap.positionBlocked(pos)
    }

    fun arePositionsAdjacent(pos1: AbsolutePosition, pos2: AbsolutePosition): Boolean {
        return this.dreamMap.arePositionsAdjacent(pos1, pos2)
    }

    fun randomUnblockedPosition(): AbsolutePosition {
        return dreamMap.randomUnblockedPosition()
    }

    fun adjacentUnblockedPositions(pos: AbsolutePosition): List<AbsolutePosition> {
        return this.dreamMap.adjacentUnblockedPositions(pos)
    }

    /**
     * @throws EntityAlreadyHasLocation when a node already has a location
     * @throws NodeHasInsufficientSpaceException when node cannot find space for the entity
     */
    fun placeEntity(entity: Entity, targetPosition: AbsolutePosition, ignoreCollision: Boolean = false): EncounterState {
        this.dreamMap.placeEntity(entity, targetPosition, ignoreCollision)
        return this
    }

    fun removeEntity(entity: Entity): EncounterState {
        this.dreamMap.removeEntity(entity)
        return this
    }

    fun teleportEntity(entity: Entity, targetPosition: AbsolutePosition, ignoreCollision: Boolean = false) {
        this.dreamMap.teleportEntity(entity, targetPosition, ignoreCollision)
    }

    fun roomToAbsolutePosition(roomPosition: RoomPosition): AbsolutePosition? {
        return this.dreamMap.roomToAbsolutePosition(roomPosition)
    }
}

