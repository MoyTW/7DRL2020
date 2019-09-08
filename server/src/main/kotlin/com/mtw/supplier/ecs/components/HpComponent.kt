package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import kotlinx.serialization.Serializable

@Serializable
class HpComponent(
    var maxHp: Int,
    var currentHp: Int
): Component() {
    override var _parentId: Int? = null

    fun removeHp(hp: Int) {
        this.currentHp -= hp
    }

    fun healHp(hp: Int) {
        this.currentHp += hp
        if (this.currentHp > maxHp) {
            this.currentHp = this.maxHp
        }
    }
}