package com.mrbysco.initially;

import com.mrbysco.command.InitialCommand;
import com.mrbysco.config.ConfigHandler;
import com.mrbysco.config.ConfigReloadManager;
import com.mrbysco.handler.InitialHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Initially.MOD_ID)
public class Initially {
	public static final String MOD_ID = "initially";
	public static final Logger LOGGER = LogManager.getLogger();

	public Initially() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::loadComplete);

		MinecraftForge.EVENT_BUS.register(new InitialHandler());
		MinecraftForge.EVENT_BUS.register(new ConfigReloadManager());
		MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		InitialCommand.initializeCommands(event.getDispatcher());
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		ConfigHandler.initializeConfig();
	}
}
