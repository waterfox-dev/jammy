package com.jammy.bot;

import com.jammy.bot.commands.CommandLoader;
import com.jammy.bot.commands.PongCommand;
import com.jammy.bot.database.Database;
import com.jammy.bot.events.JoinGuildListener;
import com.jammy.bot.events.QuitGuildListener;
import com.jammy.bot.events.ReadyListener;
import com.jammy.bot.events.StopListener;
import com.jammy.utils.PropertiesReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Properties;

public class Bot extends ListenerAdapter
{
    private String token;
    private Properties channelProperties;

    public static Database database = new Database();

    public JDABuilder jdaBuilder;

    public long logGuild;
    public long logChannel;
    public long exceptionChannel;
    public JDA jda;


    public void loadBot(String token)
    {
        this.token = token;
        this.jdaBuilder = JDABuilder.createDefault(this.token);
        this.jdaBuilder.setActivity(Activity.listening("Boogie Body - Rock The Factory"));

        this.channelProperties = PropertiesReader.getProperties("assets/config/channel.properties");
        try
        {
            assert this.channelProperties != null;

            this.logGuild = Long.parseLong(this.channelProperties.getProperty("logGuild"));
            this.logChannel = Long.parseLong(this.channelProperties.getProperty("logChannel"));
            this.exceptionChannel = Long.parseLong(this.channelProperties.getProperty("exceptionChannel"));

        }
        catch (AssertionError e)
        {
            System.out.println(e.toString());
        }

        try
        {
            this.jda = jdaBuilder.build().awaitReady();

            this.jdaBuilder.addEventListeners(new ReadyListener(this));
            this.jdaBuilder.addEventListeners(new StopListener(this));
            this.jdaBuilder.addEventListeners(new JoinGuildListener(this));
            this.jdaBuilder.addEventListeners(new QuitGuildListener(this));
            this.jdaBuilder.addEventListeners(new Bot());

            this.jda = this.jdaBuilder.build();
            CommandLoader.load(this.jda);

        }
        catch (InterruptedException e)
        {
            System.out.println(e);
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        switch (event.getName()) {
            case "ping":
                new PongCommand().execute(Bot.database,  event);
        }
    }
}
