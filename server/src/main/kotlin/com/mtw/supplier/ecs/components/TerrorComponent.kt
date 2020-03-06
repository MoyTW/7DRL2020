package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import kotlinx.serialization.Serializable

@Serializable
class TerrorComponent(
    val minTerror: Int = 0,
    val maxTerror: Int = 100,
    private var _currentTerror: Int = 50
): Component() {
    override var _parentId: String? = null

    val currentTerror: Int
        get() = _currentTerror

    fun applyTerror(amount: Int) {
        this._currentTerror += amount
    }

    fun setTerror(terror: Int) {
        if (terror > maxTerror) {
            this._currentTerror = maxTerror
        } else if (terror < minTerror) {
            this._currentTerror = minTerror
        }
        this._currentTerror = terror
    }
}