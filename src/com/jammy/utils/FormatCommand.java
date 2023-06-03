package com.jammy.utils;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import okio.Options;

public class FormatCommand
{
    public static String formatCommand(SlashCommandInteractionEvent event)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(event.getName()).append(" - ");
        for(OptionMapping e : event.getOptions())
        {
            sb.append(e.getAsString()).append(";");
        }
        return sb.toString();
    }
}
