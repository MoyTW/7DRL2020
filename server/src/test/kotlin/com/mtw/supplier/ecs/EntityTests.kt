package com.mtw.supplier.ecs

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class EntityTests {
    @Serializable
    class TestComponent(val x1: String, val x2: Int): Component() {
        override var _parentId: Int? = null
    }

    val testComponentModule = SerializersModule {
        polymorphic(Component::class) {
            TestComponent::class with TestComponent.serializer()
        }
    }

    @Test
    fun testSerializationToJson() {
        val json = Json(JsonConfiguration.Stable, testComponentModule)
        val entity = Entity(95, "bob").addComponent(TestComponent("test", 99))
        val jsonData = json.stringify(Entity.serializer(), entity)
        Assert.assertEquals("{\"id\":95,\"name\":\"bob\",\"components\":[{\"type\":\"com.mtw.supplier.ecs.EntityTests.TestComponent\",\"x1\":\"test\",\"x2\":99,\"_parentId\":95}]}", jsonData)
    }

    @Test
    fun testSerializationFromJson() {
        val jsonString = "{\"id\":95,\"name\":\"bob\",\"components\":[{\"type\":\"com.mtw.supplier.ecs.EntityTests.TestComponent\",\"_parentId\":95,\"x1\":\"test\",\"x2\":99}]}"
        val json = Json(JsonConfiguration.Stable, testComponentModule)
        val entity: Entity = json.parse(Entity.serializer(), jsonString)
        Assert.assertEquals(95, entity.id)
        Assert.assertEquals("bob", entity.name)
        val component = entity.getComponent(TestComponent::class)
        Assert.assertEquals(95, component.parentId)
        Assert.assertEquals("test", component.x1)
        Assert.assertEquals(99, component.x2)
    }
}
