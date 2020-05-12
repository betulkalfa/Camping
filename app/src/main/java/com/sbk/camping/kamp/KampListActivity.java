package com.sbk.camping.kamp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sbk.camping.kamp.malzeme.KampMalzemeListActivity;
import com.sbk.camping.R;
import com.sbk.camping.model.Kamp;

import java.util.ArrayList;
import java.util.List;

public class KampListActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    private KampAdapter kampAdapter;
    private List<Kamp> kampList = new ArrayList<Kamp>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamp_list);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("kamp");
        kampAdapter = new KampAdapter(kampList);

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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                kampList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Kamp malzeme = postSnapshot.getValue(Kamp.class);
                    kampList.add(malzeme);
                }
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
                myRef.child(id).setValue(new Kamp(id, kampAdi.getText().toString(),  aciklama.getText().toString(),null));
            }
        });


        ad.setNegativeButton("İptal", null);

        ad.create().show();
    }
}
