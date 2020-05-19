package com.sbk.camping.kamp.malzeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.sbk.camping.MainActivity;
import com.sbk.camping.R;
import com.sbk.camping.malzeme.MalzemeAdapter;
import com.sbk.camping.model.Kamp;
import com.sbk.camping.model.Malzeme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class KampMalzemeListActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference malzemeRef,kampRef;
    private String kampID;
    private Kamp kamp;
    private KampMalzemeAdapter kampMalzemeAdapter;
    private MalzemeAdapter malzemeAdapter;
    private List<Malzeme> malzemeList = new ArrayList<Malzeme>();
    private List<Malzeme> kampMalzemeList = new ArrayList<Malzeme>();
    SharedPreferences  sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamp_malzeme_list);

        database = FirebaseDatabase.getInstance();
        kampRef = database.getReference("kamp");
        malzemeRef = database.getReference("malzeme");

        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            kampID = bundle.getString("KampID");
        }

        kampMalzemeAdapter = new KampMalzemeAdapter(malzemeList,kampMalzemeList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(kampMalzemeAdapter);

        kampMalzemeListele();

        kampRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                kampMalzemeList.clear();
                kamp = dataSnapshot.child(kampID).getValue(Kamp.class);
                if (kamp != null && kamp.getMalzemeList() != null) {
                    kampMalzemeList.addAll(kamp.getMalzemeList());
                }

                kampMalzemeAdapter.notifyDataSetChanged();

              getSupportActionBar().setTitle(kamp.getAdi());
              getSupportActionBar().setSubtitle(kamp.getTuru());

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        malzemeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                malzemeList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Malzeme malzeme = postSnapshot.getValue(Malzeme.class);
                    malzemeList.add(malzeme);
                }
                Collections.sort(malzemeList, new Comparator<Malzeme>() {
                    @Override
                    public int compare(Malzeme o1, Malzeme o2) {
                        return o1.getAdi().compareTo(o2.getAdi());
                    }
                });
                Collections.sort(malzemeList, new Comparator<Malzeme>() {
                    @Override
                    public int compare(Malzeme o1, Malzeme o2) {
                        return o1.getTuru().compareTo(o2.getTuru());
                    }
                });
                kampMalzemeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void kampMalzemeListele(){
        kampMalzemeAdapter.setOnClickListener(new KampMalzemeAdapter.OnClickListener() {
            @Override
            public void onSelect(Malzeme item) {
                DatabaseReference dr = kampRef.child(kampID);
                List<Malzeme> ml = new ArrayList<Malzeme>();

                if (kamp.getMalzemeList()!=null){
                    ml.addAll(kamp.getMalzemeList());
                }

                ml.add(item);
                kamp.setMalzemeList(ml);
                dr.setValue(kamp);
            }

            @Override
            public void onUnSelect(Malzeme item) {
                DatabaseReference dr = kampRef.child(kampID);
                List<Malzeme> ml = new ArrayList<Malzeme>();
                if (kamp.getMalzemeList()!=null){
                    ml.addAll(kamp.getMalzemeList());

                }

                for (Malzeme mItem : ml){
                    if (mItem.getId().equals(item.getId())){
                        ml.remove(mItem);
                        break;
                    }
                }
                kamp.setMalzemeList(ml);
                dr.setValue(kamp);
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

        menu.add("Yenile").setIcon(R.drawable.ic_refresh_black_24dp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                kampMalzemeListele();

                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    public void arama(final String aramKelime){

        kampRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                malzemeList.clear();

                for(DataSnapshot d:dataSnapshot.getChildren()){

                    Malzeme malzeme = d.getValue(Malzeme.class);

                    if(malzeme.getTuru().contains(aramKelime )|| malzeme.getAdi().contains(aramKelime) ){
                        malzeme.setId(d.getKey());
                        malzemeList.add(malzeme);
                    }

                }

                kampMalzemeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getSetting(){
        sp=getSharedPreferences("myref", MODE_PRIVATE);
        boolean radio=sp.getBoolean("rd",true);
    }

}
