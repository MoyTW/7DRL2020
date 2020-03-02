package com.mtw.supplier.encounter.rulebook.actions

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.ActionType
import com.mtw.supplier.utils.AbsolutePosition

class MoveAction(actor: Entity, val targetPosition: AbsolutePosition): Action(actor, ActionType.MOVE)