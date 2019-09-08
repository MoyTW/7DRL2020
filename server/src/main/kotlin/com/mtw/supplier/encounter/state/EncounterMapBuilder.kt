package com.mtw.supplier.encounter.state

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.utils.XYCoordinates

class Zone(
    val bottomLeft: XYCoordinates,
    val width: Int,
    val height: Int,
    val name: String
) {
    fun intersects(zone: Zone): Boolean {
        TODO()
    }

    fun center(): XYCoordinates {
        TODO()
    }

    fun randomUnblockedCoordinates(): XYCoordinates {
        TODO()
    }
}

class EncounterMapBuilder(
    val mapWidth: Int = 300,
    val mapHeight: Int = 300,
    val maxZoneGenAttempts: Int = 100,
    val maxZones: Int = 9,
    val zoneMinSize: Int = 20,
    val zoneMaxSize: Int = 40
) {
    fun placeObjects(zone: Zone) {
        TODO()
    }

    fun build() {
        // Generate the map with a border
        val encounterMap = EncounterMap(mapWidth, mapHeight)
        for (x in 0 until mapWidth) {
            for (y in 0 until mapHeight) {
                if (x == 0 || x == mapWidth - 1 || y == 0 || y == mapHeight - 1) {
                    encounterMap.markBlockStatus(XYCoordinates(x, y),true, true)
                }
            }
        }

        // Generate the zones
        val zones: MutableList<Zone> = mutableListOf()
        var zoneGenAttempts = 0
        while (zoneGenAttempts < maxZoneGenAttempts && zones.size < maxZones) {
            // Make the zone & place it
            val zoneWidth = (zoneMinSize..zoneMaxSize).random()
            val zoneHeight = (zoneMinSize..zoneMaxSize).random()
            val bottomLeft = XYCoordinates((0 until mapWidth - zoneWidth).random(),
                (0 until mapHeight - zoneHeight).random())
            val newZone = Zone(bottomLeft, zoneWidth, zoneHeight, "Zone ${zones.size}")

            // Compare the zones
            var failed = false
            for (otherZone in zones) {
                if (newZone.intersects(otherZone)) {
                    failed = true
                    break
                }
            }

            if (!failed) {
                TODO() // Figure out how to properly place this!
                //if (zones.isEmpty()) { encounterState.placeEntity(player, newZone.center()) }
                placeObjects(newZone)
                zones.add(newZone)
            }
            zoneGenAttempts += 1
        }

        // TODO: Generate the diplomat if you're on the last level

        // Generate the stairs
        val stairsZone = zones[(1 until zones.size).random()]
        val stairsPos = stairsZone.randomUnblockedCoordinates()
        TODO() // Generate stairs

        // Generate the intel
        TODO()
    }

}