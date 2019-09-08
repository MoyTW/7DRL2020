package com.mtw.supplier.ecs.components.ai

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.state.EncounterState
import kotlinx.serialization.Serializable


@Serializable
abstract class AIComponent : Component() {
    abstract var isActive: Boolean
    abstract fun decideNextActions(encounterState: EncounterState): List<Action>
}
