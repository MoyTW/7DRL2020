package com.mtw.supplier.encounter.rulebook.actions

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.ActionType
import com.mtw.supplier.utils.XYCoordinates

class MoveAction(actor: Entity, val targetPosition: XYCoordinates): Action(actor, ActionType.MOVE)