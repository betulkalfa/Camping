package com.sbk.camping.kamp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sbk.camping.R;
import com.sbk.camping.model.Kamp;

import java.util.List;

public class KampAdapter extends RecyclerView.Adapter<KampAdapter.RowHolder> {

    private List<Kamp> kampList;

    public KampAdapter(List<Kamp> kampList) {
        this.kampList = kampList;
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
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_title_description,parent,false);
        return new RowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RowHolder holder, int position) {
        final Kamp kamp = kampList.get(position);
        holder.title.setText(kamp.getAdi());
        holder.description.setText(kamp.getTuru());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null){
                   onClickListener.onClick(kamp.getId());
                }
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(holder.button,"Silinsin mi?",Snackbar.LENGTH_SHORT)
                        .setAction("Evet", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                deleteKamp(kamp.getId());
                            }
                        })
                        .show();
            }
        });

            }






    @Override
    public int getItemCount() {
        return kampList != null ? kampList.size() : 0;
    }
    private void deleteKamp(String id) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("kamp").child(id);
        dR.removeValue();

    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;

    }

    public interface OnClickListener{
        void onClick(String id);
        void  sil(String id);
    }
}
