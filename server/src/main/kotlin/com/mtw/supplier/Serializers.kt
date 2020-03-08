package com.mtw.supplier

import com.mtw.supplier.ecs.Component
import com.mtw.supplier.ecs.components.*
import com.mtw.supplier.ecs.components.ai.*
import com.mtw.supplier.ecs.components.item.CarryableComponent
import com.mtw.supplier.ecs.components.item.InventoryComponent
import com.mtw.supplier.ecs.components.item.UsableComponent
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

object Serializers {
    fun componentSerializersModuleBuilder(): SerialModule {
        return SerializersModule {
            polymorphic(Component::class) {
                // ai
                AIComponent::class with AIComponent.serializer()
                FamiliarFigureAIComponent::class with FamiliarFigureAIComponent.serializer()
                HospitalErReceptionistAIComponent::class with HospitalErReceptionistAIComponent.serializer()
                PathAIComponent::class with PathAIComponent.serializer()

                // item
                CarryableComponent::class with CarryableComponent.serializer()
                InventoryComponent::class with InventoryComponent.serializer()
                UsableComponent::class with UsableComponent.serializer()

                // other
                ActionTimeComponent::class with ActionTimeComponent.serializer()
                CollisionComponent::class with CollisionComponent.serializer()
                RoomPositionComponent::class with RoomPositionComponent.serializer()
                FactionComponent::class with FactionComponent.serializer()
                HpComponent::class with HpComponent.serializer()
                FighterComponent::class with FighterComponent.serializer()
                PlayerComponent::class with PlayerComponent.serializer()
                SpeedComponent::class with SpeedComponent.serializer()
            }
        }
    }
}