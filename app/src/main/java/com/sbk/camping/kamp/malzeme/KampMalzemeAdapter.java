package com.sbk.camping.kamp.malzeme;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbk.camping.R;
import com.sbk.camping.model.Malzeme;

import java.util.List;

public class KampMalzemeAdapter extends RecyclerView.Adapter<KampMalzemeAdapter.RowHolder> {

    private List<Malzeme> malzemeList;
    private List<Malzeme> kampMalzemeList;

    public KampMalzemeAdapter(List<Malzeme> malzemeList, List<Malzeme> kampMalzemeList) {
        this.malzemeList = malzemeList;
        this.kampMalzemeList = kampMalzemeList;
    }

    public class RowHolder extends RecyclerView.ViewHolder {
        private RadioButton radioButton;
        private TextView title,description;
        public RowHolder( View itemView) {
            super(itemView);
            radioButton= itemView.findViewById(R.id.radioButton);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

        }
    }

    @NonNull
    @Override
    public RowHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_malzeme,parent,false);
        return new  RowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        final Malzeme malzeme = malzemeList.get(position);
        holder.title.setText(malzeme.getAdi());
        holder.description.setText(malzeme.getTuru());


        boolean isHave = false;
        for (Malzeme item :kampMalzemeList){
            if (item.getId()==malzeme.getId()){
                isHave = true;
                break;
            }
        }

        holder.radioButton.setChecked(isHave);


        final boolean finalIsHave = isHave;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    if (finalIsHave){
                        onClickListener.onUnSelect(malzeme);
                    }else {
                        onClickListener.onSelect(malzeme);
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return malzemeList != null ? malzemeList.size() : 0;
    }


    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onSelect(Malzeme item);
        void onUnSelect(Malzeme item);
    }
}
