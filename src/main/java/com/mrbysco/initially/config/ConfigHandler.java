package com.mrbysco.initially.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrbysco.initially.config.object.ItemObject;
import com.mrbysco.initially.handler.InitialHandler;
import com.mrbysco.initially.Initially;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final File INITIAL_FOLDER = new File(FMLPaths.CONFIGDIR.get().toFile() + "/initially");
	public static final File INITIAL_FILE = new File(INITIAL_FOLDER, "Initially.json");

	public static void initializeConfig() {
		if (!INITIAL_FOLDER.exists() || !INITIAL_FILE.exists()) {
			INITIAL_FOLDER.mkdirs();

			List<ItemObject> items = new ArrayList<>(41);
			items.add(new ItemObject(40, "offhand", "", "", 1));
			items.add(new ItemObject(39, "head", "", "", 1));
			items.add(new ItemObject(38, "chest", "", "", 1));
			items.add(new ItemObject(37, "legs", "", "", 1));
			items.add(new ItemObject(36, "feet", "", "", 1));
			for (int i = 0; i < 36; i++) {
				switch (i) {
					default -> items.add(new ItemObject(i, "", "", 1));
				}
			}

			InitialConfig initialConfig = new InitialConfig(items);
			try (FileWriter writer = new FileWriter(INITIAL_FILE)) {
				GSON.toJson(initialConfig, writer);
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadInitialConfig() {
		InitialHandler.itemList.clear();
		String fileName = INITIAL_FILE.getName();
		try (FileReader json = new FileReader(INITIAL_FILE)) {
			final InitialConfig initialConfig = GSON.fromJson(json, InitialConfig.class);
			if (initialConfig != null) {
				InitialHandler.itemList.addAll(initialConfig.initialList());
			} else {
				Initially.LOGGER.error("Could not load initial item from {}.", fileName);
			}
		} catch (final Exception e) {
			Initially.LOGGER.error("Unable to load file {}. Please make sure it's a valid json.", fileName);
			Initially.LOGGER.catching(e);
		}
	}
}
