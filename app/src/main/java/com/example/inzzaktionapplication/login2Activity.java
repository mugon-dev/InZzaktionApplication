package com.example.inzzaktionapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class login2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ImageView s = (ImageView) findViewById(R.id.imgInsta);

        s.setX(-10);
        s.setY(-20);
    }
}