package com.mtw.supplier.ecs

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
class Entity(
    val id: Int,
    val name: String
) {
    private val components: MutableList<Component> = mutableListOf()

    fun addComponent(component: Component): Entity {
        this.components.add(component)
        component.notifyAdded(this.id)
        return this
    }

    fun removeComponent(component: Component) {
        if (component !in components) {
            throw ComponentNotFoundException("Could not find component of type ${component::class} in entity [$name,$id]")
        }
        this.components.remove(component)
        component.notifyRemoved()
    }

    fun <T: Component> removeComponent(clazz: KClass<T>) {
        this.removeComponent(this.getComponent(clazz))
    }

    fun hasComponent(componentClass: KClass<*>): Boolean {
        return components.any { componentClass.isInstance(it) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Component> getComponent(clazz: KClass<T>): T {
        val first = components.firstOrNull { clazz.isInstance(it) }
            ?: throw ComponentNotFoundException("Could not find component of type $clazz in entity [$name,$id]")
        return first as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Component> getComponentOrNull(clazz: KClass<T>): T? {
        return components.firstOrNull { clazz.isInstance(it) } as T?
    }

    class ComponentNotFoundException(message: String): Exception(message)
}