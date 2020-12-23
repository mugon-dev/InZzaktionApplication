package com.example.localinzzaktionapplication.ui.write;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.localinzzaktionapplication.MainActivity;
import com.example.localinzzaktionapplication.R;
import com.example.localinzzaktionapplication.databaseHelper.TagDatabaseHelper;
import com.example.localinzzaktionapplication.databaseHelper.WriteDatabaseHelper;
import com.example.localinzzaktionapplication.entity.Tag;
import com.example.localinzzaktionapplication.entity.Write;
import com.wefika.flowlayout.FlowLayout;

import java.io.InputStream;

public class WriteFragment extends Fragment  {

    private WriteViewModel writeViewModel;

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

    public static WriteFragment newInstance() {
        WriteFragment fragment = new WriteFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    EditText edContentName, edContent;
    Button btnPhoto, btnTag, btnSave, btnCancel;
    ImageView ivContentPhoto;
    FlowLayout flowLayout;
    SQLiteDatabase database;
    TagDatabaseHelper tagHelper;
    WriteDatabaseHelper writeDatabaseHelper;
    CheckBox cbPublic;

    String MEMBER_NO = "1";
    int noteNo = 2;
    //countNote();
    String imageName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        writeViewModel =
                ViewModelProviders.of(this).get(WriteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_write, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        writeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        noteNo = mainActivity.noteCount("1");
        edContentName = root.findViewById(R.id.edContentName);
        cbPublic = root.findViewById(R.id.cbPublic);
        btnTag = root.findViewById(R.id.btnTag);
        ivContentPhoto = root.findViewById(R.id.ivContentPhoto);
        btnPhoto = root.findViewById(R.id.btnPhoto);
        edContent = root.findViewById(R.id.editContent);
        btnSave = root.findViewById(R.id.btnSave);
        btnCancel = root.findViewById(R.id.btnCancel);
        flowLayout = root.findViewById(R.id.layoutTag);


        Bundle bundle = getArguments();
        //noteNo = bundle.getParcelable("noteNo");
        //MEMBER_NO = bundle.getParcelable("memberNo");
        printLog("Write noteNo :"+noteNo);

        /*태그추가 이동 note_no 넘기기*/
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getArguments();
                //noteNo = bundle.getParcelable("noteNo");
                //MEMBER_NO = bundle.getParcelable("memberNo");
                mainActivity.changeFragment(1,noteNo);
            }
        });

        /*태그 추가*/
        selectTag(noteNo);

        /*sd카드 갤러리 사진 가져와서 뿌리기*/
        ivContentPhoto.setVisibility(View.INVISIBLE);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                ivContentPhoto.setVisibility(View.VISIBLE);
            }
        });

        /*노트 작성 내용 보내기*/
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*제목*/
                String title = edContentName.getText().toString();
                /*공유 유무*/
                String shared="Y";
                if(cbPublic.isChecked()){
                    shared ="N";
                }
                String content = edContent.getText().toString();
                Write write = new Write();
                write.setMemberNo(MEMBER_NO);
                write.setTitle(title);
                write.setShared(shared);
                write.setPhoto(imageName);
                write.setContent(content);
                printLog("저장: note: ");
                printLog("savedNote : "+write.toString());
                insertNote(write);
                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                startActivity(intent1);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTag(noteNo);
                Intent intent2 = new Intent(getActivity(), MainActivity.class);
                startActivity(intent2);
            }
        });

        return root;
    }

    private int countNote(){
        int count = 0;
        writeDatabaseHelper = new WriteDatabaseHelper(getContext());
        database = writeDatabaseHelper.getReadableDatabase();
        String sql = "select NOTE_NO from NOTE order by NOTE_NO desc";
        Cursor cursor = database.rawQuery(sql, null);
        count = cursor.getInt(0);
        printLog("저장될 노트 번호: " + count);
        return count;
    }

    private void deleteTag(int noteNo){
        tagHelper = new TagDatabaseHelper(getContext());
        database = tagHelper.getReadableDatabase();
        String sql = "delete from TAG where NOTE_NO=" + noteNo;
        database.execSQL(sql);
        printLog("데이터 삭제 성공: " + sql);
    }

    private void insertNote(Write write){
        writeDatabaseHelper = new WriteDatabaseHelper(getContext());
        database = writeDatabaseHelper.getReadableDatabase();
        String sql = "insert into NOTE(MEMBER_NO,NOTE_TITLE,NOTE_CONTENT,NOTE_PHOTO,NOTE_SHARE) values('"+write.getMemberNo()+"','"+ write.getTitle() +"','"+write.getContent()+"','"+write.getPhoto()+"','"+write.getShared()+"')";
        printLog("NOTE insert: "+ sql);
        database.execSQL(sql);
        printLog("NOTE insert 성공");
    }

    private void selectTag(int noteNo) {
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT /* layout_width */, FlowLayout.LayoutParams.WRAP_CONTENT /* layout_height*/);
        layoutParams.setMargins(10, 10, 10, 10);
        tagHelper = new TagDatabaseHelper(getContext());
        database = tagHelper.getReadableDatabase();
        String sql = "select * from TAG where NOTE_NO=" + noteNo + " order by TAG_NO";
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount();
        printLog("count" + count);
        for (int i = 0; i < count; i++) {
            cursor.moveToNext();
            final Tag tag = new Tag(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            printLog("write select 성공: " + tag.toString());
            final TextView tv = new TextView(getContext());
            tv.setText(tag.getTagName());
            tv.setLayoutParams(layoutParams);
            tv.setBackgroundColor(Color.parseColor(cardColor(tag.getRgbNo())));
            tv.setId(i);
            flowLayout.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sql = "delete from tag where TAG_NO='" + tag.getTagNo() + "'";
                    database.execSQL(sql);
                    printLog("데이터 삭제 성공: " + sql);
                    flowLayout.removeView(tv);
                }
            });
        }
        printLog("select 성공");
    }

    private String cardColor(String colorNo) {
        String hexColor = "#fffafa";
        if (colorNo.equals("0")) {
            hexColor = "#E3DDCB";
        } else if (colorNo.equals("1")) {
            hexColor = "#E2A6B4";
        } else if (colorNo.equals("2")) {
            hexColor = "#F8E77F";
        } else if (colorNo.equals("3")) {
            hexColor = "#BB9E8B";
        } else if (colorNo.equals("4")) {
            hexColor = "#0B6DB7";
        } else if (colorNo.equals("5")) {
            hexColor = "#6C71B5";
        } else if (colorNo.equals("6")) {
            hexColor = "#9ED6C0";
        } else if (colorNo.equals("7")) {
            hexColor = "#CBDD61";
        } else if (colorNo.equals("8")) {
            hexColor = "#84A7D3";
        } else if (colorNo.equals("9")) {
            hexColor = "#006494";
        }
        return hexColor;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 102);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 102){
            Uri fileUri = data.getData();
            printLog("fileUri: " +fileUri.toString());
            try {
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                Bitmap bitmapCopy = Bitmap.createScaledBitmap(bitmapImage, 400, 300, true);
                ivContentPhoto.setImageBitmap(bitmapCopy);  //이미지를 띄울 위젯 ID값
                imageName = getImagePathToUri(fileUri);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getImagePathToUri(Uri data) {

        String uri = data.toString();
        printLog("imgPath: "+uri);

        //이미지의 이름 값
        String imgName = uri.substring(uri.lastIndexOf("/") + 1);

        return imgName;
    }//end of getImagePathToUri()

    private void printLog(String data) {
        Log.d("WriteActivity", data);
    }
}