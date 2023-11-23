package com.mrbysco.initially.config;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

public class ConfigReloadManager implements ResourceManagerReloadListener {
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		ConfigHandler.loadInitialConfig();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onAddReloadListeners(AddReloadListenerEvent event) {
		event.addListener(this);
	}
}
