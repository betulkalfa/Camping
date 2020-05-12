package com.sbk.camping.malzeme;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sbk.camping.R;
import com.sbk.camping.model.Malzeme;

import java.util.List;

public class MalzemeAdapter extends RecyclerView.Adapter<MalzemeAdapter.RowHolder> {


    private List<Malzeme> malzemeList;
    private DatabaseReference myRef;

    public MalzemeAdapter(List<Malzeme> malzemeList) {
        this.malzemeList = malzemeList;
    }

    public class RowHolder extends RecyclerView.ViewHolder {
        private TextView title,description;
        private Button button;
        public RowHolder( View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            button=itemView.findViewById(R.id.button);
        }
    }


    @NonNull
    @Override
    public RowHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_title_description,parent,false);
        return new  RowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RowHolder holder, int position) {
       final Malzeme malzeme = malzemeList.get(position);
       holder.title.setText(malzeme.getAdi());
       holder.description.setText(malzeme.getTuru());
       holder.button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Snackbar.make(holder.button,"Silinsin mi?",Snackbar.LENGTH_SHORT)
                       .setAction("Evet", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {

                              deleteMalzeme(malzeme.getId());
                           }
                       })
                       .show();
           }
       });
    }
    private void deleteMalzeme(String id) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("malzeme").child(id);
        dR.removeValue();

    }
    @Override
    public int getItemCount() {
        return malzemeList != null ? malzemeList.size() : 0;
    }
}
