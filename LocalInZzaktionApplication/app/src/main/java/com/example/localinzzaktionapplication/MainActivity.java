package com.example.localinzzaktionapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.localinzzaktionapplication.databaseHelper.WriteDatabaseHelper;
import com.example.localinzzaktionapplication.ui.write.TagFragment;
import com.example.localinzzaktionapplication.ui.write.WriteFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {

    private AppBarConfiguration mAppBarConfiguration;
    WriteFragment writeFragment = new WriteFragment();
    TagFragment tagFragment = new TagFragment();
    SQLiteDatabase database;
    WriteDatabaseHelper writeDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_list, R.id.nav_write, R.id.nav_shared)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        /*autopermission*/
        AutoPermissions.Companion.loadAllPermissions(MainActivity.this, 101);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }

    @Override
    public void onDenied(int i, String[] strings) {
        Toast.makeText(this, "permissions denied:" + strings.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Toast.makeText(this, "permissions granted:" + strings.length, Toast.LENGTH_LONG).show();
    }

    public void changeFragment(int index, int noteNo) {
        switch(index){
            case 1:
                //write -> tag
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, tagFragment.newInstance(noteNo)).addToBackStack(null).commit();
                break;
            case 2:
                //tag -> write
                getSupportFragmentManager().beginTransaction().remove(tagFragment).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, writeFragment).commit();
                break;
        }
    }

    public int noteCount(String memberNo){
        writeDatabaseHelper = new WriteDatabaseHelper(getApplicationContext());
        database = writeDatabaseHelper.getReadableDatabase();
        String sql = "select * from NOTE where MEMBER_NO='" + memberNo + "'";
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount()+1;
        return count;
    }

    public String getMemberNo(){
        Intent intent = getIntent();
        String memberNo = intent.getStringExtra("memberNo");
        return memberNo;
    }
}