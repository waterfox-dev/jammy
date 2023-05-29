package com.jammy.bot.commands;

import com.jammy.bot.Bot;
import com.jammy.bot.database.Database;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class JammyCommand
{
    public void insertCommand(Database db, SlashCommandInteractionEvent event, String content)
    {
        db.addCommand(event, content);
    }
    public abstract void execute(Database db, SlashCommandInteractionEvent event);
}
