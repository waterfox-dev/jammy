package com.jammy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;

public class PropertiesReader
{
    public static Properties getProperties(String path)
    {
        TreeMap<String, String> map = new TreeMap<>();
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
            Properties prop = new Properties();
            prop.load(bufferedReader);
            return prop;
        }
        catch(IOException e)
        {
            System.out.println(path + ":File  not found");
            //TODO : call the stop procedure
            return null;
        }
    }
}
