package com.jammy.bot.database;

import com.jammy.utils.PropertiesReader;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class Database
{
    private Properties properties;
    private Connection connection;

    public Database()
    {
        try
        {
            properties = PropertiesReader.getProperties("assets/config/database.properties");
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
        try
        {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            String query = "INSERT INTO jam_log(log_date, log_message)" + "VALUES (?,?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setDate(1, sqlDate);
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
        try
        {
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            String query = "INSERT INTO jam_guild(gui_id, gui_name, gui_owner, gui_date)" + "VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, guild.getIdLong());
            preparedStatement.setString(2, guild.getName());
            preparedStatement.setLong(3, guild.getOwnerIdLong());
            preparedStatement.setDate(4, sqlDate);
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
}
