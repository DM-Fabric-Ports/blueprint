package com.teamabnormals.blueprint.core;

import com.teamabnormals.blueprint.client.BlueprintShaders;
import com.teamabnormals.blueprint.client.RewardHandler;
import com.teamabnormals.blueprint.client.renderer.BlueprintBoatRenderer;
import com.teamabnormals.blueprint.client.renderer.block.BlueprintChestBlockEntityRenderer;
import com.teamabnormals.blueprint.client.screen.splash.BlueprintSplashManager;
import com.teamabnormals.blueprint.common.capability.chunkloading.ChunkLoaderCapability;
import com.teamabnormals.blueprint.common.capability.chunkloading.ChunkLoaderEvents;
import com.teamabnormals.blueprint.common.network.MessageC2SUpdateSlabfishHat;
import com.teamabnormals.blueprint.common.network.entity.MessageS2CEndimation;
import com.teamabnormals.blueprint.common.network.entity.MessageS2CTeleportEntity;
import com.teamabnormals.blueprint.common.network.entity.MessageS2CUpdateEntityData;
import com.teamabnormals.blueprint.common.network.particle.MessageS2CSpawnParticle;
import com.teamabnormals.blueprint.common.world.modification.ModdedBiomeSource;
import com.teamabnormals.blueprint.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.api.SignManager;
import com.teamabnormals.blueprint.core.api.conditions.BlueprintAndCondition;
import com.teamabnormals.blueprint.core.api.conditions.config.*;
import com.teamabnormals.blueprint.core.api.model.FullbrightModel;
import com.teamabnormals.blueprint.core.data.server.modifiers.BlueprintModdedBiomeSliceProvider;
import com.teamabnormals.blueprint.core.data.server.tags.*;
import com.teamabnormals.blueprint.core.endimator.EndimationLoader;
import com.teamabnormals.blueprint.core.other.BlueprintEvents;
import com.teamabnormals.blueprint.core.registry.*;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import io.github.fabricators_of_create.porting_lib.crafting.CraftingHelper;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

/**
 * Mod class for the Blueprint mod.
 *
 * @author SmellyModder (Luke Tonon)
 * @author bageldotjpg
 * @author Jackson
 * @author abigailfails
 */
