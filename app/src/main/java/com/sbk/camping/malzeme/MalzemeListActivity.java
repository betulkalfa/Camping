package com.sbk.camping.malzeme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sbk.camping.R;
import com.sbk.camping.model.Malzeme;

import java.util.ArrayList;
import java.util.List;

public class MalzemeListActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private MalzemeAdapter malzemeAdapter;
    private List<Malzeme> malzemeList = new ArrayList<Malzeme>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malzeme_list);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("malzeme");

        malzemeAdapter = new MalzemeAdapter(malzemeList);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(malzemeAdapter);

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                malzemeEkleDialog();

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                malzemeList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Malzeme malzeme = postSnapshot.getValue(Malzeme.class);
                    malzemeList.add(malzeme);
                }
                malzemeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


    void malzemeEkleDialog() {

        LayoutInflater layout = LayoutInflater.from(this);
        View tasarim = layout.inflate(R.layout.alert_malzeme_ekle, null);
        final EditText edtMalzemeAdi = tasarim.findViewById(R.id.edtMalzemeUrunAd);
        final Spinner spMalzemeTur = tasarim.findViewById(R.id.sp);


        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Kamp Malzemesi Ekleyin");


        ad.setView(tasarim);

        ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String id = myRef.push().getKey();
                myRef.child(id).setValue(new Malzeme(id, edtMalzemeAdi.getText().toString(), spMalzemeTur.getItemAtPosition(spMalzemeTur.getSelectedItemPosition()).toString()));
            }
        });


        ad.setNegativeButton("İptal", null);

        ad.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView sv;
        sv = new SearchView(this);
        ((TextView) sv.findViewById(sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null))).setTextColor(Color.WHITE);
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

                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    public void arama(final String aramKelime){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                malzemeList.clear();

                for(DataSnapshot d:dataSnapshot.getChildren()){

                    Malzeme malzeme = d.getValue(Malzeme.class);

                    if(malzeme.getTuru().contains(aramKelime )|| malzeme.getAdi().contains(aramKelime) ){
                        malzeme.setTuru(d.getKey());
                        malzemeList.add(malzeme);
                        malzeme.setAdi(d.getKey());
                        malzemeList.add(malzeme);
                    }

                }

                malzemeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
