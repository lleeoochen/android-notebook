package com.example.wei_tung.mynotebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 * EachNote class allows user to edit each note and save it.
 * Date: 11/19/2016
 * Author: Wei Tung Chen
 */
public class EachNote extends AppCompatActivity implements View.OnClickListener
{
    TextView noteTitle;
    TextView noteContent;

    @Override //initialize everything!
    protected void onCreate(Bundle savedInstanceState)
    {
        // initialize content and actionbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_note);
        setupActionBar();

        // get title and content data from MainMenu class
        retrieveData( savedInstanceState );

        // create a delete button
        Button deleteButton = (Button) findViewById( R.id.delete_button );
        deleteButton.setOnClickListener(this);
    }

    // get data from MainMenu class
    private void retrieveData( Bundle savedInstanceState )
    {
        String[] newTitleContent;
        if ( savedInstanceState == null )
        {
            Bundle bundle = getIntent().getExtras();
            if (bundle == null)
            {
                newTitleContent = null;
            }
            else
            {
                newTitleContent = bundle.getStringArray( "titlecontent" );
            }
        }
        else
        {
            newTitleContent = (String[]) savedInstanceState.getSerializable( "titlecontent" );
        }

        noteTitle = (TextView) this.findViewById(R.id.note_title);
        noteContent = (TextView) this.findViewById(R.id.note_content);

        //if the title or the content is empty, don't do anything
        if ( !newTitleContent[0].equals( "" ) )
        {
            noteTitle.setText(newTitleContent[0]);
        }
        if ( !newTitleContent[1].equals( "" ) )
        {
            noteContent.setText(newTitleContent[1]);
        }
    }

    @Override //when back button is pressed, save note
    public void onBackPressed()
    {
        saveNote( noteTitle.getText().toString(), noteContent.getText().toString() );
    }

    @Override //when delete button is clicked, ask and delete
    public void onClick(View view)
    {
        // If the note is empty, delete without ask. Else, ask before delete
        if ( noteTitle.getText().toString().isEmpty() && noteContent.getText().toString().isEmpty() )
        {
            saveNote( "", "" );
        }
        else
        {
            // construct an alert message with positive/negative button
            AlertDialog.Builder confirmMessage = new AlertDialog.Builder(this);
            confirmMessage.setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this note?")
                    .setCancelable(false)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            saveNote("", "");
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialogInterface.cancel();
                        }
                    })
                    .show();
        }
    }

    //save the note title and note content
    private void saveNote( String saveTitle, String saveContent )
    {
        // when back button is pressed, pass note to MainMenu and save it
        Intent intent = new Intent( EachNote.this, MainMenu.class );
        intent.putExtra( "newtitlecontent", new String[]{ saveTitle, saveContent } );
        setResult(RESULT_OK, intent);
        finish();
    }

    // customize texts and icons in actionbar
    private void setupActionBar()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // fromHtml(String, int) works only when API is >=24
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onContextItemSelected(item);
    }
}
