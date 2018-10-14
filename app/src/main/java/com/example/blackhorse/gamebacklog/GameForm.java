package com.example.blackhorse.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class GameForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_form);

        final EditText titleEdit = findViewById(R.id.titleEdit);
        final EditText platformEdit = findViewById(R.id.platformEdit);
        final EditText notesEdit = findViewById(R.id.notesEdit);
        final Spinner statusSpinner = findViewById(R.id.statusSpinner);

        Game gameUpdate =  getIntent().getParcelableExtra(MainActivity.EXTRA_GAME);
        //If editing a game set the text fields to the game details.
        if(gameUpdate != null){
            titleEdit.setText(gameUpdate.getTitle());
            platformEdit.setText(gameUpdate.getPlatform());
            notesEdit.setText(gameUpdate.getNotes());
            statusSpinner.setSelection(gameUpdate.getStatus());
        }else{
            gameUpdate = new Game("","","",-1,"");
        }

        final long id = gameUpdate.getId();

        FloatingActionButton fab = findViewById(R.id.editFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleText = titleEdit.getText().toString();
                String platformText = platformEdit.getText().toString();
                String notesText = notesEdit.getText().toString();
                int statusPos = statusSpinner.getSelectedItemPosition();
                Log.d("EditGameActivity", "OnClick Status Pos: " + statusPos);

                if (titleText.isEmpty())
                    Toast.makeText(GameForm.this,
                            "Please Enter a title for the game", Toast.LENGTH_SHORT).show();
                else if (platformText.isEmpty())
                    Toast.makeText(GameForm.this,
                            "Please Enter a platform for the game", Toast.LENGTH_SHORT).show();
                else{
                    Intent resultIntent = new Intent();
                    DateFormat dateF = DateFormat.getDateInstance();
                    Date now = new Date(System.currentTimeMillis());

                    Game game = new Game(titleText,platformText,notesText,statusPos, dateF.format(now));
                    //Set the id for the game so that it updates the correct one
                    game.setId(id);
                    resultIntent.putExtra(MainActivity.EXTRA_GAME, game);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}


