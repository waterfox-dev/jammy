package com.jammy.bot.structures;

public class Citation
{
    public int id;
    public long channel;
    public long author;
    public String content;
    public long submit;
    public java.sql.Timestamp time;

    public Citation(int i, long c, long a, String con, long s, java.sql.Timestamp time)
    {
        this.id = i;
        this.channel = c;
        this.author = a;
        this.content = con;
        this.submit = s;
        this.time = time;
    }
}
