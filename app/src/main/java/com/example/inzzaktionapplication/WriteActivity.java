package com.example.inzzaktionapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.wefika.flowlayout.FlowLayout;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.InputStream;

public class WriteActivity extends AppCompatActivity implements AutoPermissionsListener {
    EditText edContentName, edContent;
    Button btnPhoto, btnTag;
    ImageView ivContentPhoto;
    FlowLayout flowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        /*제목*/
        edContentName = findViewById(R.id.edContentName);
        String contentName = edContentName.getText().toString();
        /*태그추가*/
        btnTag = findViewById(R.id.btnTag);
        flowLayout = findViewById(R.id.layoutTag);
        final FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT /* layout_width */, FlowLayout.LayoutParams.WRAP_CONTENT /* layout_height*/);

        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = new TextView(getApplicationContext());
                tv.setText("new");
                tv.setLayoutParams(layoutParams);
                tv.setClickable(true);
                flowLayout.addView(tv);

            }
        });

        /*sd카드 갤러리 사진 가져와서 뿌리기*/
        ivContentPhoto = findViewById(R.id.ivContentPhoto);
        ivContentPhoto.setVisibility(View.INVISIBLE);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                ivContentPhoto.setVisibility(View.VISIBLE);
            }
        });

        /*내용*/
        edContent = findViewById(R.id.editContent);
        String content = edContent.getText().toString();

        /*autopermission*/
        AutoPermissions.Companion.loadAllPermissions(this, 101);
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
        if(requestCode==102){
            if(resultCode==RESULT_OK){
                Uri fileUri = data.getData();
                ContentResolver resolver = getContentResolver();
                try{
                    InputStream is = resolver.openInputStream(fileUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ivContentPhoto.setImageBitmap(bitmap);
                    is.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /*autopermission interface*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int i, @NotNull String[] strings) {
        Toast.makeText(this, "permissions denied:" + strings.length, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onGranted(int i, @NotNull String[] strings) {
        Toast.makeText(this, "permissions granted:" + strings.length, Toast.LENGTH_LONG).show();
    }
}