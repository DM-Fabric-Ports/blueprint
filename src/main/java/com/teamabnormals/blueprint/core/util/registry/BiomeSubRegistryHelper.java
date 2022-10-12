package com.teamabnormals.blueprint.core.util.registry;

import com.dm.earth.deferred_registries.DeferredRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

/**
 * A {@link AbstractSubRegistryHelper} extension for biomes. This contains some useful registering methods for biomes.
 *
 * @author SmellyModder (Luke Tonon)
 * @see AbstractSubRegistryHelper
 * @see KeyedBiome
 */
public class BiomeSubRegistryHelper extends AbstractSubRegistryHelper<Biome> {

	public BiomeSubRegistryHelper(RegistryHelper parent, DeferredRegistries<Biome> deferredRegister) {
		super(parent, deferredRegister);
	}

	public BiomeSubRegistryHelper(RegistryHelper parent) {
		super(parent, DeferredRegistries.create(ForgeRegistries.BIOMES, parent.modId));
	}

	/**
	 * Registers a {@link Biome} and wraps it around a {@link KeyedBiome}.
	 *
	 * @param name  The name for the {@link Biome}.
	 * @param biome A {@link Biome} to register.
	 * @return A {@link KeyedBiome} wrapped around the registered {@link Biome}.
	 * @see KeyedBiome
	 */
	public KeyedBiome createBiome(String name, Supplier<Biome> biome) {
		return new KeyedBiome(this.deferredRegister.register(name, biome));
	}

	/**
	 * A wrapper around a {@link Biome} {@link RegistryObject} for storing a biome's {@link ResourceKey}.
	 * <p>This allows for a biome's {@link ResourceKey} to be cached.</p>
	 *
	 * @author SmellyModder (Luke Tonon)
	 */
	@SuppressWarnings("deprecation")
	public static final class KeyedBiome {
		private static final ForgeRegistry<Biome> BIOME_REGISTRY = (ForgeRegistry<Biome>) ForgeRegistries.BIOMES;
		private final RegistryObject<Biome> biome;
		private final LazyLoadedValue<ResourceKey<Biome>> lazyKey;

		public KeyedBiome(RegistryObject<Biome> biome) {
			this.biome = biome;
			this.lazyKey = new LazyLoadedValue<>(() -> BIOME_REGISTRY.getKey(BIOME_REGISTRY.getID(this.biome.get())));
		}

		/**
		 * Performs the same function as {@link RegistryObject#get()}.
		 * <p>This value will automatically be updated when the backing biome registry is updated.</p>
		 *
		 * @return The {@link Biome} stored inside the {@link #biome} {@link RegistryObject}.
		 * @see RegistryObject#get()
		 */
		public Biome get() {
			return this.biome.get();
		}

		/**
		 * Gets the {@link #biome} {@link RegistryObject}.
		 *
		 * @return The {@link #biome} {@link RegistryObject}.
		 */
		public RegistryObject<Biome> getObject() {
			return this.biome;
		}

		/**
		 * Gets the {@link ResourceKey} of the biome stored in the {@link #biome} {@link RegistryObject}.
		 * <p>Only call this if {@link RegistryObject#isPresent()} returns true.</p>
		 *
		 * @return The {@link ResourceKey} of the biome stored in the {@link #biome} {@link RegistryObject}.
		 * @see RegistryObject#get()
		 */
		public ResourceKey<Biome> getKey() {
			return this.lazyKey.get();
		}
	}

}
