package com.sbk.camping.malzeme;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbk.camping.R;
import com.sbk.camping.model.Malzeme;

import java.util.List;

public class MalzemeAdapter extends RecyclerView.Adapter<MalzemeAdapter.RowHolder> {

    private List<Malzeme> malzemeList;

    public MalzemeAdapter(List<Malzeme> malzemeList) {
        this.malzemeList = malzemeList;
    }

    public class RowHolder extends RecyclerView.ViewHolder {
        private TextView title,description;
        public RowHolder( View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

        }
    }


    @NonNull
    @Override
    public RowHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_title_description,parent,false);
        return new  RowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
       Malzeme malzeme = malzemeList.get(position);
       holder.title.setText(malzeme.getAdi());
       holder.description.setText(malzeme.getTuru());
    }

    @Override
    public int getItemCount() {
        return malzemeList != null ? malzemeList.size() : 0;
    }
}
