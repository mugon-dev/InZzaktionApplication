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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText editId, editPw;
    Button btn ,btn2;
    private String serverURL = "http://192.168.0.24:8090/inZzaktion/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        editId = findViewById(R.id.editId);
        editPw = findViewById(R.id.editPw);

        btn = findViewById(R.id.btnLogin);
        btn2= findViewById(R.id.btnJoin2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = editId.getText().toString();
                String pw = editPw.getText().toString();

                LoginTask task = new LoginTask();

                task.execute(id, pw);

                /*Intent intent = new Intent(login2Activity.this, listActivity.class);
                startActivity(intent);*/
            }
        });
    }

    class LoginTask extends AsyncTask<String, String, String> {
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
                Log.i("i",sendMsg);
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
                Log.i("i",s);
            String memberNo=null;
            String str=null;
            try {
                JSONObject response = new JSONObject(s);
                memberNo = response.getString("memberNo");
                str = response.getString("str");
                Log.i("result: ",str+memberNo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (str.equals("로그인 완료")) {
                Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                intent.putExtra("memberNo",memberNo);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }

        }
    }

}