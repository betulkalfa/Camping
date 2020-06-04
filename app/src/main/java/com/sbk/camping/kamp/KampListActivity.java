package com.sbk.camping.kamp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sbk.camping.kamp.malzeme.KampMalzemeListActivity;
import com.sbk.camping.R;
import com.sbk.camping.model.Cihaz;
import com.sbk.camping.model.Kamp;
import com.sbk.camping.model.Malzeme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KampListActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private KampAdapter kampAdapter;
    private List<Kamp> kampList = new ArrayList<Kamp>();
    private  DatabaseReference ref;
    private Cihaz cihaz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamp_list);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("kamp");
        kampAdapter = new KampAdapter(kampList,this);



        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(kampAdapter);
        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kampEkleDialog();

            }
        });

        kampAdapter.setOnClickListener(new KampAdapter.OnClickListener() {
            @Override
            public void onClick(String id) {
                Intent intent = new Intent(KampListActivity.this, KampMalzemeListActivity.class);
                intent.putExtra("KampID",id);
                startActivity(intent);
            }

            @Override
            public void sil(String id) {

            }
        });
        tumKampListele();

    }
    private void tumKampListele(){
        myRef.addValueEventListener(new ValueEventListener() {
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

    private void kampEkleDialog() {

        LayoutInflater layout = LayoutInflater.from(this);
        View tasarim = layout.inflate(R.layout.alert_kamp_ekle, null);
        final EditText kampAdi = tasarim.findViewById(R.id.kampAdi);
        final EditText aciklama = tasarim.findViewById(R.id.aciklama);



        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Kamp Ekleyin");


        ad.setView(tasarim);
        ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String id = myRef.push().getKey();
                myRef.child(id).setValue(new Kamp(id, kampAdi.getText().toString(), aciklama.getText().toString(),null));





            }
        });


        ad.setNegativeButton("İptal", null);

        ad.create().show();
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
                tumKampListele();
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
    public void arama(final String aramKelime){

        myRef.addValueEventListener(new ValueEventListener() {
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
