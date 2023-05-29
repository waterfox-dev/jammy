package com.jammy.bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandLoader
{
    public static void load(JDA jda)
    {
        jda.updateCommands().addCommands(
                Commands.slash("ping", "Calculate ping of the bot")
        ).queue();

    }
}
