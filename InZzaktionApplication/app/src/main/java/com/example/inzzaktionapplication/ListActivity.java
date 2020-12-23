package com.example.inzzaktionapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ListActivity extends AppCompatActivity implements AutoPermissionsListener {
    private String serverURL = "http://192.168.0.24:8090/InZzaktion/List";
    EditText editSearch;
    ImageView listSearch, ivMenu;
    Button btnCreateNote;
    RecyclerView recyclerView;

    String memberNo=null;

    LinearLayoutManager layoutManager;
    ListNoteAdapter listNoteAdapter;
    SQLiteDatabase database;
    TagDatabaseHelper tagHelper;
    WriteDatabaseHelper writeDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list);
        editSearch = findViewById(R.id.editSearch);
        listSearch = findViewById(R.id.listSearch);
        btnCreateNote = findViewById(R.id.btnCreateNote);
        recyclerView = findViewById(R.id.listRecyclerView);
        ivMenu= findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout li = (LinearLayout) inflater.inflate(R.layout.activity_menu_bar, null);
                li.setVisibility(View.INVISIBLE);


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                addContentView(li, params);

                li.setVisibility(View.VISIBLE);
                li.setBackgroundColor(Color.parseColor("#4D000000"));

                Button b1, b2, b3;
                b1 = findViewById(R.id.goToList);
                b2 = findViewById(R.id.goToNote);
                b3 = findViewById(R.id.goToLogout);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent go = new Intent(getApplicationContext(), ListActivity.class);
                        startActivity(go);
                    }
                });

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent go = new Intent(getApplicationContext(), NoteActivity.class);
                        startActivity(go);
                    }
                });
                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent go = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(go);
                    }
                });
            }
        });
        final Intent intent = getIntent();
        memberNo = intent.getStringExtra("memberNo");
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        listNoteAdapter = new ListNoteAdapter();
        selectNoteData(memberNo);
        recyclerView.setAdapter(listNoteAdapter);
        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListActivity.this,WriteActivity.class);
                intent1.putExtra("memberNo","1");
                int count = noteCount(memberNo)+1;
                intent1.putExtra("noteNo",count);
                startActivity(intent1);
            }
        });

        /*autopermission*/
        AutoPermissions.Companion.loadAllPermissions(ListActivity.this, 101);
    }
    private void selectNoteData(String memberNo) {
        writeDatabaseHelper = new WriteDatabaseHelper(getApplicationContext());
        database = writeDatabaseHelper.getReadableDatabase();
        //listAdapter.writes.clear();
        String sql = "select NOTE_TITLE,NOTE_NO from NOTE where MEMBER_NO='" + memberNo + "'";
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            cursor.moveToNext();
            listNoteAdapter.addNoteItem(cursor.getString(0), cursor.getString(1));
            printLog("list note title: "+cursor.getString(0));
            printLog("list note no: "+cursor.getString(1));
            listNoteAdapter.notifyDataSetChanged();
        }
        printLog("note title select 성공");
    }

    private int noteCount(String memberNo){
        writeDatabaseHelper = new WriteDatabaseHelper(getApplicationContext());
        database = writeDatabaseHelper.getReadableDatabase();
        String sql = "select * from NOTE where MEMBER_NO='" + memberNo + "'";
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount();
        return count;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, (AutoPermissionsListener) this);
    }

    @Override
    public void onDenied(int i, @NotNull String[] strings) {
        Toast.makeText(this, "permissions denied:" + strings.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, @NotNull String[] strings) {
        Toast.makeText(this, "permissions granted:" + strings.length, Toast.LENGTH_LONG).show();
    }

    /*class ListTask extends AsyncTask<String , String , String> {
        String sendMsg, receiverMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str = "";
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.d("tag_nm", strings[0]);

                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),
                        "UTF-8");
                sendMsg = "memberNo=" + strings[0];
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
            printLog("list 받아온 데이터: "+s);
            String memberNo="-1";
            String noteNo="1";
            Intent intent = new Intent(ListActivity.this, WriteActivity.class);
            intent.putExtra("memberNo",memberNo);
            intent.putExtra("noteNo",noteNo);
            //startActivity(intent);
            *//*try {
                JSONObject response = new JSONObject(s);
                memberNo = response.getString("memberNo");
                noteNo = response.getString("noteNo");
                Log.i("result: ",noteNo+memberNo);
                Intent intent = new Intent(ListActivity.this, WriteActivity.class);
                intent.putExtra("memberNo",memberNo);
                intent.putExtra("noteNo",noteNo);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }*//*
        }
    }*/

    private void printLog(String data) {
        Log.d("ListActivity", data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recyclerView.removeAllViews();
        selectNoteData(memberNo);
    }
}
