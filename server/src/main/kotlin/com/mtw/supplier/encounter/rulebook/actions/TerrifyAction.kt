package com.mtw.supplier.encounter.rulebook.actions

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.ActionType

class TerrifyAction(
    actor: Entity,
    val target: Entity,
    val terrorAmount: Int,
    val description: String
) : Action(actor, ActionType.TERRIFY)