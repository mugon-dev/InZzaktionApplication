package com.example.inzzaktionapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    NoteAdapter adapter;
    static RequestQueue requestQueue;
    ImageView imageView;
    ImageButton ibtnSearch;
    Button btnLiked;
    EditText edSearch;
    String tagNm;
    List<NoteSearch> tagList = new ArrayList<NoteSearch>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note);
        recyclerView = findViewById(R.id.rvTags);
        ibtnSearch = findViewById(R.id.ibSearch);
        btnLiked = findViewById(R.id.btnLiked);
        edSearch = findViewById(R.id.edSearch);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        requestQueue = Volley.newRequestQueue(this);
        /*
        adapter.setOnItemClickListener(new OnTagItemClickListener() {
            @Override
            public void onItemClick(TagAdapter.ViewHolder holder, View view, int position) {
                TagSearch tagSearch = adapter.items.get(position);
                Toast.makeText(getApplicationContext(), adapter.items.get(position).tagNm, Toast.LENGTH_LONG).show();
            }
        });

         */
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //menu_bar 엑티비티를 객체화 시켜서 불러옴
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout li = (LinearLayout) inflater.inflate(R.layout.activity_menu_bar,null);
                li.setVisibility(View.INVISIBLE);


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
                addContentView(li,params);

                li.setVisibility(View.VISIBLE);
                li.setBackgroundColor(Color.parseColor("#4D000000"));

                //menu_bar 액티비티에 있는 버튼 클릭시 화면 전환하는 코드
                Button b1, b2, b3;
                b1= findViewById(R.id.goToList);
                b2= findViewById(R.id.goToNote);
                b3=findViewById(R.id.goToLogout);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent go = new Intent(getApplicationContext(),ListActivity.class);
                        startActivity(go);
                    }
                });

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent go = new Intent(getApplicationContext(),NoteActivity.class);
                        startActivity(go);
                    }
                });
                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent go = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(go);
                    }
                });
            }
        });

        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTagSearch();
            }
        });
    }

    public void getTagSearch() {
        String url = "http://192.168.0.24:8090/InZzaktion/TagSearch.do";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("contents");
                            List<NoteSearch> tagList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                NoteSearch addTag = new NoteSearch();

                                addTag.setTitle(obj.getString("title"));
                                addTag.setPhoto(obj.getString("photo"));
                                addTag.setTagNm(obj.getString("tagNm"));
                                addTag.setRgbCode(obj.getString("rgbCode"));
                                addTag.setLikedCount(obj.getString("likedCount"));
                                addTag.setLiked(obj.getString("liked"));
                                addTag.setContent(obj.getString("content"));
                                //tagSearch.setRgstDt(obj.getString("rgstDt"));
                                addTag.setNo(obj.getInt("no"));
                                addTag.setNo(obj.getInt("noteNo"));

                                tagList.add(addTag);
                            }
                            adapter = new NoteAdapter(tagList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
}
