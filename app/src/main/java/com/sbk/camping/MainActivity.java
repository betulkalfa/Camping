package com.sbk.camping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sbk.camping.git.GitActivity;

import com.sbk.camping.kamp.KampListActivity;

import com.sbk.camping.malzeme.MalzemeListActivity;


public class MainActivity extends AppCompatActivity implements  View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
