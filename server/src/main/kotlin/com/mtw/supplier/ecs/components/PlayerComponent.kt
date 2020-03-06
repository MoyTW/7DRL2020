package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.ecs.Entity
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
    val dTerror: Int,
    val changesDownToMin: Int = 0,
    val changesUpToMax: Int = 100
) : Memory(name) {
    override fun remember(encounterState: EncounterState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

@Serializable
class PlayerComponent(
    var targeted: Entity? = null,
    val memories: MutableList<Memory> = mutableListOf()
): Component() {
    override var _parentId: String? = null


}