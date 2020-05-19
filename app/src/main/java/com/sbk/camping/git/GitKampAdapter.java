package com.sbk.camping.git;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbk.camping.R;
import com.sbk.camping.model.Kamp;

import java.util.List;

public class GitKampAdapter extends RecyclerView.Adapter<GitKampAdapter.RowHolder> {

    private List<Kamp> kampList;

    public GitKampAdapter(List<Kamp> kampList) {
        this.kampList = kampList;
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
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_kamp,parent,false);
        return new RowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return kampList != null ? kampList.size() : 0;
    }


    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClick(String id);
    }
}
