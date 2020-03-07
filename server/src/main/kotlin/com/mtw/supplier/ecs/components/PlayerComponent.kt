package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.encounter.EncounterRunner
import com.mtw.supplier.encounter.rulebook.actions.TerrifyAction
import com.mtw.supplier.encounter.rulebook.actions.TerrorChangeStats
import com.mtw.supplier.encounter.state.EncounterState
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min

@Serializable
abstract class Memory(
    val name: String
) {
    abstract fun remember(encounterState: EncounterState)
}

class TerrorChangeMemory(
    name: String,
    val terrorChangeStats: TerrorChangeStats
) : Memory(name) {
    override fun remember(encounterState: EncounterState) {
        val player = encounterState.playerEntity()
        EncounterRunner.runPlayerTurn(encounterState, TerrifyAction(player, player, terrorChangeStats))
    }

}

@Serializable
class PlayerComponent(
    var targeted: Entity? = null,
    val memories: MutableList<Memory> = mutableListOf()
): Component() {
    override var _parentId: String? = null
}