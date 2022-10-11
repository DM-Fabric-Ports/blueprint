package com.teamabnormals.blueprint.core.util.registry;

import com.dm.earth.deferred_registries.DeferredRegistries;
import org.quiltmc.loader.api.QuiltLoader;

/**
 * An abstract implementation class of {@link ISubRegistryHelper}.
 * This contains a {@link RegistryHelper} parent and a {@link DeferredRegistries} to register objects.
 * <p> It is recommended you use this for making a new {@link ISubRegistryHelper}. </p>
 *
 * @param <T> The type of objects this helper registers.
 * @author SmellyModder (Luke Tonon)
 * @see ISubRegistryHelper
 */
public abstract class AbstractSubRegistryHelper<T> implements ISubRegistryHelper<T> {
	protected final RegistryHelper parent;
	protected final DeferredRegistries<T> deferredRegister;

	public AbstractSubRegistryHelper(RegistryHelper parent, DeferredRegistries<T> deferredRegister) {
		this.parent = parent;
		this.deferredRegister = deferredRegister;
	}

	/**
	 * @return The parent {@link RegistryHelper} this is a child of.
	 */
	@Override
	public RegistryHelper getParent() {
		return this.parent;
	}

	/**
	 * @return The {@link DeferredRegistries} belonging to this {@link AbstractSubRegistryHelper}.
	 */
	@Override
	public DeferredRegistries<T> getDeferredRegister() {
		return this.deferredRegister;
	}

	/**
	 * Registers this {@link AbstractSubRegistryHelper}.
	 */
	@Override
	public void register() {
		this.getDeferredRegister().register();
	}

	/**
	 * Determines whether a group of mods are loaded.
	 *
	 * @param modIds The mod ids of the mods to check.
	 * @return A boolean representing whether all the mods passed in are loaded.
	 */
	public static boolean areModsLoaded(String... modIds) {
		if ("true".equals(System.getProperty("blueprint.indev")))
			return true;
		for (String mod : modIds)
			if (!QuiltLoader.isModLoaded(mod))
				return false;
		return true;
	}
}
