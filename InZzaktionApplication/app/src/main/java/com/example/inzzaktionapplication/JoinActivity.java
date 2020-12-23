package com.example.inzzaktionapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JoinActivity extends AppCompatActivity {
    Button btnJoin ,btnLogin2;
    EditText editId, editPw, editEmail;
    //학원 24
    private String serverURL = "http://192.168.0.24:8090/InZzaktion/inzzaktion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join);
        btnJoin = findViewById(R.id.btnJoin);
        editId = findViewById(R.id.editId);
        editPw = findViewById(R.id.editPw);
        editEmail = findViewById(R.id.editMail);
        btnLogin2 = findViewById(R.id.btnLogin22);


        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = editId.getText().toString();
                String pw = editPw.getText().toString();
                String email = editEmail.getText().toString();

                /*if(id==""){
                    Toast.makeText(getApplicationContext(),"아이디를 입력하세요",Toast.LENGTH_LONG).show();
                }*/
                RegisterTask task = new RegisterTask();
                task.execute(id, pw, email);

                /*Intent intent = new Intent(MainActivity.this,login2Activity.class);
                startActivity(intent);*/
                /*intent.putExtra("ID",id);
                intent.putExtra("PW",pw);
                intent.putExtra("EMAIL",email);*/


            }
        });
    }

    class RegisterTask extends AsyncTask<String, String, String> {
        String sendMsg, receiverMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str = "";
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.d("id", strings[0]);
                Log.d("pw", strings[1]);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),
                        "UTF-8");

                sendMsg = "id=" + strings[0] + "&pw=" + strings[1];
                Log.i("join: ",sendMsg);
                osw.write(sendMsg);
                osw.flush();
                osw.close();

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader isr = new InputStreamReader(conn.getInputStream(),
                            "UTF-8");
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }

                    receiverMsg = buffer.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiverMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(JoinActivity.this, s, Toast.LENGTH_SHORT).show();

            if (s.equals("회원가입 성공")) {
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(JoinActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
            }


        }
    }
}