package com.mtw.supplier.encounter.state

import com.mtw.supplier.utils.XYCoordinates
import java.util.*
import kotlin.Comparator
import kotlin.math.abs
import kotlin.math.sqrt

object EncounterStateUtils {
    fun distanceBetween(pos1: XYCoordinates, pos2: XYCoordinates): Float {
        val dx = (pos1.x - pos2.x).toFloat()
        val dy = (pos1.y - pos2.y).toFloat()
        return sqrt(dx * dx + dy * dy)
    }

    private val aStarComparator = Comparator<Pair<XYCoordinates, Double>> { o1, o2 -> o1!!.second.compareTo(o2!!.second) }

    private fun aStarHeuristic(startPos: XYCoordinates, endPos: XYCoordinates): Double {
        return abs(startPos.x.toDouble() - endPos.x.toDouble()) +
            abs(startPos.y.toDouble() - endPos.y.toDouble())
    }

    fun aStarWithNewGrid(startPos: XYCoordinates,
                         endPos: XYCoordinates,
                         encounterState: EncounterState): List<XYCoordinates>? {
        val frontier = PriorityQueue<Pair<XYCoordinates, Double>>(aStarComparator)
        frontier.add(Pair(startPos, 0.0))

        val cameFrom: MutableMap<XYCoordinates, XYCoordinates> = mutableMapOf()

        val costSoFar: MutableMap<XYCoordinates, Double> = mutableMapOf()
        costSoFar[startPos] = 0.0

        while (frontier.isNotEmpty()) {
            val currentPos = frontier.poll().first

            if (encounterState.arePositionsAdjacent(currentPos, endPos)) {
                val path = mutableListOf(endPos, currentPos)
                while (cameFrom.containsKey(path.last())) {
                    path.add(cameFrom[path.last()]!!)
                }
                path.remove(startPos)
                return path.reversed()
            }

            for (nextPos in encounterState.adjacentUnblockedPositions(currentPos)) {
                val newNextPosCost = costSoFar[currentPos]!!.plus(1.0) // Fixed cost of 1
                if (!costSoFar.containsKey(nextPos) || newNextPosCost < costSoFar[nextPos]!!) {
                    costSoFar[nextPos] = newNextPosCost
                    val priority = newNextPosCost + aStarHeuristic(nextPos, endPos)
                    frontier.add(Pair(nextPos, priority))
                    cameFrom[nextPos] = currentPos
                }
            }
        }
        return null
    }
}