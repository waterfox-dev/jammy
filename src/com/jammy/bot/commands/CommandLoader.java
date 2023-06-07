package com.jammy.bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLoader
{
    public static List<SlashCommandData> commands = Arrays.asList(
            Commands.slash("ping", "Calculate ping of the bot"),
            Commands.slash("setcitation", "Define the citation channel for the bot")
                    .setGuildOnly(true)
                    .addOption(OptionType.CHANNEL, "channel", "The citation channel"),
            Commands.slash("cite", "Add a citation to citation channel")
                    .setGuildOnly(true)
                    .addOption(OptionType.USER, "author", "The author of the citation")
                    .addOption(OptionType.STRING, "content", "The content of the citation")
                    .addOption(OptionType.USER, "reporter", "The reporter of the citation"),
            Commands.slash("formatcitation", "Format all citations into a file")
                    .setGuildOnly(true)

    );


    public static void load(JDA jda)
    {
        jda.updateCommands().addCommands(            Commands.slash("ping", "Calculate ping of the bot"),
                Commands.slash("setcitation", "Define the citation channel for the bot")
                        .setGuildOnly(true)
                        .addOption(OptionType.CHANNEL, "channel", "The citation channel"),
                Commands.slash("cite", "Add a citation to citation channel")
                        .setGuildOnly(true)
                        .addOption(OptionType.USER, "author", "The author of the citation")
                        .addOption(OptionType.STRING, "content", "The content of the citation")
                        .addOption(OptionType.USER, "reporter", "The reporter of the citation"),
                Commands.slash("formatcitation", "Format all citations into a file")
                        .setGuildOnly(true)).queue();
    }
}
