package com.teamabnormals.blueprint.core.util.registry;

import com.dm.earth.deferred_registries.DeferredObject;
import com.dm.earth.deferred_registries.DeferredRegistries;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

/**
 * A basic {@link AbstractSubRegistryHelper} for entities.
 * <p>This contains some useful registering methods for entities.</p>
 *
 * @author SmellyModder (Luke Tonon)
 * @see AbstractSubRegistryHelper
 */
public class EntitySubRegistryHelper extends AbstractSubRegistryHelper<EntityType<?>> {

	public EntitySubRegistryHelper(RegistryHelper parent, DeferredRegistries<EntityType<?>> deferredRegister) {
		super(parent, deferredRegister);
	}

	public EntitySubRegistryHelper(RegistryHelper parent) {
		super(parent, DeferredRegistries.create(Registry.ENTITY_TYPE, parent.getModId()));
	}

	/**
	 * Creates and registers an {@link EntityType} with the type of {@link LivingEntity}.
	 *
	 * @param name                 The entity's name.
	 * @param factory              The entity's factory.
	 * @param entityClassification The entity's classification.
	 * @param width                The width of the entity's bounding box.
	 * @param height               The height of the entity's bounding box.
	 * @return A {@link DeferredObject} containing the created {@link EntityType}.
	 */
	public DeferredObject<EntityType<?>> createLivingEntity(String name, EntityType.EntityFactory<? extends LivingEntity> factory, MobCategory entityClassification, float width, float height) {
		return this.deferredRegister.register(name, createLivingEntity(factory, entityClassification, name, width, height));
	}

	/**
	 * Creates and registers an {@link EntityType} with the type of {@link Entity}.
	 *
	 * @param name                 The entity's name.
	 * @param factory              The entity's factory.
	 * @param entityClassification The entity's classification.
	 * @param width                The width of the entity's bounding box.
	 * @param height               The height of the entity's bounding box.
	 * @return A {@link DeferredObject} containing the created {@link EntityType}.
	 */
	public DeferredObject<EntityType<?>> createEntity(String name, EntityType.EntityFactory<? extends LivingEntity> factory, MobCategory entityClassification, float width, float height) {
		return this.deferredRegister.register(name, createEntity(factory, entityClassification, name, width, height));
	}

	/**
	 * Creates an {@link EntityType} with the type of {@link LivingEntity}.
	 *
	 * @param name                 The entity's name.
	 * @param factory              The entity's factory.
	 * @param entityClassification The entity's classification.
	 * @param width                The width of the entity's bounding box.
	 * @param height               The height of the entity's bounding box.
	 * @return The created {@link EntityType}.
	 */
	public EntityType<? extends LivingEntity> createLivingEntity(EntityType.EntityFactory<? extends LivingEntity> factory, MobCategory entityClassification, String name, float width, float height) {
		ResourceLocation location = this.parent.prefix(name);
		return FabricEntityTypeBuilder.createLiving()
				.trackRangeBlocks(64)
				.dimensions(EntityDimensions.fixed(width, height))
				.forceTrackedVelocityUpdates(true)
				.trackedUpdateRate(3)
				.build();
	}

	/**
	 * Creates an {@link EntityType} with the type of {@link Entity}.
	 *
	 * @param name                 The entity's name.
	 * @param factory              The entity's factory.
	 * @param entityClassification The entity's classification.
	 * @param width                The width of the entity's bounding box.
	 * @param height               The height of the entity's bounding box.
	 * @return The created {@link EntityType}.
	 */
	public EntityType<? extends Entity> createEntity(EntityType.EntityFactory<? extends Entity> factory, MobCategory entityClassification, String name, float width, float height) {
		ResourceLocation location = this.parent.prefix(name);
		return FabricEntityTypeBuilder.create(entityClassification)
				.entityFactory(factory)
				.dimensions(EntityDimensions.fixed(width, height))
				.forceTrackedVelocityUpdates(true)
				.trackRangeBlocks(64)
				.trackedUpdateRate(3)
				.build();
	}

}
