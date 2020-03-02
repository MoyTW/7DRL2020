package com.mtw.supplier.utils

import kotlinx.serialization.Serializable

@Serializable
data class AbsolutePosition(
    val x: Int,
    val y: Int
)