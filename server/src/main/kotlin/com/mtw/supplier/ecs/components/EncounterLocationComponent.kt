package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.utils.XYCoordinates
import kotlinx.serialization.Serializable

@Serializable
class EncounterLocationComponent(var position: XYCoordinates, override var _parentId: Int? = null): Component()