package com.sbk.camping.git;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
}
