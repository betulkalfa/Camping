package com.sbk.camping.git;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
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
import com.sbk.camping.model.Kamp;
import com.sbk.camping.model.Malzeme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GitDetayActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference kampRef;
    private String kampID;
    private Kamp kamp;
    private KampMalzemeAdapter kampMalzemeAdapter;
    private List<Malzeme> kampMalzemeList = new ArrayList<Malzeme>();
    private List<Malzeme> olanMalzemeList = new ArrayList<Malzeme>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_detay);



        database = FirebaseDatabase.getInstance();
        kampRef = database.getReference("kamp");

        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            kampID = bundle.getString("KampID");
        }


        kampMalzemeAdapter = new KampMalzemeAdapter(kampMalzemeList,olanMalzemeList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(kampMalzemeAdapter);

        kampMalzemeAdapter.setOnClickListener(new KampMalzemeAdapter.OnClickListener() {
            @Override
            public void onSelect(Malzeme item) {
                DatabaseReference dr = kampRef.child(kampID);
                List<Malzeme> ml = new ArrayList<Malzeme>();
                if (kamp.getOlanMalzemeList()!=null){
                    ml.addAll(kamp.getOlanMalzemeList());
                }

                ml.add(item);
                kamp.setOlanMalzemeList(ml);
                dr.setValue(kamp);
            }

            @Override
            public void onUnSelect(Malzeme item) {
                DatabaseReference dr = kampRef.child(kampID);
                List<Malzeme> ml = new ArrayList<Malzeme>();
                if (kamp.getOlanMalzemeList()!=null){
                    ml.addAll(kamp.getOlanMalzemeList());

                }

                for (Malzeme mItem : ml){
                    if (mItem.getId().equals(item.getId())){
                        ml.remove(mItem);
                        break;
                    }
                }
                kamp.setOlanMalzemeList(ml);
                dr.setValue(kamp);
            }
        });
        kampRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

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
                        return o1.getTuru().compareTo(o2.getTuru());

                    }

                });
                Collections.sort(olanMalzemeList, new Comparator<Malzeme>() {
                    @Override
                    public int compare(Malzeme o1, Malzeme o2) {
                        return o1.getAdi().compareTo(o2.getAdi());

                    }

                });

                kampMalzemeAdapter.notifyDataSetChanged();

                getSupportActionBar().setTitle(kamp.getAdi());
                getSupportActionBar().setSubtitle(kamp.getTuru());

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

        menu.add("Kamp Bitir").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();


                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
    public void arama(final String aramKelime){

        kampRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                olanMalzemeList.clear();

                for(DataSnapshot d:dataSnapshot.getChildren()){

                    Malzeme malzeme = d.getValue(Malzeme.class);

                    if(malzeme.getTuru().contains(aramKelime )|| malzeme.getAdi().contains(aramKelime) ){
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
}
