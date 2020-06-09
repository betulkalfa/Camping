package com.sbk.camping.git;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sbk.camping.R;

import com.sbk.camping.model.Kamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GitActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference kampRef,df;

    private GitKampAdapter kampAdapter;
    private List<Kamp> kampList = new ArrayList<Kamp>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git);

        final String cihazID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        database = FirebaseDatabase.getInstance();
        kampRef = database.getReference(cihazID);
        df= kampRef.child("kamp");


        kampAdapter = new GitKampAdapter(kampList);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(kampAdapter);


        kampAdapter.setOnClickListener(new GitKampAdapter.OnClickListener() {
            @Override
            public void onClick(String id) {
                Intent intent = new Intent(GitActivity.this, GitDetayActivity.class);
               intent.putExtra("KampID",id);
                startActivity(intent);
            }
        });

        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                kampList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Kamp malzeme = postSnapshot.getValue(Kamp.class);
                    kampList.add(malzeme);
                }
                Collections.sort(kampList, new Comparator<Kamp>() {
                    @Override
                    public int compare(Kamp o1, Kamp o2) {
                        return o1.getTuru().compareTo(o2.getTuru());

                    }

                });
                Collections.sort(kampList, new Comparator<Kamp>() {
                    @Override
                    public int compare(Kamp o1, Kamp o2) {
                        return o1.getAdi().compareTo(o2.getAdi());

                    }

                });
                kampAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView sv;
        sv = new SearchView(this);
        ((TextView) sv.findViewById(sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null))).setTextColor(Color.BLACK);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arama(newText);

                return false;
            }
        });
        menu.add("Ara").setActionView(sv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        return true;
    }
    public void arama(final String aramKelime){

        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                kampList.clear();

                for(DataSnapshot d:dataSnapshot.getChildren()){

                    Kamp kamp = d.getValue(Kamp.class);

                    if(kamp.getTuru().contains(aramKelime )|| kamp.getAdi().contains(aramKelime) ){
                        kamp.setId(d.getKey());
                        kampList.add(kamp);

                    }

                }
                kampAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
