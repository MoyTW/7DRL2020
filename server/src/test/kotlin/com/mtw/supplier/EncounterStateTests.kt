package com.mtw.supplier

import com.mtw.supplier.ecs.Entity
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.ecs.components.ai.AIComponent
import com.mtw.supplier.encounter.state.EncounterState
import com.mtw.supplier.encounter.EncounterRunner
import com.mtw.supplier.utils.XYCoordinates
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.springframework.boot.test.context.SpringBootTest

@RunWith(JUnit4::class)
@SpringBootTest
class EncounterStateTests {

    @Test
    fun doesStuff() {
        val fighterOne = Entity(1, "wolf")
            .addComponent(AIComponent())
            .addComponent(HpComponent(20, 20))
            .addComponent(FighterComponent(5, 5, 5))
            .addComponent(FactionComponent(0))
            .addComponent(CollisionComponent(true))
            .addComponent(ActionTimeComponent(5))
            .addComponent(SpeedComponent(5))
        val fighterTwo = Entity(2, "strongMercenary")
            .addComponent(AIComponent())
            .addComponent(HpComponent(50, 50))
            .addComponent(FighterComponent(5, 100, 100))
            .addComponent(FactionComponent(2))
            .addComponent(CollisionComponent(true))
            .addComponent(ActionTimeComponent(30))
            .addComponent(SpeedComponent(30))

        val encounterState = EncounterState(5, 1)
            .placeEntity(fighterOne, XYCoordinates(0, 0))
            .placeEntity(fighterTwo, XYCoordinates(4, 0))
        EncounterRunner.runEncounter(encounterState)
    }
}
