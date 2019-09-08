package com.mtw.supplier.encounter.rulebook

import com.mtw.supplier.ecs.Entity

abstract class Action(val actor: Entity, val actionType: ActionType)