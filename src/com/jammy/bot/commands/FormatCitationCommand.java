package com.jammy.bot.commands;

import com.jammy.bot.Tools.CitationsFormatter;
import com.jammy.bot.database.Database;
import com.jammy.bot.structures.Citation;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.*;
import java.nio.Buffer;
import java.nio.channels.Channel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class FormatCitationCommand extends JammyCommand
{

    @Override
    public void execute(Database db, SlashCommandInteractionEvent event)
    {
        event.reply("We're preparing your citations").setEphemeral(true).queue();
        MessageChannelUnion channel = event.getChannel();
        ArrayList<Citation> citations = db.getCitation(Objects.requireNonNull(event.getGuild()).getIdLong());
        File file = new CitationsFormatter(citations, event.getGuild()).path.toFile();
        FileUpload upload = FileUpload.fromData(file);
        channel.sendMessage("Hey ! Here your citations !").addFiles(upload).queue();

    }
}
