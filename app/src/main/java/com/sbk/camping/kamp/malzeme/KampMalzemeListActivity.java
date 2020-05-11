package com.sbk.camping.kamp.malzeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sbk.camping.R;
import com.sbk.camping.model.Kamp;
import com.sbk.camping.model.Malzeme;

import java.util.ArrayList;
import java.util.List;

public class KampMalzemeListActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference malzemeRef,kampRef;
    private String kampID;
    private Kamp kamp;
    private KampMalzemeAdapter kampMalzemeAdapter;
    private List<Malzeme> malzemeList = new ArrayList<Malzeme>();
    private List<Malzeme> kampMalzemeList = new ArrayList<Malzeme>();
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
        kampRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

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
                // Failed to read value
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
                kampMalzemeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }
}
