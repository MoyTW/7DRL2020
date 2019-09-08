package com.mtw.supplier.encounter.rulebook.actions

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.ActionType
import com.mtw.supplier.utils.Path
import com.mtw.supplier.utils.PathBuilder


enum class ProjectileType(val displayName: String) {
    LASER("laser beam"),
    SHOTGUN_PELLET("shotgun pellet")
}

class FireProjectileAction(
    actor: Entity,
    val damage: Int,
    val pathBuilder: PathBuilder,
    val speed: Int,
    val projectileType: ProjectileType,
    val numProjectiles: Int = 1
): Action(actor, actionType = ActionType.FIRE_PROJECTILE)