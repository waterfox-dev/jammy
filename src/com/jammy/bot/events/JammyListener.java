package com.jammy.bot.events;

import com.jammy.bot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.EventListener;

public abstract class JammyListener implements EventListener
{
    protected final Bot bot;
    protected Guild logGuild;

    public JammyListener(Bot bot)
    {
        this.bot = bot;
        this.logGuild = bot.jda.getGuildById(this.bot.logGuild);
    }
}
