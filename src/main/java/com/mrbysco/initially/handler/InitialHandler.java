package com.mrbysco.initially.handler;

import com.mrbysco.initially.Initially;
import com.mrbysco.initially.config.object.ItemObject;
import com.mrbysco.initially.util.InitialData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class InitialHandler {
	public static final List<ItemObject> itemList = new ArrayList<>();
	private static final String initialGiven = Initially.MOD_ID + ":initialGiven";

	@SubscribeEvent
	public void firstJoin(PlayerLoggedInEvent event) {
		Player player = event.getPlayer();

		if (!player.level.isClientSide) {
			CompoundTag playerData = player.getPersistentData();
			InitialData initialData = InitialData.get(player.level);
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
				Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(object.itemLocation()));
				if (item != null) {
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
					player.getInventory().setItem(object.slot(), stack);
				}
			}
		}
	}
}
