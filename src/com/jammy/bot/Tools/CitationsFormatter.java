package com.jammy.bot.Tools;

import com.jammy.bot.structures.Citation;
import com.qkyrie.markdown2pdf.Markdown2PdfConverter;
import com.qkyrie.markdown2pdf.internal.reading.SimpleStringMarkdown2PdfReader;
import com.qkyrie.markdown2pdf.internal.writing.SimpleFileMarkdown2PdfWriter;
import net.dv8tion.jda.api.entities.Guild;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CitationsFormatter
{

    private ArrayList<Citation> citations;
    private Guild guild;
    public Path path;

    public CitationsFormatter(ArrayList<Citation> citations, Guild guild)
    {
        this.citations = citations;
        this.guild = guild;

        StringBuilder content = new StringBuilder();

        content.append("# Citations \n");

        for(Citation c : citations)
        {
            content.append("---\n");
            content.append("> ").append(c.content).append("\n \n ");
            content.append("**").append(guild.getMemberById(c.author).getEffectiveName()).append("**").append(" - ").append(c.time).append("\n");
        }

        Path path = Paths.get("assets/markdown/" + guild.getIdLong() + ".md");

        try
        {
            Files.writeString(path, content.toString());
            this.path = path;
        }
        catch (IOException e)
        {
            System.out.println("Path not found");
            //TODO : call the stop procedure
        }


    }
}
