package com.jammy.bot.commands;

import com.jammy.bot.database.Database;
import com.jammy.utils.FormatCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SetCitationCommand extends JammyCommand
{

    @Override
    public void execute(Database db, SlashCommandInteractionEvent event)
    {
        if(event.getOption("channel") == null)
        {
            System.out.println("test");
            event.reply("Channel args is required").setEphemeral(true).queue();
        }
        else {
            event.deferReply().queue();
            this.insertCommand(db, event, FormatCommand.formatCommand(event));
            boolean result = db.addCitationChannel(event.getGuild().getIdLong(), event.getOption("channel").getAsLong());
            if (!result) {
                event.getHook().editOriginal("Sorry an issue occcured, please try again soon. " +
                        "**If your channel is already added : it's may produce this error. You can remove with `/rmcitation` command**").queue();
            } else {
                event.getHook().editOriginal("Successfully added").queue();
            }
        }
    }
}
