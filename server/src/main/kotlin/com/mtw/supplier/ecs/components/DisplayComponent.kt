package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import kotlinx.serialization.Serializable
import org.hexworks.zircon.api.builder.data.TileBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile

@Serializable
data class RGB(val red: Int, val green: Int, val blue: Int) {
    companion object {
        fun fromTileColor(tileColor: TileColor): RGB {
            return RGB(tileColor.red, tileColor.green, tileColor.blue)
        }
    }
}

@Serializable
class DisplayComponent(
    val backgroundRGB: RGB? = null,
    val foregroundRGB: RGB? = null,
    val character: Char? = null
): Component() {
    override var _parentId: String? = null

    @Transient
    private var tile: Tile? = null

    fun toTile(): Tile {
        if (tile == null) {
            val builder = TileBuilder.newBuilder()
                .withBackgroundColor(
                    if (backgroundRGB == null) {
                        TileColor.transparent()
                    } else {
                        TileColor.create(backgroundRGB.red, backgroundRGB.green, backgroundRGB.blue)
                    }
                ).withForegroundColor(
                    if (foregroundRGB == null) {
                        TileColor.transparent()
                    } else {
                        TileColor.create(foregroundRGB.red, foregroundRGB.green, foregroundRGB.blue)
                    }
                )
            if (character != null) {
                builder.withCharacter(character)
            }
            this.tile = builder.build()
        }
        return tile!!
    }
}