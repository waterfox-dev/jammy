package com.jammy.bot.database;

import com.jammy.bot.structures.Citation;
import com.jammy.utils.PropertiesReader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

public class Database
{
    private Properties properties;
    private Connection connection;

    public Database()
    {
        try
        {
            this.properties = PropertiesReader.getProperties("assets/config/database.properties");
            assert properties != null;

            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    properties.getProperty("host"), properties.getProperty("user"), properties.getProperty("password")
            );

        }
        catch (AssertionError | ClassNotFoundException | SQLException e)
        {
            System.out.println(e);
            //TODO : call the stop procedure
        }
    }

    public void insertLog(java.util.Date date, String message)
    {
        reload();
        try
        {
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());

            String query = "INSERT INTO jam_log(log_date, log_message)" + "VALUES (?,?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setTimestamp(1, sqlDate);
            preparedStatement.setString(2, message);
            preparedStatement.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e);
            //TODO : call the stop procedure
        }
    }

    public void addGuild(Guild guild)
    {
        reload();
        try
        {
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());

            String query = "INSERT INTO jam_guild(gui_id, gui_name, gui_owner, gui_date)" + "VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, guild.getIdLong());
            preparedStatement.setString(2, guild.getName());
            preparedStatement.setLong(3, guild.getOwnerIdLong());
            preparedStatement.setTimestamp(4, sqlDate);
            preparedStatement.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e);
            //TODO : call the stop procedure
        }
    }

    public void removeGuild(Guild guild)
    {
        reload();
        try
        {
            String query = "DELETE FROM jam_guild WHERE gui_id = " + "?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, guild.getIdLong());
            preparedStatement.execute();

        }
        catch (SQLException e)
        {
            System.out.println(e);
            //TODO : call the stop procedure
        }
    }

    public void addCommand(SlashCommandInteractionEvent event, String content)
    {
        reload();
        try
        {
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());

            long guildId = 0;

            if(event.getGuild() != null)
            {
                guildId = event.getGuild().getIdLong();
            }

            String query = "INSERT INTO jam_command(com_content, gui_id, com_author, com_date)" +  " VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1,content);
            preparedStatement.setLong(2, guildId);
            preparedStatement.setLong(3, event.getUser().getIdLong());
            preparedStatement.setTimestamp(4, sqlDate);
            preparedStatement.execute();
        }

        catch (SQLException e)
        {
            System.out.println(e);
            //TODO : call the stop procedure
        }
    }

    public boolean addCitationChannel(long guildId, long channelId)
    {
        reload();
        try
        {
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());

            String query = "INSERT INTO jam_citation_channel(chan_id, gui_id, citchan_date)" + "VALUES(?,?,?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, channelId);
            preparedStatement.setLong(2, guildId);
            preparedStatement.setTimestamp(3, sqlDate);
            preparedStatement.execute();
            return true;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return false;
            //TODO : call the stop procedure
        }
    }

    public boolean addCite(SlashCommandInteractionEvent event)
    {
        reload();
        try
        {
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());

            String query = "INSERT INTO jam_citation(chan_id, cit_author, cit_content, cit_submit_id, cit_date)" +
                    "VALUES((SELECT chan_id FROM jam_citation_channel WHERE gui_id = ?), ?, ?, ?, ?);";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, event.getGuild().getIdLong());
            preparedStatement.setLong(2, event.getOption("author").getAsLong());
            preparedStatement.setString(3, event.getOption("content").getAsString());
            preparedStatement.setLong(4, event.getOption("reporter").getAsLong());
            preparedStatement.setTimestamp(5, sqlDate);
            System.out.println(preparedStatement.toString());
            preparedStatement.execute();
            return true;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return false;
            //TODO : call the stop procedure
        }
    }

    public long getCitationChannel(SlashCommandInteractionEvent event)
    {
        reload();
        try
        {
            String query = "SELECT chan_id FROM jam_citation_channel WHERE gui_id = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, event.getGuild().getIdLong());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                return rs.getLong("chan_id");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return -1;
            //TODO : call the stop procedure
        }
        return -1;
    }

    public ArrayList<Citation> getCitation(long guild_id)
    {
        reload();
        try
        {
            String query = "SELECT * FROM jam_citation " +
                    "WHERE chan_id IN " +
                    "( " +
                    "    SELECT chan_id FROM jam_citation_channel WHERE gui_id = ?" +
                    ")";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, guild_id);

            ArrayList<Citation> citations = new ArrayList<>();
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                citations.add(new Citation(
                        rs.getInt("cit_id"),
                        rs.getLong("chan_id"),
                        rs.getLong("cit_author"),
                        rs.getString("cit_content"),
                        rs.getLong("cit_submit_id"),
                        rs.getTimestamp("cit_date")
                ));
            }

            return citations;
        }
        catch (SQLException e)
        {
            System.out.println(e);
            //todo : Call the stop procedure
            return null;

        }
    }

    private void reload()
    {
        try
        {
            this.connection = DriverManager.getConnection(
                    properties.getProperty("host"), properties.getProperty("user"), properties.getProperty("password")
            );
        }
        catch(SQLException e)
        {
            System.out.println(e);
            //todo : Call the stop procedure
        }
    }
}
