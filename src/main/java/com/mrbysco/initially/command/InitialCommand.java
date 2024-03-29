package com.mrbysco.initially.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.initially.handler.InitialHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public class InitialCommand {
	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("initially");
		root.requires((sourceStack) -> sourceStack.hasPermission(2))
				.then(Commands.literal("give").then(Commands.argument("player", EntityArgument.players()).executes(InitialCommand::giveInitial)));
		dispatcher.register(root);
	}

	private static int giveInitial(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
			InitialHandler.giveInitially(player);
			MutableComponent text = Component.translatable("commands.initially.give.success", player.getName().getContents()).withStyle(ChatFormatting.GOLD);
			ctx.getSource().sendSuccess(() -> text, false);
		}

		return 0;
	}
}
