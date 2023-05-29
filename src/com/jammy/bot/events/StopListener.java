package com.jammy.bot.events;

import com.jammy.bot.Bot;
import com.jammy.utils.DiscordLogger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.hooks.EventListener;

public class StopListener extends JammyListener {

    public StopListener(Bot bot)
    {
        super(bot);
    }

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent)
    {
        if(genericEvent instanceof ShutdownEvent)
        {
            try
            {
                TextChannel logChannel = this.logGuild.getTextChannelById(this.bot.logChannel);
                assert logChannel != null;
                logChannel.sendMessage(DiscordLogger.log("Jammy is stopping !", this.bot.database)).queue();
            }
            catch (AssertionError e)
            {
                System.out.println("Log Channel isn't found");
            }
        }
    }
}
