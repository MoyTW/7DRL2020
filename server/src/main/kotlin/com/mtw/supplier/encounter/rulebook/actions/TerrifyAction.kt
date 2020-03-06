package com.mtw.supplier.encounter.rulebook.actions

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.ActionType
import kotlinx.serialization.Serializable

@Serializable
data class TerrorChangeStats(
    val dTerror: Int,
    val changesDownToMin: Int = 0,
    val changesUpToMax: Int = 100,
    val description: String
)

class TerrifyAction(
    actor: Entity,
    val target: Entity,
    val terrorChangeStats: TerrorChangeStats
) : Action(actor, ActionType.TERRIFY)