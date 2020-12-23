package com.example.inzzaktionapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class menu_barActivity extends AppCompatActivity {
    Button goList, goNote, goLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bar);
        goList = findViewById(R.id.goToList);
        goNote = findViewById(R.id.goToNote);
        goLogout = findViewById(R.id.goToLogout);

        goList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "d", Toast.LENGTH_LONG).show();
                Intent go = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(go);
            }
        });

        goNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(go);
            }
        });

        goLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(go);
            }
        });
    }
}

