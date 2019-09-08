package com.mtw.supplier.encounter.rulebook.actions

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.ActionType

class WaitAction(actor: Entity): Action(actor, ActionType.WAIT)