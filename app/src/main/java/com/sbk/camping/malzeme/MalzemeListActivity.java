package com.sbk.camping.malzeme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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
}