public final class Blueprint implements ModInitializer, ClientModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "blueprint";
	public static final String NETWORK_PROTOCOL = "BP1";
	public static final EndimationLoader ENDIMATION_LOADER = new EndimationLoader();
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
	public static final TrackedData<Byte> SLABFISH_SETTINGS = TrackedData.Builder.create(DataProcessors.BYTE, () -> (byte) 8).enablePersistence().build();

	public static final SimpleChannel CHANNEL = new SimpleChannel(new ResourceLocation(MOD_ID, "net"));

	static {
		ResourceConditions.register(BlueprintAndCondition.LOCATION, BlueprintAndCondition.Serializer::read);
	}

	@Override
	public void onInitialize(ModContainer mod) {
//		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
//		MinecraftForge.EVENT_BUS.register(this);
//		MinecraftForge.EVENT_BUS.register(new ChunkLoaderEvents());

		this.registerMessages();

		DataUtil.registerConfigPredicate(new EqualsPredicate.Serializer());
		DataUtil.registerConfigPredicate(new GreaterThanOrEqualPredicate.Serializer());
		DataUtil.registerConfigPredicate(new GreaterThanPredicate.Serializer());
		DataUtil.registerConfigPredicate(new LessThanOrEqualPredicate.Serializer());
		DataUtil.registerConfigPredicate(new LessThanPredicate.Serializer());
		DataUtil.registerConfigPredicate(new ContainsPredicate.Serializer());
		DataUtil.registerConfigPredicate(new MatchesPredicate.Serializer());

		REGISTRY_HELPER.getEntitySubHelper().register();
		REGISTRY_HELPER.getBlockEntitySubHelper().register();
		REGISTRY_HELPER.getBiomeSubHelper().register();
		BlueprintPoiTypes.POI_TYPES.register();
		BlueprintSurfaceRules.CONDITIONS.register();
		BlueprintLootConditions.LOOT_CONDITION_TYPES.register();

//		bus.addListener((ModConfigEvent event) -> {
//			final ModConfig config = event.getConfig();
//			if (config.getSpec() == BlueprintConfig.CLIENT_SPEC) {
//				BlueprintConfig.CLIENT.load();
//			}
//		});

		registerOnEvent();
		bus.addListener(EventPriority.LOWEST, this::commonSetup);
		bus.addListener(EventPriority.LOWEST, this::postLoadingSetup);
		bus.addListener(this::dataSetup);
		bus.addListener(this::registerCapabilities);
		context.registerConfig(ModConfig.Type.CLIENT, BlueprintConfig.CLIENT_SPEC);
		context.registerConfig(ModConfig.Type.COMMON, BlueprintConfig.COMMON_SPEC);
	}

	@Override
	public void onInitializeClient(ModContainer mod) {
		bus.addListener(EventPriority.NORMAL, false, RegisterColorHandlersEvent.Block.class, event -> {
			ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
			if (resourceManager instanceof ReloadableResourceManager) {
				((ReloadableResourceManager) resourceManager).registerReloadListener(ENDIMATION_LOADER);
			}
		});
		bus.addListener(EventPriority.NORMAL, false, ModConfigEvent.Reloading.class, event -> {
			if (event.getConfig().getModId().equals(Blueprint.MOD_ID))
				NetworkUtil.updateSlabfish(RewardHandler.SlabfishSetting.getConfig());
		});
		bus.addListener(this::clientSetup);
		bus.addListener(this::modelSetup);
		bus.addListener(this::rendererSetup);
		bus.addListener(BlueprintSplashManager::onRegisterClientReloadListeners);
		bus.addListener(RewardHandler::clientSetup);
		bus.addListener(RewardHandler::addLayers);
		bus.addListener(BlueprintShaders::registerShaders);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(MOD_ID, "slabfish_head"), SLABFISH_SETTINGS);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(SignManager::setupAtlas);
	}

	private void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

		boolean includeServer = event.includeServer();
		BlueprintBlockTagsProvider blockTags = new BlueprintBlockTagsProvider(MOD_ID, generator, fileHelper);
		generator.addProvider(includeServer, blockTags);
		generator.addProvider(includeServer, new BlueprintItemTagsProvider(MOD_ID, generator, blockTags, fileHelper));
		generator.addProvider(includeServer, new BlueprintEntityTypeTagsProvider(MOD_ID, generator, fileHelper));
		generator.addProvider(includeServer, new BlueprintBiomeTagsProvider(MOD_ID, generator, fileHelper));
		generator.addProvider(includeServer, new BlueprintPoiTypeTagsProvider(generator, fileHelper));
		generator.addProvider(includeServer, new BlueprintModdedBiomeSliceProvider(generator));
	}

	private static void registerOnEvent() {
		Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(MOD_ID, "modded"), ModdedBiomeSource.CODEC);
	}

	private void rendererSetup(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(BlueprintEntityTypes.BOAT.get(), BlueprintBoatRenderer::simple);
		event.registerEntityRenderer(BlueprintEntityTypes.CHEST_BOAT.get(), BlueprintBoatRenderer::chest);

		event.registerBlockEntityRenderer(BlueprintBlockEntityTypes.CHEST.get(), BlueprintChestBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BlueprintBlockEntityTypes.TRAPPED_CHEST.get(), BlueprintChestBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BlueprintBlockEntityTypes.SIGN.get(), SignRenderer::new);
	}

	private void postLoadingSetup(FMLLoadCompleteEvent event) {
		event.enqueueWork(() -> {
			DataUtil.getSortedAlternativeDispenseBehaviors().forEach(DataUtil.AlternativeDispenseBehavior::register);
			BlueprintEvents.SORTED_CUSTOM_NOTE_BLOCK_INSTRUMENTS = DataUtil.getSortedCustomNoteBlockInstruments();
		});
	}

	private void registerCapabilities(RegisterCapabilitiesEvent event) {
		ChunkLoaderCapability.register(event);
	}

	private void modelSetup(RegisterGeometryLoaders event) {
		event.register("fullbright", FullbrightModel.Loader.INSTANCE);
	}

	private void registerMessages() {
		int id = -1;
		CHANNEL.registerMessage(++id, MessageS2CEndimation.class, MessageS2CEndimation::serialize, MessageS2CEndimation::deserialize, MessageS2CEndimation::handle);
		CHANNEL.registerMessage(++id, MessageS2CTeleportEntity.class, MessageS2CTeleportEntity::serialize, MessageS2CTeleportEntity::deserialize, MessageS2CTeleportEntity::handle);
		CHANNEL.registerMessage(++id, MessageS2CSpawnParticle.class, MessageS2CSpawnParticle::serialize, MessageS2CSpawnParticle::deserialize, MessageS2CSpawnParticle::handle);
		CHANNEL.registerMessage(++id, MessageS2CUpdateEntityData.class, MessageS2CUpdateEntityData::serialize, MessageS2CUpdateEntityData::deserialize, MessageS2CUpdateEntityData::handle);
		CHANNEL.registerMessage(++id, MessageC2SUpdateSlabfishHat.class, MessageC2SUpdateSlabfishHat::serialize, MessageC2SUpdateSlabfishHat::deserialize, MessageC2SUpdateSlabfishHat::handle);
	}
}
