package com.mrbysco.initially.handler;

import com.mojang.brigadier.StringReader;
import com.mrbysco.initially.Initially;
import com.mrbysco.initially.config.object.ItemObject;
import com.mrbysco.initially.util.InitialData;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InitialHandler {
	public static final List<ItemObject> itemList = new ArrayList<>();
	private static final String initialGiven = Initially.MOD_ID + ":initialGiven";

	@SubscribeEvent
	public void firstJoin(PlayerLoggedInEvent event) {
		final Player player = event.getEntity();
		if (!player.level().isClientSide()) {
			CompoundTag playerData = player.getPersistentData();
			InitialData initialData = InitialData.get(player.level());
			if (playerData.getBooleanOr(initialGiven, false)) {
				initialData.setGiven(player.getUUID());
				initialData.setDirty();
				playerData.remove(initialGiven);
			}

			if (!initialData.hasBeenGiven(player.getUUID())) {
				giveInitially(player);
				initialData.setGiven(player.getUUID());
				initialData.setDirty();
			}
		}
	}

	public static void giveInitially(Player player) {
		for (ItemObject object : itemList) {
			if (!object.itemLocation().isEmpty()) {
				ResourceLocation location = ResourceLocation.tryParse(object.itemLocation());
				if (location == null) continue;
				Optional<Item> optionalItem = BuiltInRegistries.ITEM.getOptional(location);
				if (optionalItem.isPresent()) {
					Item item = optionalItem.get();
					Inventory inventory = player.getInventory();
					int slot = object.slot();
					ItemStack stack = new ItemStack(item, object.count());
					if (!object.components().isEmpty()) {
						ItemParser parser = new ItemParser(player.level().registryAccess());
						try {
							ItemParser.ItemResult result = parser.parse(new StringReader(object.itemLocation() + object.components()));
							//Have to add the item location so that the parser doesn't throw an error
							stack.applyComponents(result.components());
						} catch (Exception e) {
							Initially.LOGGER.trace("Exception: ", e);
						}
					}
					if (inventory.getItem(slot).isEmpty()) {
						inventory.setItem(slot, stack);
					} else {
						if (!player.addItem(stack)) {
							ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), stack);
							player.level().addFreshEntity(itemEntity);
						}
					}
				}
			}
		}
	}
}
