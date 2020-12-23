package com.example.inzzaktionapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.wefika.flowlayout.FlowLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WriteActivity extends AppCompatActivity implements AutoPermissionsListener {
    EditText edContentName, edContent;
    Button btnPhoto, btnTag, btnSave, btnCancel;
    ImageView ivContentPhoto;
    FlowLayout flowLayout;
    SQLiteDatabase database;
    TagDatabaseHelper tagHelper;
    WriteDatabaseHelper writeDatabaseHelper;
    CheckBox cbPublic;
    String noteNo = "-1";
    String MEMBER_NO = "1";


    private String serverURL = "http://192.168.0.24:8090/inZzaktion/write";
    private String img_path = new String();
    private String imageName = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_write);
        Intent intent = getIntent();
        noteNo = intent.getStringExtra("noteNo");
        MEMBER_NO = intent.getStringExtra("memberNo");
        printLog("Write noteNo :"+noteNo);

        edContentName = findViewById(R.id.edContentName);
        cbPublic = findViewById(R.id.cbPublic);
        btnTag = findViewById(R.id.btnTag);
        ivContentPhoto = findViewById(R.id.ivContentPhoto);
        btnPhoto = findViewById(R.id.btnPhoto);
        edContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        flowLayout = findViewById(R.id.layoutTag);

        if(savedInstanceState!=null){
            String title = savedInstanceState.getString("title");
            String shared = savedInstanceState.getString("shared");
            String content = savedInstanceState.getString("content");
            printLog("저장된 데이터 : "+title+shared+content);

            edContentName.setText(title);
            if(shared.equals("1")){
                cbPublic.setChecked(true);
            }
            edContent.setText(content);
        }

        /*태그추가 이동 note_no 넘기기*/
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TagActivity.class);
                intent.putExtra("noteNo", noteNo); /*noteNo 가져와서 넘기기*/
                startActivityForResult(intent, 103);
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
                String writeJson = write.toJsonString();
                String tagJson = insertNoteTag(noteNo);
                printLog("저장: note: ");
                printLog("savedNote json: "+writeJson);
                printLog("savedTag json: "+tagJson);
                insertNote(write);
                //RegisterTask task = new RegisterTask();
                //task.execute(writeJson, tagJson);
                //DoFileUpload(serverURL,img_path,savedNote,savedTag);
                //deleteTag(noteNo);
                onBackPressed();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTag(noteNo);
                onBackPressed();
            }
        });

        /*autopermission*/
        //AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    private void insertNote(Write write){
        writeDatabaseHelper = new WriteDatabaseHelper(this);
        database = writeDatabaseHelper.getReadableDatabase();
        String sql = "insert into NOTE(MEMBER_NO,NOTE_TITLE,NOTE_CONTENT,NOTE_PHOTO,NOTE_SHARE) values('"+write.getMemberNo()+"','"+ write.getTitle() +"','"+write.getContent()+"','"+write.getPhoto()+"','"+write.getShared()+"')";
        printLog("NOTE insert: "+ sql);
        database.execSQL(sql);
        printLog("NOTE insert 성공");
    }

    private String insertNoteTag(String noteNo){
        String savedTag=null;
        tagHelper = new TagDatabaseHelper(this);
        database = tagHelper.getReadableDatabase();
        String sql = "select * from TAG where NOTE_NO='" + noteNo + "' order by TAG_NO";
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
            savedTag = tag.toJsonString();
            printLog("savedTag 성공: " + savedTag);
        }
        return savedTag;
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

    private void deleteTag(String noteNo){
        tagHelper = new TagDatabaseHelper(this);
        database = tagHelper.getReadableDatabase();
        String sql = "delete from TAG where NOTE_NO='" + noteNo + "'";
        database.execSQL(sql);
        printLog("데이터 삭제 성공: " + sql);
    }

    private void selectTag(String noteNo) {
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT /* layout_width */, FlowLayout.LayoutParams.WRAP_CONTENT /* layout_height*/);
        layoutParams.setMargins(10, 10, 10, 10);
        tagHelper = new TagDatabaseHelper(this);
        database = tagHelper.getReadableDatabase();
        String sql = "select * from TAG where NOTE_NO='" + noteNo + "' order by TAG_NO";
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
            final TextView tv = new TextView(getApplicationContext());
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

    private int selectNoteCount() {
        int count = 0;
        tagHelper = new TagDatabaseHelper(this);
        database = tagHelper.getReadableDatabase();
        String sql = "select * from note";
        Cursor cursor = database.rawQuery(sql, null);
        count = cursor.getCount();
        return count+1;
    }

    //저장할때 태그의 노트번호를 서버노트번호로 변환
    private void updateTagNoteNo(String serverNo){
        tagHelper = new TagDatabaseHelper(this);
        database = tagHelper.getReadableDatabase();
        String sql = "update Tag set NOTE_NO=replace(NOTE_NO,'-1',"+serverNo+")";
        database.execSQL(sql);
    }

    private void updateNoteServerNo(String noteNo){
        writeDatabaseHelper = new WriteDatabaseHelper(this);
        database = writeDatabaseHelper.getReadableDatabase();
        String sql = "update NOTE set SERVER_NO='"+noteNo+"'";
        database.execSQL(sql);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                printLog("fileUri: " +fileUri.toString());
                img_path = getImagePathToUri(fileUri);
                printLog("img_path: " +img_path);
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream is = resolver.openInputStream(fileUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    Bitmap bitmapCopy = Bitmap.createScaledBitmap(bitmap, 400, 300, true);
                    ImageView ivContentPhoto = (ImageView) findViewById(R.id.ivContentPhoto);  //이미지를 띄울 위젯 ID값
                    ivContentPhoto.setImageBitmap(bitmapCopy);

                    /*bitmap을 byte로 저장
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String byteString = new String(byteArray, "utf-8");
                    printLog("byteString: " + byteString);
                    write.setPhoto(byteString);
                    bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);*/


                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        String uri = data.toString();
        printLog("imgPath: "+uri);

        //이미지의 이름 값
        String imgName = uri.substring(uri.lastIndexOf("/") + 1);
        this.imageName = imgName;

        return imgName;
    }//end of getImagePathToUri()

    public void DoFileUpload(String apiUrl, String absolutePath, String savedNote, String savedTag) {
        HttpFileUpload(apiUrl, "", absolutePath, savedNote, savedTag);
    }

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    public void HttpFileUpload(String urlString, String params, String fileName,String savedNote, String savedTag) {
        try {

            FileInputStream mFileInputStream = new FileInputStream(fileName);
            URL connectUrl = new URL(urlString);
            printLog("mFileInputStream  is " + mFileInputStream);

            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // write data
            OutputStream httpConnOutputStream = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(httpConnOutputStream);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dos.writeUTF("Write"+savedNote+lineEnd);
            dos.writeUTF("Tag"+savedTag+lineEnd);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes("Content-Type: image/jpeg" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            printLog("image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            printLog("File is written");
            mFileInputStream.close();
            dos.flush();
            // finish upload...

            // get response
            InputStream is = conn.getInputStream();

            StringBuffer b = new StringBuffer();
            for (int ch = 0; (ch = is.read()) != -1; ) {
                b.append((char) ch);
            }
            is.close();
            printLog("get response: "+b.toString());


        } catch (Exception e) {
            printLog("exception " + e.getMessage());
            // TODO: handle exception
        }
    } // end of HttpFileUpload()

    //이미지 업로드 제외
    class RegisterTask extends AsyncTask<String, String, String>{
        String sendMsg, receiverMsg;

        @Override
        protected String doInBackground(String... strings) {
            try{
                String str = "";
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                printLog("Write"+strings[0]);
                printLog("Tag"+strings[1]);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),
                        "UTF-8");
                sendMsg = "Write="+strings[0] + "&Tag=" + strings[1];
                printLog(sendMsg);
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
                    printLog("저장할 노트번호:"+receiverMsg);
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return receiverMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            printLog("전송받은데이터: "+s);
            updateTagNoteNo(s);
            updateNoteServerNo(s);
            printLog("db note번호 업데이트 완료");
        }
    }


    //화면 데이터 저장

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String title = edContentName.getText().toString();
        String content = edContent.getText().toString();
        String shared="0";
        if(cbPublic.isChecked()){
            shared ="1";
        }
        printLog("onSaveInstanceState"+title+":"+shared+":"+content);
        outState.putString("title",title);
        outState.putString("content",content);
        outState.putString("shared",shared);
    }


    /*autopermission interface*/

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

    private void printLog(String data) {
        Log.d("WriteActivity", data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        flowLayout.removeAllViews();
        selectTag(noteNo);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}