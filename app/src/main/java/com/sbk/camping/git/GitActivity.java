package com.sbk.camping.git;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sbk.camping.R;
import com.sbk.camping.kamp.GitKampAdapter;
import com.sbk.camping.model.Kamp;

import java.util.ArrayList;
import java.util.List;

public class GitActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    private GitKampAdapter kampAdapter;
    private List<Kamp> kampList = new ArrayList<Kamp>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("kamp");
        kampAdapter = new GitKampAdapter(kampList);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(kampAdapter);


        kampAdapter.setOnClickListener(new GitKampAdapter.OnClickListener() {
            @Override
            public void onClick(String id) {
                Intent intent = new Intent(GitActivity.this, GitDetayActivity.class);
               intent.putExtra("KampID",id);
                startActivity(intent);
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
}
