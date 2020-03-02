package com.mtw.supplier.encounter.state

import com.mtw.supplier.utils.AbsolutePosition
import java.util.*
import kotlin.Comparator
import kotlin.math.abs
import kotlin.math.sqrt

object EncounterStateUtils {
    fun distanceBetween(pos1: AbsolutePosition, pos2: AbsolutePosition): Float {
        val dx = (pos1.x - pos2.x).toFloat()
        val dy = (pos1.y - pos2.y).toFloat()
        return sqrt(dx * dx + dy * dy)
    }

    private val aStarComparator = Comparator<Pair<AbsolutePosition, Double>> { o1, o2 -> o1!!.second.compareTo(o2!!.second) }

    private fun aStarHeuristic(startPos: AbsolutePosition, endPos: AbsolutePosition): Double {
        return abs(startPos.x.toDouble() - endPos.x.toDouble()) +
            abs(startPos.y.toDouble() - endPos.y.toDouble())
    }

    fun aStarWithNewGrid(startPos: AbsolutePosition,
                         endPos: AbsolutePosition,
                         encounterState: EncounterState): List<AbsolutePosition>? {
        val frontier = PriorityQueue<Pair<AbsolutePosition, Double>>(aStarComparator)
        frontier.add(Pair(startPos, 0.0))

        val cameFrom: MutableMap<AbsolutePosition, AbsolutePosition> = mutableMapOf()

        val costSoFar: MutableMap<AbsolutePosition, Double> = mutableMapOf()
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