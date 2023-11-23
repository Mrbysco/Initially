package com.mrbysco.initially.util;

import com.mrbysco.initially.Initially;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InitialData extends SavedData {
	private static final String DATA_NAME = Initially.MOD_ID + "_world_data";
	private final List<UUID> playerList = new ArrayList<>();

	public InitialData(List<UUID> playerList) {
		this.playerList.clear();
		this.playerList.addAll(playerList);
	}

	public InitialData() {
		this(new ArrayList<>());
	}

	public boolean hasBeenGiven(UUID uuid) {
		return this.playerList.contains(uuid);
	}

	public void setGiven(UUID uuid) {
		this.playerList.add(uuid);
	}

	public static InitialData load(CompoundTag compound) {
		ListTag listTag = compound.getList("UUIDList", CompoundTag.TAG_COMPOUND);
		List<UUID> uuidList = new ArrayList<>();
		for (int i = 0; i < listTag.size(); ++i) {
			CompoundTag uuidTag = listTag.getCompound(i);
			uuidList.add(uuidTag.getUUID("UUID"));
		}

		return new InitialData(uuidList);
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		ListTag listTag = new ListTag();
		for (UUID uuid : playerList) {
			CompoundTag uuidTag = new CompoundTag();
			uuidTag.putUUID("UUID", uuid);
			listTag.add(uuidTag);
		}
		compound.put("UUIDList", listTag);

		return compound;
	}

	public static InitialData get(Level level) {
		if (!(level instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client level. This is wrong.");
		}
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);

		DimensionDataStorage storage = overworld.getDataStorage();
		return storage.computeIfAbsent(new SavedData.Factory<>(InitialData::new, InitialData::load), DATA_NAME);
	}

	public record SpawnerInfo(Integer spawnCount, boolean playerPlaced) {
	}
}
