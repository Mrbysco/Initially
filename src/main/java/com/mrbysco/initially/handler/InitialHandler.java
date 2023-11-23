package com.mrbysco.initially.handler;

import com.mrbysco.initially.Initially;
import com.mrbysco.initially.config.object.ItemObject;
import com.mrbysco.initially.util.InitialData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
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

public class InitialHandler {
	public static final List<ItemObject> itemList = new ArrayList<>();
	private static final String initialGiven = Initially.MOD_ID + ":initialGiven";

	@SubscribeEvent
	public void firstJoin(PlayerLoggedInEvent event) {
		final Player player = event.getEntity();
		if (!player.level().isClientSide) {
			CompoundTag playerData = player.getPersistentData();
			InitialData initialData = InitialData.get(player.level());
			if (playerData.getBoolean(initialGiven)) {
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
				Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(object.itemLocation()));
				if (item != null) {
					Inventory inventory = player.getInventory();
					int slot = object.slot();
					ItemStack stack = new ItemStack(item, object.count());
					CompoundTag tag = null;
					if (!object.tag().isEmpty()) {
						try {
							tag = TagParser.parseTag(object.tag());
						} catch (Exception e) {
							Initially.LOGGER.trace(e);
						}
					}
					if (tag != null) {
						stack.setTag(tag);
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
