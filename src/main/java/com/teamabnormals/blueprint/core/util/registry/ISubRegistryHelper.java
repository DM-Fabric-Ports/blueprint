package com.teamabnormals.blueprint.core.util.registry;

import com.dm.earth.deferred_registries.DeferredRegistries;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;

/**
 * An interface for 'sub' registry helpers used in {@link RegistryHelper}.
 *
 * @param <T> The type of objects this helper registers.
 * @author SmellyModder (Luke Tonon)
 */
public interface ISubRegistryHelper<T> {
	/**
	 * @return The {@link RegistryHelper} this is a child of.
	 */
	RegistryHelper getParent();

	/**
	 * @return The {@link DeferredRegistries} for registering.
	 */
	LazyRegistrar<T> getDeferredRegister();

	/**
	 * Should ideally register {@link #getDeferredRegister()}.
	 */
	void register();
}
