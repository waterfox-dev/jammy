package com.jammy.bot.events;

import com.jammy.bot.Bot;
import com.jammy.utils.DiscordLogger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class ReadyListener implements EventListener
{
    private final Bot bot;
    private Guild logGuild;

    public ReadyListener(Bot bot)
    {
        System.out.println("test");
        this.bot = bot;
        this.logGuild = bot.jda.getGuildById(this.bot.logGuild);
    }

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent)
    {
        System.out.println(genericEvent.getClass().getSimpleName());
        if(genericEvent instanceof ReadyEvent)
        {
            try
            {
                TextChannel logChannel = this.logGuild.getTextChannelById(this.bot.logChannel);
                assert logChannel != null;
                logChannel.sendMessage(DiscordLogger.log("Jammy is ready !")).queue();
            }
            catch (AssertionError e)
            {
                System.out.println("Log Channel isn't found");
            }
        }
    }
}
