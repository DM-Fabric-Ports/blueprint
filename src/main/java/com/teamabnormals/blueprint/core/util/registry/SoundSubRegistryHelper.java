package com.teamabnormals.blueprint.core.util.registry;

import com.dm.earth.deferred_registries.DeferredObject;
import com.dm.earth.deferred_registries.DeferredRegistries;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;

/**
 * A basic {@link AbstractSubRegistryHelper} for sounds. This contains some useful registering methods for sounds.
 *
 * @author SmellyModder (Luke Tonon)
 * @see AbstractSubRegistryHelper
 */
public class SoundSubRegistryHelper extends AbstractSubRegistryHelper<SoundEvent> {

	public SoundSubRegistryHelper(RegistryHelper parent, DeferredRegistries<SoundEvent> deferredRegister) {
		super(parent, deferredRegister);
	}

	public SoundSubRegistryHelper(RegistryHelper parent) {
		super(parent, DeferredRegistries.create(Registry.SOUND_EVENT, parent.getModId()));
	}

	/**
	 * Creates and registers a {@link SoundEvent}.
	 *
	 * @param name The sound's name.
	 * @return A {@link RegistryObject} containing the created {@link SoundEvent}.
	 */
	public DeferredObject<SoundEvent> createSoundEvent(String name) {
		return this.deferredRegister.register(name, () -> new SoundEvent(this.parent.prefix(name)));
	}

}
