package core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import core.BlueprintTest;
import io.github.fabricators_of_create.porting_lib.util.LazySpawnEggItem;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

@Mod.EventBusSubscriber(modid = BlueprintTest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TestItems {
	private static final Helper HELPER = BlueprintTest.REGISTRY_HELPER.getItemSubHelper();

	public static final RegistryObject<Item> ITEM = HELPER.createTest();
	public static final RegistryObject<LazySpawnEggItem> COW_SPAWN_EGG = HELPER.createItem("test_spawn_egg", () -> new LazySpawnEggItem(TestEntities.COW, 100, 200, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final Pair<RegistryObject<Item>, RegistryObject<Item>> BOAT = HELPER.createBoatAndChestBoatItem("test", TestBlocks.BLOCK);
	public static final RegistryObject<Item> ITEM_THE_SQUEAKQUEL = HELPER.createCompatItem("fortnite", "item_the_squeakquel", new Item.Properties().fireResistant(), CreativeModeTab.TAB_BREWING);

	public static class Helper extends ItemSubRegistryHelper {

		public Helper(RegistryHelper parent) {
			super(parent, parent.getItemSubHelper().getDeferredRegister());
		}

		private RegistryObject<Item> createTest() {
			return this.deferredRegister.register("test", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));
		}

	}
}
