package com.mtw.supplier.encounter.state

import com.mtw.supplier.encounter.state.map.DreamMapI
import com.mtw.supplier.utils.AbsolutePosition
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Keeps a list of coordinates visible from the current position.
 */
@Serializable
class FoVCache internal constructor(val visiblePositions: Set<AbsolutePosition>) {

    fun isInFoV(pos: AbsolutePosition): Boolean {
        return visiblePositions.contains(pos)
    }

    companion object {
        fun computeFoV(tileMapI: DreamMapI, center: AbsolutePosition, radius: Int): FoVCache {
            val visibleCells = RPASCal.calcVisibleCellsFrom(center, radius) {
                tileMapI.getDreamTileI(it.x, it.y)?.blocksVision == false
            }.filter {
                0 <= it.x && it.x < tileMapI.width && 0 <= it.y && it.y < tileMapI.height
            }.toSet()
            return FoVCache(visibleCells)
        }
    }
}

object RPASCal {
    private enum class Quadrant(val x: Int, val y: Int) {
        NE(1, 1),
        SE(1, -1),
        SW(-1, -1),
        NW(-1, 1)
    }

    private class CellAngles(var near: Float, val center: Float, var far: Float)

    val RADIUS_FUDGE: Float = 1.0F / 3.0F
    val NOT_VISIBLE_BLOCKS_VISION: Boolean = true
    val RESTRICTIVENESS: Int = 1
    val VISIBLE_ON_EQUAL: Boolean = true

    fun calcVisibleCellsFrom(center: AbsolutePosition, radius: Int,
                             isTransparent: (cell: AbsolutePosition) -> Boolean): Set<AbsolutePosition> {
        return mutableSetOf(center)
            .union(visibleCellsInQuadrantFrom(center, Quadrant.NE, radius, isTransparent))
            .union(visibleCellsInQuadrantFrom(center, Quadrant.SE, radius, isTransparent))
            .union(visibleCellsInQuadrantFrom(center, Quadrant.SW, radius, isTransparent))
            .union(visibleCellsInQuadrantFrom(center, Quadrant.NW, radius, isTransparent))
    }

    private fun visibleCellsInQuadrantFrom(center: AbsolutePosition, quadrant: Quadrant, radius: Int,
                                           isTransparent: (cell: AbsolutePosition) -> Boolean): Set<AbsolutePosition> {
        return visibleCellsInOctantFrom(center, quadrant, radius, isTransparent, true)
            .union(visibleCellsInOctantFrom(center, quadrant, radius, isTransparent, false))
    }

    private fun visibleCellsInOctantFrom(center: AbsolutePosition, quadrant: Quadrant, radius: Int,
                                         isTransparent: (cell: AbsolutePosition) -> Boolean, isVertical: Boolean
    ): Set<AbsolutePosition> {
        var iteration: Int = 1
        val visibleCells = mutableSetOf<AbsolutePosition>()
        var obstructions = mutableListOf<CellAngles>()

        while (iteration <= radius &&
            !(obstructions.size == 1 && obstructions[0].near == 0f && obstructions[0].far == 1f)) {
            val numCellsInRow: Int = iteration + 1
            val angleAllocation: Float = 1f / numCellsInRow.toFloat()

            // Python: for step in range(iteration + 1):
            for (step: Int in 0..iteration) {
                val cell: AbsolutePosition = cellAt(center, quadrant, step, iteration, isVertical)

                if (isCellInRadius(center, cell, radius)) {
                    val cellAngles = CellAngles(
                        near=step.toFloat() * angleAllocation,
                        center = (step.toFloat() + .5f) * angleAllocation,
                        far = (step.toFloat() + 1f) * angleAllocation)
                    if (cellIsVisible(cellAngles, obstructions)) {
                        visibleCells.add(cell)
                        if (!isTransparent(cell)) {
                            obstructions = addObstruction(obstructions, cellAngles)
                        }
                    } else if (NOT_VISIBLE_BLOCKS_VISION) {
                        obstructions = addObstruction(obstructions, cellAngles)
                    }
                }
            }

            iteration += 1
        }

        return visibleCells
    }

    private fun cellAt(
        center: AbsolutePosition,
        quadrant: Quadrant,
        step: Int,
        iteration: Int,
        isVertical: Boolean
    ): AbsolutePosition {
        return if (isVertical) {
            AbsolutePosition(center.x + step * quadrant.x, center.y + iteration * quadrant.y)
        } else {
            AbsolutePosition(center.x + iteration * quadrant.x, center.y + step * quadrant.y)
        }
    }

    private fun isCellInRadius(center: AbsolutePosition, cell: AbsolutePosition, radius: Int): Boolean {
        val distance: Float = sqrt(
            ((center.x - cell.x).toFloat() * (center.x - cell.x).toFloat()) +
            ((center.y - cell.y).toFloat() * (center.y - cell.y).toFloat())
        )
        return distance <= radius.toFloat() + RADIUS_FUDGE
    }

    private fun cellIsVisible(cellAngles: CellAngles, obstructions: List<CellAngles>): Boolean {
        var nearVisible = true
        var centerVisible = true
        var farVisible = true

        for (obstruction in obstructions) {
            if (VISIBLE_ON_EQUAL) {
                if (obstruction.near < cellAngles.near && cellAngles.near < obstruction.far)
                    nearVisible = false
                if (obstruction.near < cellAngles.center && cellAngles.center < obstruction.far)
                    centerVisible = false
                if (obstruction.near < cellAngles.far && cellAngles.far < obstruction.far)
                    farVisible = false
            } else {
                if (obstruction.near <= cellAngles.near && cellAngles.near <= obstruction.far)
                    nearVisible = false
                if (obstruction.near <= cellAngles.center && cellAngles.center <= obstruction.far)
                    centerVisible = false
                if (obstruction.near <= cellAngles.far && cellAngles.far <= obstruction.far)
                    farVisible = false
            }
        }

        if (RESTRICTIVENESS == 0) {
            return centerVisible || nearVisible || farVisible
        } else if (RESTRICTIVENESS == 1) {
            return (centerVisible && nearVisible) || (centerVisible && farVisible)
        } else {
            return centerVisible && nearVisible && farVisible
        }
    }

    private fun addObstruction(obstructions: List<CellAngles>, newObstruction: CellAngles): MutableList<CellAngles> {
        val newObject = CellAngles(newObstruction.near, newObstruction.center, newObstruction.far)
        val newList = obstructions.filter { !combineObstructions(it, newObject) }.toMutableList()
        newList.add(newObject)
        return newList
    }

    /**
     * Oh it's...it's actually mutating the obstructions. That's why there's this song and dance with copying the
     * objects. RIP past me I guess. And present me since I ain't rewriting this, got a 7DRL to prep for.
     */
    private fun combineObstructions(old: CellAngles, new: CellAngles): Boolean {
        lateinit var low: CellAngles
        lateinit var high: CellAngles
        if (old.near < new.near) {
            low = old
            high = new
        } else if (new.near < old.near) {
            low = new
            high = old
        } else {
            new.far = max(old.far, new.far)
            return true
        }

        if (low.far >= high.near) {
            new.near = min(low.near, high.near)
            new.far = max(low.far, high.far)
            return true
        }

        return false
    }
}