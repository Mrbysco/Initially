package com.mrbysco.initially;

import com.mrbysco.initially.command.InitialCommand;
import com.mrbysco.initially.config.ConfigHandler;
import com.mrbysco.initially.config.ConfigReloadManager;
import com.mrbysco.initially.handler.InitialHandler;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Initially.MOD_ID)
public class Initially {
	public static final String MOD_ID = "initially";
	public static final Logger LOGGER = LogManager.getLogger();

	public Initially() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::loadComplete);

		NeoForge.EVENT_BUS.register(new InitialHandler());
		NeoForge.EVENT_BUS.register(new ConfigReloadManager());
		NeoForge.EVENT_BUS.addListener(this::onCommandRegister);
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		InitialCommand.initializeCommands(event.getDispatcher());
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		ConfigHandler.initializeConfig();
	}
}
