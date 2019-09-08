package com.mtw.supplier.ecs.defs

import kotlinx.serialization.Serializable

@Serializable
abstract class Generator {
    abstract fun generate(): Any
}

@Serializable
class FixedIntegerGenerator(val value: Int): Generator() {
    override fun generate(): Int {
        return value
    }
}
