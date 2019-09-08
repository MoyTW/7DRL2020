package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import kotlinx.serialization.Serializable

@Serializable
class ActionTimeComponent(
    private var _ticksUntilTurn: Int = 0
) : Component() {
    override var _parentId: Int? = null

    val ticksUntilTurn: Int
        get() = _ticksUntilTurn

    fun isReady(): Boolean {
        return this.ticksUntilTurn == 0
    }

    fun passTime(ticks: Int) {
        // TODO: Buff expirations!

        if (this._ticksUntilTurn - ticks < 0) {
            throw CannotPassTicksPastZero(
                "Entity ${this.parentId} could not pass ticks - had ${this._ticksUntilTurn}, passed $ticks")
        }
        this._ticksUntilTurn -= ticks
    }
    class CannotPassTicksPastZero(message: String): Exception(message)

    fun endTurn(speedComponent: SpeedComponent) {
        if (this._ticksUntilTurn != 0) {
            throw CannotEndTurnIfTicksNotZero("Entity ${this.parentId} could not end turn - still had ticks!")
        }
        this._ticksUntilTurn = speedComponent.speed
    }
    class CannotEndTurnIfTicksNotZero(message: String): Exception(message)
}
