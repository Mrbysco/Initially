package com.mrbysco.initially;

import com.mrbysco.command.InitialCommand;
import com.mrbysco.config.ConfigHandler;
import com.mrbysco.handler.InitialHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Initially.MOD_ID)
public class Initially {
    public static final String MOD_ID = "initially";
    public static final Logger LOGGER = LogManager.getLogger();

    public Initially() {
        MinecraftForge.EVENT_BUS.register(new InitialHandler());
        MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
    }

    public void onCommandRegister(RegisterCommandsEvent event) {
        InitialCommand.initializeCommands(event.getDispatcher());
    }

    private void onServerStarted(final ServerStartedEvent event) {
        ConfigHandler.loadInitialConfig();
    }
}
