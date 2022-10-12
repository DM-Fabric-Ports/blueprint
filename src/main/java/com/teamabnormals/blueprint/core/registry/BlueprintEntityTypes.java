package com.teamabnormals.blueprint.core.registry;

import com.dm.earth.deferred_registries.DeferredObject;
import com.teamabnormals.blueprint.common.entity.BlueprintBoat;
import com.teamabnormals.blueprint.common.entity.BlueprintChestBoat;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * Registry class for the built-in {@link EntityType}s.
 */
public final class BlueprintEntityTypes {
	private static final EntitySubRegistryHelper HELPER = Blueprint.REGISTRY_HELPER.getEntitySubHelper();

	public static final DeferredObject<EntityType<BlueprintBoat>> BOAT = HELPER.createEntity("boat", BlueprintBoat::new, MobCategory.MISC, 1.375F, 0.5625F);
	public static final DeferredObject<EntityType<BlueprintChestBoat>> CHEST_BOAT = HELPER.createEntity("chest_boat", BlueprintChestBoat::new, BlueprintChestBoat::new, MobCategory.MISC, 1.375F, 0.5625F);
}
