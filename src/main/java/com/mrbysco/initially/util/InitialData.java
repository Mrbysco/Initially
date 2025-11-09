package com.mrbysco.initially.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.initially.Initially;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InitialData extends SavedData {
	private static final String DATA_NAME = Initially.MOD_ID + "_world_data";
	public static final Codec<InitialData> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							UUIDUtil.STRING_CODEC.listOf().fieldOf("UUIDList").forGetter(data -> data.playerList)
					)
					.apply(instance, InitialData::new)
	);
	private final List<UUID> playerList;

	public InitialData(List<UUID> playerList) {
		this.playerList = playerList;
		this.setDirty();
	}

	private InitialData() {
		this(new ArrayList<>());
	}

	public static SavedDataType<InitialData> type() {
		return new SavedDataType<>(DATA_NAME, InitialData::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE);
	}

	public boolean hasBeenGiven(UUID uuid) {
		return this.playerList.contains(uuid);
	}

	public void setGiven(UUID uuid) {
		this.playerList.add(uuid);
	}

	public static InitialData get(Level level) {
		if (!(level instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client level. This is wrong.");
		}
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);

		assert overworld != null;
		DimensionDataStorage storage = overworld.getDataStorage();
		return storage.computeIfAbsent(type());
	}
}
