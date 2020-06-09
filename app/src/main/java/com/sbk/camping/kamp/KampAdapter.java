package com.sbk.camping.kamp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sbk.camping.R;
import com.sbk.camping.model.Kamp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KampAdapter extends RecyclerView.Adapter<KampAdapter.RowHolder> {

    private List<Kamp> kampList;
    DatabaseReference myRef;
    DatabaseReference ref;
    Task<Void> def;
    private Context context;
    FirebaseDatabase database;


    public KampAdapter(List<Kamp> kampList,Context context) {

        this.kampList = kampList;
        this.context=context;
    }

    public class RowHolder extends RecyclerView.ViewHolder {
        private TextView title,description;
        private Button button,button2;
        public RowHolder( View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            button=itemView.findViewById(R.id.button);
            button2=itemView.findViewById(R.id.button2);

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

                deleteKamp(kamp.getId());

            }
        });

        holder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateKamp(kamp);
            }
        });

    }
    @Override
    public int getItemCount() {
        return kampList != null ? kampList.size() : 0;
    }



    private void deleteKamp(final String id) {
        final AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("Kamp Malzemesi Silinsin mi?");

        ad.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    String cihazID= Settings.Secure.getString(context.getContentResolver() ,Settings.Secure.ANDROID_ID);

                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference(cihazID);
                    ref= myRef.child("kamp").child(id);

                    ref.removeValue();


            }
        });

        ad.setNegativeButton("İptal", null);

        ad.create().show();

    }

    public void updateKamp(final Kamp kamp){



        LayoutInflater layout = LayoutInflater.from(context);
        final View tasarim = layout.inflate(R.layout.alert_kamp_ekle, null);
        final EditText edtAdi = tasarim.findViewById(R.id.kampAdi);
        final EditText edtTur = tasarim.findViewById(R.id.aciklama);
        final AlertDialog.Builder ad = new AlertDialog.Builder(context);

        ad.setTitle("Kamp  Güncelleyin");
        ad.setView(tasarim);

        edtAdi.setText(kamp.getAdi());
        edtTur.setText(kamp.getTuru());

        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               // String adi= edtAdi.getText().toString().trim();
                String adi=String.valueOf(edtAdi.getText());
                String turu= edtTur.getText().toString().trim();

                Map<String,Object> bilgiler = new HashMap<>();

                bilgiler.put("adi",adi);
                bilgiler.put("turu",turu);

                String cihazID=Settings.Secure.getString(context.getContentResolver() ,Settings.Secure.ANDROID_ID);

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference(cihazID);

                def= myRef.child("kamp").child(kamp.getId()).updateChildren(bilgiler);

            }
        });

        ad.setNegativeButton("İptal", null);

        ad.create().show();

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
