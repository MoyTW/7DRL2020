package com.mtw.supplier.utils

import kotlinx.serialization.Serializable

@Serializable
data class XYCoordinates(
    val x: Int,
    val y: Int
)