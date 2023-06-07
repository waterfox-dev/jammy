package com.jammy.bot;

import com.jammy.bot.commands.*;
import com.jammy.bot.database.Database;
import com.jammy.bot.events.JoinGuildListener;
import com.jammy.bot.events.QuitGuildListener;
import com.jammy.bot.events.ReadyListener;
import com.jammy.bot.events.StopListener;
import com.jammy.utils.PropertiesReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

public class Bot extends ListenerAdapter
{

    private static String token;
    private static Properties channelProperties;
    private static Properties botProperties;

    public static Database database = new Database();
    public static JDA jda;

    public static JDABuilder jdaBuilder;

    public static long logGuild;
    public static long logChannel;
    public static long exceptionChannel;


    public void loadBot(String token)
    {
        Bot.token = token;
        Bot.jdaBuilder = JDABuilder.createDefault(Bot.token,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_MEMBERS);
        Bot.jdaBuilder.setActivity(Activity.listening("Boogie Body - Rock The Factory"));

        Bot.channelProperties = PropertiesReader.getProperties("assets/config/channel.properties");
        Bot.botProperties = PropertiesReader.getProperties("assets/config/bot.properties");

        try
        {
            assert Bot.channelProperties != null;

            Bot.logGuild = Long.parseLong(Bot.channelProperties.getProperty("logGuild"));
            Bot.logChannel = Long.parseLong(Bot.channelProperties.getProperty("logChannel"));
            Bot.exceptionChannel = Long.parseLong(Bot.channelProperties.getProperty("exceptionChannel"));

        }
        catch (AssertionError e)
        {
            System.out.println(e.toString());
        }

        try
        {
            Bot.jda = jdaBuilder.build().awaitReady();

            Bot.jdaBuilder.addEventListeners(new ReadyListener(this));
            Bot.jdaBuilder.addEventListeners(new StopListener(this));
            Bot.jdaBuilder.addEventListeners(new JoinGuildListener(this));
            Bot.jdaBuilder.addEventListeners(new QuitGuildListener(this));
            Bot.jdaBuilder.addEventListeners(new Bot());

            Bot.jda = Bot.jdaBuilder.build();
            CommandLoader.load(Bot.jda);

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
            case "ping" -> new PingCommand().execute(Bot.database, event);
            case "setcitation" -> new SetCitationCommand().execute(Bot.database, event);
            case "cite" -> new CiteCommand().execute(Bot.database, event);
            case "formatcitation" -> new FormatCitationCommand().execute(Bot.database, event);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getAuthor().getIdLong() == Long.parseLong(botProperties.getProperty("ownerId")))
        {
            System.out.println(event.getMessage().getContentRaw());
            if(event.getMessage().getContentRaw().equals("//stop"))
            {
                Bot.jda.shutdown();
                System.exit(1);
            }
            if(event.getMessage().getContentRaw().startsWith("//sql"))
            {
                String query = event.getMessage().getContentRaw().split("//sql ")[1];
                String datas = Bot.database.executeSpecific(query);
                Path path = Paths.get("assets/csv/response.csv");
                try
                {
                    Files.writeString(path, datas);
                    FileUpload fileUpload = FileUpload.fromData(path.toFile());
                    event.getChannel().sendFiles(fileUpload).queue();
                }
                catch (IOException e)
                {
                    System.out.println(e);
                    //TODO : call the stop procedure
                }

            }
        }
    }
}
