import com.jammy.bot.Bot;
import com.jammy.utils.PropertiesReader;

import java.util.Properties;

public class Main
{
    public static void main(String[] args)
    {
        Properties token = PropertiesReader.getProperties("assets/config/token.properties");
        try
        {
            assert token != null;
            Bot jammy = new Bot(token.getProperty("token"));
        }
        catch (AssertionError e)
        {
            System.out.println("Token not found");
        }
    }
}