package com.mtw.supplier.ecs.defs

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.ecs.components.ai.AIComponent
import com.mtw.supplier.ecs.components.HpComponent
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner

@Serializable
class TestComponentNoArgs: Component() {
    override var _parentId: Int? = null
}

@Serializable
class TestComponentTwoIntArgs(var argOne: Int, var argTwo: Int): Component() {
    override var _parentId: Int? = null
}

@RunWith(SpringRunner::class)
class EntityDefTests {

    private val testComponentModule = SerializersModule {
        polymorphic(Generator::class) {
            FixedIntegerGenerator::class with FixedIntegerGenerator.serializer()
        }
    }

    //<editor-fold desc="Serialization>
    @Test
    fun testSerializationToJson() {
        val json = Json(JsonConfiguration.Stable, testComponentModule)
        val hpDef = ComponentDef(HpComponent::class.qualifiedName!!, arrayOf(FixedIntegerGenerator(3), FixedIntegerGenerator(9)))
        val aiDef = ComponentDef(AIComponent::class.qualifiedName!!, arrayOf())
        val entityDef = EntityDef(listOf(hpDef, aiDef))

        // serializing objects
        val jsonData = json.stringify(EntityDef.serializer(), entityDef)
        Assert.assertEquals("{\"componentDefs\":[{\"qualifiedClassName\":\"com.mtw.supplier.ecs.components.HpComponent\",\"generators\":[{\"type\":\"com.mtw.supplier.ecs.defs.FixedIntegerGenerator\",\"value\":3},{\"type\":\"com.mtw.supplier.ecs.defs.FixedIntegerGenerator\",\"value\":9}]},{\"qualifiedClassName\":\"com.mtw.supplier.ecs.components.ai.AIComponent\",\"generators\":[]}]}", jsonData)
    }

    @Test
    fun testSerializationFromJson() {
        val jsonString = "{\"componentDefs\":[{\"qualifiedClassName\":\"com.mtw.supplier.ecs.components.HpComponent\",\"generators\":[{\"type\":\"com.mtw.supplier.ecs.defs.FixedIntegerGenerator\",\"value\":3},{\"type\":\"com.mtw.supplier.ecs.defs.FixedIntegerGenerator\",\"value\":9}]},{\"qualifiedClassName\":\"com.mtw.supplier.ecs.components.ai.AIComponent\",\"generators\":[]}]}"
        val json = Json(JsonConfiguration.Stable, testComponentModule)
        val entityDef: EntityDef = json.parse(EntityDef.serializer(), jsonString)

        val componentDefs = entityDef.componentDefs

        Assert.assertEquals(HpComponent::class.qualifiedName!!, componentDefs[0].qualifiedClassName)
        Assert.assertEquals(2, componentDefs[0].generators.size)

        Assert.assertEquals(FixedIntegerGenerator::class, componentDefs[0].generators[0]::class)
        Assert.assertEquals(3, (componentDefs[0].generators[0] as FixedIntegerGenerator).value)

        Assert.assertEquals(FixedIntegerGenerator::class, componentDefs[0].generators[1]::class)
        Assert.assertEquals(9, (componentDefs[0].generators[1] as FixedIntegerGenerator).value)

        Assert.assertEquals(AIComponent::class.qualifiedName!!, componentDefs[1].qualifiedClassName)
        Assert.assertTrue(componentDefs[1].generators.isEmpty())
    }
    //</editor-fold>

    @Test
    fun testCanGenerate() {
        val noArgs = ComponentDef(TestComponentNoArgs::class.qualifiedName!!, arrayOf())
        val twoArgs = ComponentDef(TestComponentTwoIntArgs::class.qualifiedName!!, arrayOf(FixedIntegerGenerator(5), FixedIntegerGenerator(2)))
        val entityDef = EntityDef(listOf(noArgs, twoArgs))
        val testEntity = entityDef.buildEntity(1, "testEntity")

        Assert.assertEquals(testEntity.id, testEntity.getComponent(TestComponentNoArgs::class).parentId)
        Assert.assertEquals(testEntity.id, testEntity.getComponent(TestComponentTwoIntArgs::class).parentId)
        Assert.assertEquals(5, testEntity.getComponent(TestComponentTwoIntArgs::class).argOne)
        Assert.assertEquals(2, testEntity.getComponent(TestComponentTwoIntArgs::class).argTwo)
    }

}
