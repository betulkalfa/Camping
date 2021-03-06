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
import com.sbk.camping.kamp.malzeme.KampMalzemeAdapter;
import com.sbk.camping.malzeme.MalzemeAdapter;
import com.sbk.camping.model.Kamp;
import com.sbk.camping.model.Malzeme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GitDetayActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference kampRef, df,malzemedf;
    private String kampID;
    private Kamp kamp;
    private KampMalzemeAdapter kampMalzemeAdapter;
    private List<Malzeme> kampMalzemeList = new ArrayList<Malzeme>();
    private List<Malzeme> olanMalzemeList = new ArrayList<Malzeme>();
    private Malzeme malzeme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_detay);

        final String cihazID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        database = FirebaseDatabase.getInstance();
        kampRef = database.getReference(cihazID);
        df = kampRef.child("kamp");
        malzemedf=df.child("olanMalzemeList");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            kampID = bundle.getString("KampID");
        }

        kampMalzemeAdapter = new KampMalzemeAdapter(kampMalzemeList, olanMalzemeList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(kampMalzemeAdapter);

        kampMalzemeAdapter.setOnClickListener(new KampMalzemeAdapter.OnClickListener() {
            @Override
            public void onSelect(Malzeme item) {
                DatabaseReference dr = df.child(kampID);
                List<Malzeme> ml = new ArrayList<Malzeme>();
                if (kamp.getOlanMalzemeList() != null) {
                    ml.addAll(kamp.getOlanMalzemeList());
                }

                ml.add(item);
                kamp.setOlanMalzemeList(ml);
                dr.setValue(kamp);
            }

            @Override
            public void onUnSelect(Malzeme item) {

                DatabaseReference dr = df.child(kampID);
                List<Malzeme> ml = new ArrayList<Malzeme>();
                if (kamp.getOlanMalzemeList() != null) {
                    ml.addAll(kamp.getOlanMalzemeList());

                }

                for (Malzeme mItem : ml) {
                    if (mItem.getId().equals(item.getId())) {
                        ml.remove(mItem);
                        break;
                    }
                }
                kamp.setOlanMalzemeList(ml);
                dr.setValue(kamp);
            }
        });
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                olanMalzemeList.clear();
                kampMalzemeList.clear();

                kamp = dataSnapshot.child(kampID).getValue(Kamp.class);

                if (kamp != null && kamp.getOlanMalzemeList() != null) {
                    olanMalzemeList.addAll(kamp.getOlanMalzemeList());
                }
                if (kamp != null && kamp.getMalzemeList() != null) {
                    kampMalzemeList.addAll(kamp.getMalzemeList());
                }

                Collections.sort(olanMalzemeList, new Comparator<Malzeme>() {
                    @Override
                    public int compare(Malzeme o1, Malzeme o2) {
                        return o1.getAdi().compareTo(o2.getAdi());
                    }
                });

                kampMalzemeAdapter.notifyDataSetChanged();
                try {
                    getSupportActionBar().setTitle(kamp.getAdi());
                getSupportActionBar().setSubtitle(kamp.getTuru());
                }
                catch (Exception e){

                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

   /* @Override
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

        malzemedf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                olanMalzemeList.clear();

                for(DataSnapshot d:dataSnapshot.getChildren()){

                    Malzeme malzeme = d.getValue(Malzeme.class);

                    if( malzeme.getTuru().toLowerCase().contains(aramKelime )|| malzeme.getAdi().toLowerCase().contains(aramKelime)){
                        malzeme.setId(d.getKey());
                        olanMalzemeList.add(malzeme);
                    }
                }
                kampMalzemeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public boolean malzemeBul(List<Malzeme>olanMalzemeList, List<Malzeme> malzemeList) {

        for (Malzeme m : malzemeList) {

            if (m.getId().toLowerCase().equals(olanMalzemeList)) {
                return true;
            }

        }
        return false;

        }*/


}