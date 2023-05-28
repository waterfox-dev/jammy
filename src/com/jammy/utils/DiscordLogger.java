package com.jammy.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DiscordLogger
{
    public static String log(String message)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        return "`[" + dtf.format(now) + "] -> " + message + "`";
    }
}
