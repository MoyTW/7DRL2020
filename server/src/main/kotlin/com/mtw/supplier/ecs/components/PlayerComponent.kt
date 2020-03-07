package com.mtw.supplier.ecs.components

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.encounter.EncounterRunner
import com.mtw.supplier.encounter.rulebook.Action
import com.mtw.supplier.encounter.rulebook.actions.TerrifyAction
import com.mtw.supplier.encounter.rulebook.actions.TerrorChangeStats
import com.mtw.supplier.encounter.state.EncounterState
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import kotlin.math.max
import kotlin.math.min

@Serializable
abstract class Memory(
    val name: String
) {
    abstract fun remember(encounterState: EncounterState): List<Action>
}

class TerrorChangeMemory(
    name: String,
    val terrorChangeStats: TerrorChangeStats
) : Memory(name) {
    override fun remember(encounterState: EncounterState): List<Action> {
        val player = encounterState.playerEntity()
        return listOf(TerrifyAction(player, player, terrorChangeStats))
    }

}

@Serializable
class PlayerComponent(
    var targeted: Entity? = null
): Component() {
    private val logger = LoggerFactory.getLogger(PlayerComponent::class.java)

    override var _parentId: String? = null

    private val seenMemories: MutableSet<String> = mutableSetOf()
    private val memories: MutableList<Memory> = mutableListOf()

    val maxMemories: Int = 12

    fun seenMemory(memory: Memory): Boolean {
        return this.seenMemories.contains(memory.name)
    }

    fun addMemory(memory: Memory) {
        if (seenMemories.contains(memory.name)) {
            logger.error("can't double add memory you missed your seen check")
            return
        }

        if (memories.size > maxMemories) {
            memories.remove(memories.random())
        }
        memories.add(memory)
    }

    fun getMemories(): List<Memory> {
        return memories
    }

    fun removeMemory(memory: Memory) {
        memories.remove(memory)
    }
}