package com.jammy.bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandLoader
{
    public static void load(JDA jda)
    {
        jda.updateCommands().addCommands(
                Commands.slash("ping", "Calculate ping of the bot"),
                Commands.slash("setcitation", "Define the citation channel for the bot")
                        .setGuildOnly(true)
                        .addOption(OptionType.CHANNEL, "channel", "The citation channel")
        ).queue();

    }
}
