package com.teamabnormals.blueprint.core.util.registry;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import org.quiltmc.loader.api.QuiltLoader;

/**
 * An abstract implementation class of {@link ISubRegistryHelper}.
 * This contains a {@link RegistryHelper} parent and a {@link LazyRegistrar} to register objects.
 * <p> It is recommended you use this for making a new {@link ISubRegistryHelper}. </p>
 *
 * @param <T> The type of objects this helper registers.
 * @author SmellyModder (Luke Tonon)
 * @see ISubRegistryHelper
 */
public abstract class AbstractSubRegistryHelper<T> implements ISubRegistryHelper<T> {
	protected final RegistryHelper parent;
	protected final LazyRegistrar<T> deferredRegister;

	public AbstractSubRegistryHelper(RegistryHelper parent, LazyRegistrar<T> deferredRegister) {
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
	 * @return The {@link LazyRegistrar} belonging to this {@link AbstractSubRegistryHelper}.
	 */
	@Override
	public LazyRegistrar<T> getDeferredRegister() {
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
