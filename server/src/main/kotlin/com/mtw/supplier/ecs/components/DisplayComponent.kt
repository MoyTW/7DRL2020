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
    val seeInFoW: Boolean,
    val backgroundRGB: RGB? = null,
    val foregroundRGB: RGB? = null,
    val character: Char? = null
): Component() {
    override var _parentId: String? = null

    @Transient
    private var tile: Tile? = null

    fun tileBuilder(): TileBuilder {
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
        return builder
    }

    fun toTile(): Tile {
        if (tile == null) {
            this.tile = tileBuilder().build()
        }
        return tile!!
    }
}