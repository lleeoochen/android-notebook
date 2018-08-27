package com.example.wei_tung.mynotebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/*
 * MainMenu class creates a list of note titles using arrayadapter and listview. The note content
 * is also stored in this class.
 * Date: 11/19/2016
 * Author: Wei Tung Chen
 */
public class MainMenu extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private ListView listView;
    private ArrayAdapter<Note> arrayAdapter;
    private ArrayList<Note> notelist;
    private int currentIndex = 1;

    @Override // initialize everything
    protected void onCreate(Bundle savedInstanceState)
    {
        // initialize contents and Action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        setupActionBar();

        // initialize notelist
        notelist = new ArrayList<Note>();
        notelist.add( 0, new Note( "+ New Note", "") );
        readFromFile();

        // implement lists in display
        arrayAdapter = new ArrayAdapter<>(this, R.layout.menu_item_template, notelist);
        listView = (ListView) findViewById( R.id.main_menu );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override // open note when clicked
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent = new Intent(MainMenu.this, EachNote.class );

        // If "+ New Note" is clicked, create a new note. Otherwise, open the old note
        if ( i == 0 )
        {
            intent.putExtra("titlecontent", new String[]{ "", "" });
            notelist.add( 1, new Note( "", "" ) );
            currentIndex = 1;
        }
        else
        {
            Note note = arrayAdapter.getItem( i );
            intent.putExtra("titlecontent", new String[]{note.getTitle(), note.getContent()});
            currentIndex = i;
        }

        // send intent to Each Note class and apply changes to the list
        MainMenu.this.startActivityForResult( intent, 1 );
        arrayAdapter.notifyDataSetChanged();
    }

    @Override // getting data back from EachNote class
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Note note = arrayAdapter.getItem( currentIndex );

        // retrieving data from EachNote class
        if ( requestCode == 1 && resultCode == RESULT_OK )
        {
            String[] newTitleContent;
            if ( data == null )
            {
                newTitleContent= null;
            }
            else
            {
                newTitleContent= data.getStringArrayExtra( "newtitlecontent" );
            }
            note.setTitle(newTitleContent[0]);
            note.setContent(newTitleContent[1]);
        }

        // If the content is empty, delete the note. Otherwise, set title to Untitled Note*
        if ( note.getTitle().isEmpty() )
        {
            if (note.getContent().isEmpty())
            {
                arrayAdapter.remove( note );
            }
            else
            {
                note.setTitle( "Untitled*" );
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop()
    {
        saveToFile();
        super.onStop();
    }

    // save the list to file
    private void saveToFile()
    {
        SharedPreferences pref = getSharedPreferences( "my_data", MODE_PRIVATE );
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        for ( int i = 1; i < notelist.size(); i++ )
        {
            editor.putString( "note" + i,
                    encodedString( notelist.get(i).getTitle(), notelist.get(i).getContent() ) );
        }
        editor.commit();
    }

    // read data from file
    private void readFromFile()
    {
        SharedPreferences pref = getSharedPreferences( "my_data", MODE_PRIVATE );
        String line = "line";
        String[] titlecontent;

        for ( int i = 1; !line.isEmpty(); i++ )
        {
            line = pref.getString( "note" + i, "" );
            if ( !line.isEmpty() )
            {
                titlecontent = decodedString( line );
                notelist.add( i, new Note( titlecontent[0], titlecontent[1] ) );
            }
        }
    }

    // customize texts and icons in actionbar
    private void setupActionBar()
    {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.actionbar_icon);

        // fromHtml(String, int) works only when API is >=24
        if (Build.VERSION.SDK_INT >= 24)
            getSupportActionBar().setTitle(Html.fromHtml("<big><font color='#000'>OTEBOOK</font></big>",1));
        else
            getSupportActionBar().setTitle(Html.fromHtml("<big><font color='#000'>OTEBOOK</font></big>"));
    }

    // testing dummies for initializing notes
    private void initializeLists()
    {
        arrayAdapter.add( new Note( "Monday", "" ) );
        arrayAdapter.add( new Note( "Tuesday", "Hello" ) );
        arrayAdapter.add( new Note( "Wednesday", "123" ) );
        arrayAdapter.add( new Note( "Thursday", "mmhm" ) );
        arrayAdapter.add( new Note( "Friday", "test1" ) );
        arrayAdapter.add( new Note( "Saturday", "XD" ) );
        arrayAdapter.add( new Note( "Sunday", "" ) );
        arrayAdapter.add( new Note( "", "123" ) );
        arrayAdapter.add( new Note( "", "" ) );
    }

    // length of title + title + content
    private String encodedString( String title, String content )
    {
        return title.length() + " " + title + content;
    }

    // extract title and content from an encoded string
    private String[] decodedString( String str)
    {
        int spacePos = str.indexOf( " " );
        int titleLength = Integer.parseInt( str.substring( 0, spacePos ) );
        String title = str.substring( spacePos + 1, titleLength + spacePos + 1 );
        String content = str.substring( titleLength + spacePos + 1 );
        return new String[]{ title, content };
    }
}
