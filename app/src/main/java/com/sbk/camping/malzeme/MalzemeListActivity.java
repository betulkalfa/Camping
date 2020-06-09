package com.sbk.camping.malzeme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MalzemeListActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef, ref;
    ;
    private MalzemeAdapter malzemeAdapter;
    private List malzemeList = new ArrayList<>();
    private Malzeme malzeme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malzeme_list);
        final Malzeme malzeme = new Malzeme();

        String cihaID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        ref = myRef.child(cihaID).child("malzeme");


        malzemeAdapter = new MalzemeAdapter(malzemeList, this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(malzemeAdapter);

        tumMalzeme();

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                malzemeEkleDialog(malzeme);


            }
        });


    }

    public void tumMalzeme() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                malzemeList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Malzeme malzeme = postSnapshot.getValue(Malzeme.class);
                    malzemeList.add(malzeme);

                    Collections.sort(malzemeList, new Comparator<Malzeme>() {
                        @Override
                        public int compare(Malzeme o1, Malzeme o2) {
                            return o1.getAdi().compareTo(o2.getAdi());
                        }
                    });

                }
                malzemeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }


    void malzemeEkleDialog(final Malzeme malzeme) {


        LayoutInflater layout = LayoutInflater.from(this);
        final View tasarim = layout.inflate(R.layout.alert_malzeme_ekle, null);
        final EditText edtMalzemeAdi = tasarim.findViewById(R.id.edtMalzemeUrunAd);
        final Spinner spMalzemeTur = tasarim.findViewById(R.id.sp);

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("Kamp Malzemesi Ekleyin");
        ad.setView(tasarim);
        ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String id = ref.push().getKey();


                if (!edtMalzemeAdi.getText().toString().isEmpty()) {


                    String kelime = arama(edtMalzemeAdi.getText().toString());


                    if (edtMalzemeAdi.getText().toString() != kelime) {

                        ref.child(id).setValue(new Malzeme(id, edtMalzemeAdi.getText().toString(), spMalzemeTur.getItemAtPosition(spMalzemeTur.getSelectedItemPosition()).toString()));
                        tumMalzeme();

                    } else {
                        tumMalzeme();
                        Toast.makeText(getApplicationContext(), "Lütfen Farklı Malzeme Adı Giriniz.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Lütfen Malzeme Adı Giriniz.", Toast.LENGTH_LONG).show();

                }
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

        sv.setInputType((InputType.TYPE_TEXT_VARIATION_PERSON_NAME));
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
                tumMalzeme();
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    public String arama(final String aramKelime) {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                malzemeList.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    Malzeme malzeme = d.getValue(Malzeme.class);

                    if (malzeme.getAdi().contains(aramKelime)) {
                        malzeme.setId(d.getKey());
                        malzemeList.add(malzeme);

                    }

                }
                malzemeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return aramKelime;
    }
}
