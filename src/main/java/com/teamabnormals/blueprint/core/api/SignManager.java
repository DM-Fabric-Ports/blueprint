package com.teamabnormals.blueprint.core.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.HashSet;
import java.util.Set;

/**
 * Manager class for Blueprint's custom sign system.
 */
public final class SignManager {
	private static final Set<WoodType> WOOD_TYPES = new HashSet<>();

	@Environment(EnvType.CLIENT)
	public static void setupAtlas() {
		for (WoodType type : WOOD_TYPES)
			Sheets.SIGN_MATERIALS.put(type, new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/" + type.name())));
	}

	/**
	 * Registers a {@link WoodType} to the {@link #WOOD_TYPES} map.
     * <p>This method is safe to call during parallel mod loading.</p>
	 *
	 * @param type A {@link WoodType} to register.
	 * @return The registered {@link WoodType}.
	 */
	public static synchronized WoodType registerWoodType(WoodType type) {
		WOOD_TYPES.add(type);
		return WoodType.register(type);
	}
}
