package com.jammy.utils;

import com.jammy.bot.database.Database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DiscordLogger
{
    public static String log(String message, Database database)
    {
        DateFormat dtf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        database.insertLog(date, message);

        return "`[" + dtf.format(date) + "] -> " + message + "`";
    }
}
