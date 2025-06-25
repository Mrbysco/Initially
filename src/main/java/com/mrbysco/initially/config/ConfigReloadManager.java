package com.mrbysco.initially.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.jetbrains.annotations.NotNull;

public class ConfigReloadManager implements ResourceManagerReloadListener {
	@Override
	public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
		ConfigHandler.loadInitialConfig();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onAddReloadListeners(AddServerReloadListenersEvent event) {
		event.addListener(ResourceLocation.fromNamespaceAndPath("initially", "config"), this);
	}
}
