package com.example.wei_tung.mynotebook;

/**
 * Created by Wei-Tung on 11/20/2016.
 */

public class Note
{
    private String title;
    private String content;

    public Note( String title, String content )
    {
        this.title = title;
        this.content = content;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public void setContent( String content )
    {
        this.content = content;
    }

    public String getTitle()
    {
        return title;
    }

    public String getContent()
    {
        return content;
    }

    @Override
    public String toString()
    {
        if ( title.equals( "+ New Note" ) )
            return title;
        else
            return "• " + title;
    }
}
