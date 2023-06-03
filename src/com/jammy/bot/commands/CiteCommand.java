package com.jammy.bot.commands;

import com.jammy.bot.database.Database;
import com.jammy.utils.FormatCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CiteCommand extends JammyCommand
{

    @Override
    public void execute(Database db, SlashCommandInteractionEvent event)
    {
        if(event.getOption("author") == null ||
                event.getOption("content") == null ||
                event.getOption("reporter") == null)
        {
            event.reply("All options are mandatory.").setEphemeral(true).queue();
        }
        else
        {
            event.deferReply().queue();
            this.insertCommand(db, event, FormatCommand.formatCommand(event));
            if(db.addCite(event))
            {
                event.getHook().setEphemeral(true).editOriginal("We're preparing your citation").queue();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("**Citation**");
                eb.setColor(Color.PINK);
                eb.addField(String.format("'%s'", event.getOption("content").getAsString()),
                        String.format("A citation from <@%d>", event.getOption("author").getAsLong()),
                        true);
                eb.setThumbnail(event.getGuild().getMemberById(event.getOption("author").getAsLong()).getUser().getAvatarUrl());
                eb.setFooter(String.format("Reported by : %s", event.getGuild().getMemberById(event.getOption("reporter").getAsLong()).getEffectiveName()));
                long chan = db.getCitationChannel(event);
                if(chan != -1)
                {
                    event.getGuild().getTextChannelById(chan).sendMessageEmbeds(eb.build()).queue();
                }
                else
                {
                    event.getHook().setEphemeral(true).editOriginal("You're guild doesn't have registered citation channel, please add with `/setcitation`").queue();

                }

            }
            else
            {
                event.getHook().editOriginal("Sorry but... an issue occured, please try again later").queue();
            }
        }
    }
}
