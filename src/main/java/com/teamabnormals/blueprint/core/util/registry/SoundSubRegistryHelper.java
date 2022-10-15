package com.teamabnormals.blueprint.core.util.registry;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;

/**
 * A basic {@link AbstractSubRegistryHelper} for sounds. This contains some useful registering methods for sounds.
 *
 * @author SmellyModder (Luke Tonon)
 * @see AbstractSubRegistryHelper
 */
public class SoundSubRegistryHelper extends AbstractSubRegistryHelper<SoundEvent> {

	public SoundSubRegistryHelper(RegistryHelper parent, LazyRegistrar<SoundEvent> deferredRegister) {
		super(parent, deferredRegister);
	}

	public SoundSubRegistryHelper(RegistryHelper parent) {
		super(parent, LazyRegistrar.create(Registry.SOUND_EVENT, parent.getModId()));
	}

	/**
	 * Creates and registers a {@link SoundEvent}.
	 *
	 * @param name The sound's name.
	 * @return A {@link RegistryObject} containing the created {@link SoundEvent}.
	 */
	public RegistryObject<SoundEvent> createSoundEvent(String name) {
		return this.deferredRegister.register(name, () -> new SoundEvent(this.parent.prefix(name)));
	}

}
