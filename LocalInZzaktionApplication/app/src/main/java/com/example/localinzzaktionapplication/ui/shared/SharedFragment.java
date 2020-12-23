package com.example.localinzzaktionapplication.ui.shared;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.localinzzaktionapplication.MainActivity;
import com.example.localinzzaktionapplication.R;
import com.example.localinzzaktionapplication.adapter.SharedAdapter;
import com.example.localinzzaktionapplication.entity.NoteSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SharedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SharedFragment extends Fragment {
    MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SharedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SharedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SharedFragment newInstance(String param1, String param2) {
        SharedFragment fragment = new SharedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SharedAdapter adapter;
    static RequestQueue requestQueue;
    ImageView imageView;
    ImageButton ibtnSearch;
    Button btnLiked;
    EditText edSearch;
    String tagNm;
    List<NoteSearch> tagList = new ArrayList<NoteSearch>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shared, container, false);
        // Inflate the layout for this fragment
        recyclerView = root.findViewById(R.id.rvTags);
        ibtnSearch = root.findViewById(R.id.ibSearch);
        btnLiked = root.findViewById(R.id.btnLiked);
        edSearch = root.findViewById(R.id.edSearch);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        requestQueue = Volley.newRequestQueue(getContext());

        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTagSearch();
            }
        });

        return root;
    }

    private void getTagSearch() {
        String url = "http://192.168.0.24:8090/inZzaktion/TagSearch.do";

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
                                printLog(addTag.toString());
                                tagList.add(addTag);
                            }
                            adapter = new SharedAdapter(tagList);
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
    private void printLog(String data) {
        Log.d("SharedActivity", data);
    }
}