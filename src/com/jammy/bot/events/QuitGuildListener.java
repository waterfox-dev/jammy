package com.jammy.bot.events;

import com.jammy.bot.Bot;
import com.jammy.utils.DiscordLogger;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;

public class QuitGuildListener extends JammyListener
{

    public QuitGuildListener(Bot bot)
    {
        super(bot);
    }

    @Override
    public void onEvent(GenericEvent genericEvent)
    {
        if(genericEvent instanceof GuildLeaveEvent)
        {
            try
            {
                bot.database.removeGuild(((GuildLeaveEvent) genericEvent).getGuild());
                TextChannel logChannel = this.logGuild.getTextChannelById(this.bot.logChannel);
                assert logChannel != null;
                logChannel.sendMessage(DiscordLogger.log("Jammy quit the guild " + ((GuildLeaveEvent) genericEvent).getGuild().getName(), this.bot.database)).queue();
            }
            catch (AssertionError e)
            {
                System.out.println("Log Channel isn't found");
            }
        }
    }
}
