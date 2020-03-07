package com.mtw.supplier.encounter.rulebook.actions

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.Memory
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.ActionType

class InspectAction(
    actor: Entity,
    val target: Entity,
    var completed: Boolean = false,
    var headerText: String? = null,
    var bodyText: String? = null,
    var memory: Memory? = null
): Action(actor, ActionType.INSPECT)