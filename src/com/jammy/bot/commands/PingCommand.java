package com.jammy.bot.commands;

import com.jammy.bot.database.Database;
import com.jammy.utils.FormatCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand extends JammyCommand
{

    @Override
    public void execute(Database database, SlashCommandInteractionEvent event)
    {
        System.out.println(database);
        this.insertCommand(database, event, FormatCommand.formatCommand(event));
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true).flatMap(v ->
            event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)
        ).queue();
    }
}
