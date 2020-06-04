package com.sbk.camping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sbk.camping.git.GitActivity;
import com.sbk.camping.kamp.KampListActivity;
import com.sbk.camping.malzeme.MalzemeListActivity;
import com.sbk.camping.model.Cihaz;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    TextView tv;
    private DatabaseReference ref;
    private FirebaseDatabase database;
    private Cihaz cihaz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=(TextView) findViewById(R.id.tv);
        String id= Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        tv.setText(id);

        database = FirebaseDatabase.getInstance();
        ref=database.getReference("cihaz");

        String cid=Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        ref.child(cid).setValue(new Cihaz(cid));

        findViewById(R.id.malzemeler).setOnClickListener(this);
        findViewById(R.id.kamplar).setOnClickListener(this);
        findViewById(R.id.kampaGit).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.malzemeler :{
                startActivity(new Intent(MainActivity.this, MalzemeListActivity.class));
                break;
            }
            case  R.id.kamplar :{
                startActivity(new Intent(MainActivity.this, KampListActivity.class));
                break;
            }
            case  R.id.kampaGit :{
                startActivity(new Intent(MainActivity.this, GitActivity.class));
                break;
            }
        }
    }
}
